package com.suryakn.IssueTracker.service;

import com.suryakn.IssueTracker.dto.*;
import com.suryakn.IssueTracker.duplicate.DuplicateTicketRequest;
import com.suryakn.IssueTracker.duplicate.DuplicateTicketService;
import com.suryakn.IssueTracker.duplicate.PythonResponse;
import com.suryakn.IssueTracker.entity.Comment;
import com.suryakn.IssueTracker.entity.Ticket;
import com.suryakn.IssueTracker.entity.UserEntity;
import com.suryakn.IssueTracker.entity.VectorTable;
import com.suryakn.IssueTracker.repository.ProjectRepository;
import com.suryakn.IssueTracker.repository.TicketRepository;
import com.suryakn.IssueTracker.repository.UserRepository;
import com.suryakn.IssueTracker.repository.VectorTableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {


    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final DuplicateTicketService duplicateTicketService;
    private final VectorTableRepository vectorTableRepository;

    //    private final TicketMapper ticketMapper;
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        List<TicketResponse> ticketResponses = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketResponses.add(getTicketResponse(ticket));
        }
        return ResponseEntity.ok(ticketResponses);
    }

    public ResponseEntity<TicketResponse> getTicketById(Long id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Ticket ticket = optionalTicket.get();
        return ResponseEntity.ok(getTicketResponse(ticket));
    }


    public ResponseEntity<TicketResponse> addTicket(TicketRequest ticketRequest) {
//        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(userEntity);
        UserEntity userEntity = userRepository.findByEmail(ticketRequest.getReporter()).orElseThrow();
        UserEntity assignee = userRepository.findByEmail(ticketRequest.getAssignee()).orElseThrow();
        System.out.println(ticketRequest);

        /////////////////////////
        DuplicateTicketRequest duplicateTicketRequest = DuplicateTicketRequest.builder().ticketId(2000L)
                .title(ticketRequest.getTitle())
                .description(ticketRequest.getDescription())
                .projectId(ticketRequest.getProject())
                .build();

        PythonResponse pythonResponse = duplicateTicketService.processTicketEmbedding(duplicateTicketRequest);
        List<Long> ids = pythonResponse.getSimilar_ticket_ids();
        List<Ticket> similarTicketList = new ArrayList<>();
        for (Long id : ids) {
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            ticketOptional.ifPresent(similarTicketList::add);
        }
        System.out.println(pythonResponse.getVector());
        //////////////////////////////////
        Ticket ticket = Ticket.builder()
                .title(ticketRequest.getTitle())
                .description(ticketRequest.getDescription())
                .status(ticketRequest.getStatus())
                .priority(ticketRequest.getPriority())
                .createdBy(userEntity)
                .assignedTo(assignee)
                .project(projectRepository.findById(ticketRequest.getProject()).orElseThrow())
                .build();
//        ticket.setVector(pythonResponse.getVector());
        Ticket newTicket = ticketRepository.save(ticket);
        TicketResponse ticketResponse = getTicketResponse(newTicket, similarTicketList);
        addVectorTable(pythonResponse.getVector(), newTicket.getId(), ticketRequest.getProject());
        return new ResponseEntity<>(ticketResponse, HttpStatus.CREATED);
    }

    public void addVectorTable(String vector, Long ticketId, Long projectId) {
        vectorTableRepository.save(VectorTable.builder().vector(vector).ticketId(ticketId).projectId(projectId).build());
    }

    public ResponseEntity<TicketResponse> updateTicket(TicketRequest ticketRequest, Long id) {

        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        return optionalTicket.map(ticket -> {
            if (ticketRequest.getTitle() != null) {
                ticket.setTitle(ticketRequest.getTitle());
            }

            if (ticketRequest.getDescription() != null) {
                ticket.setDescription(ticketRequest.getDescription());
            }

            if (ticketRequest.getStatus() != null) {
                ticket.setStatus(ticketRequest.getStatus());
            }

            if (ticketRequest.getPriority() != null) {
                ticket.setPriority(ticketRequest.getPriority());
            }
            ticketRepository.save(ticket);
            return ResponseEntity.ok(getTicketResponse(ticket));
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public void assignTicket(Long ticketId, AssignRequest assignRequest) {

        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (assignRequest.getEmail().equals("")) {
            optionalTicket.get().setAssignedTo(null);
            System.out.println("ok");
            return;
        }
        Optional<UserEntity> user = userRepository.findByEmail(assignRequest.getEmail());
        if (optionalTicket.isEmpty() || user.isEmpty()) {
            System.out.println("not found");
            return;
        }

        optionalTicket.get().setAssignedTo(user.get());
        ticketRepository.save(optionalTicket.get());
    }

    private List<CommentDto> getCommentList(List<Comment> comments) {
        List<CommentDto> commentString = new ArrayList<>();
        for (Comment c : comments) {
            commentString.add(CommentDto.builder().comment(c.getComment()).build());
        }
        return commentString;
    }

    private TicketResponse getTicketResponse(Ticket ticket, List<Ticket> ticketList) {

        CreatedByDto assignedTo = null;
        if (ticket.getAssignedTo() != null) {
            assignedTo = CreatedByDto.builder()
                    .firstName(ticket.getAssignedTo().getFirstName())
                    .lastName(ticket.getAssignedTo().getLastName())
                    .email(ticket.getAssignedTo().getEmail())
                    .build();
        }
        List<CommentDto> commentDtos = null;

        if (ticket.getComments() != null) {
            commentDtos = getCommentList(ticket.getComments());
        }
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .createdAt(ticket.getCreatedAt())
                .modifiedAt(ticket.getModifiedAt())
                .comments(commentDtos)
                .created(CreatedByDto.builder()
                        .firstName(ticket.getCreatedBy().getFirstName())
                        .lastName(ticket.getCreatedBy().getLastName())
                        .email(ticket.getCreatedBy().getEmail())
                        .build())
                .assigned(assignedTo)
                .project(new ProjectDto(ticket.getProject()))
                .similarTickets(ticketList)
                .build();
    }

    private TicketResponse getTicketResponse(Ticket ticket) {

        CreatedByDto assignedTo = null;
        if (ticket.getAssignedTo() != null) {
            assignedTo = CreatedByDto.builder()
                    .firstName(ticket.getAssignedTo().getFirstName())
                    .lastName(ticket.getAssignedTo().getLastName())
                    .email(ticket.getAssignedTo().getEmail())
                    .build();
        }
        List<CommentDto> commentDtos = null;

        if (ticket.getComments() != null) {
            commentDtos = getCommentList(ticket.getComments());
        }
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .createdAt(ticket.getCreatedAt())
                .modifiedAt(ticket.getModifiedAt())
                .comments(commentDtos)
                .created(CreatedByDto.builder()
                        .firstName(ticket.getCreatedBy().getFirstName())
                        .lastName(ticket.getCreatedBy().getLastName())
                        .email(ticket.getCreatedBy().getEmail())
                        .build())
                .assigned(assignedTo)
                .project(new ProjectDto(ticket.getProject()))
                .build();
    }

    @Transactional
    public ResponseEntity<List<TicketResponse>> getAllTicketByProjectId(Long pid) {
        List<Ticket> tickets = ticketRepository.findAllByProjectId(pid);
        List<TicketResponse> ticketResponses = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketResponses.add(getTicketResponse(ticket));
        }
        return ResponseEntity.ok(ticketResponses);
    }

    public ResponseEntity<TicketResponse> getTicketByProjectId(Long pid, Long tid) {
        Optional<Ticket> optionalTicket = ticketRepository.findByProjectIdAndId(pid, tid);
        if (optionalTicket.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Ticket ticket = optionalTicket.get();
        return ResponseEntity.ok(getTicketResponse(ticket));
    }

//    public List<Ticket> getDuplicates(TicketRequest ticketRequest) {
//        DuplicateTicketRequest duplicateTicketRequest = DuplicateTicketRequest.builder().ticketId(2000L)
//                .title(ticketRequest.getTitle())
//                .description(ticketRequest.getDescription())
//                .projectId(ticketRequest.getProject())
//                .build();
//
//        PythonResponse pythonResponse = duplicateTicketService.processTicketEmbedding(duplicateTicketRequest);
//        List<Long> ids = pythonResponse.getSimilar_ticket_ids();
//        List<Ticket> similarTicketList = new ArrayList<>();
//        for (Long id : ids) {
//            similarTicketList.add(ticketRepository.findById(id).orElseThrow());
//        }
//        return similarTicketList;
//    }
}
