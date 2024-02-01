package com.suryakn.IssueTracker.service;

import com.suryakn.IssueTracker.dto.*;
import com.suryakn.IssueTracker.entity.Comment;
import com.suryakn.IssueTracker.entity.Ticket;
import com.suryakn.IssueTracker.entity.UserEntity;
import com.suryakn.IssueTracker.repository.ProjectRepository;
import com.suryakn.IssueTracker.repository.TicketRepository;
import com.suryakn.IssueTracker.repository.UserRepository;
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

        Ticket ticket = Ticket.builder()
                .title(ticketRequest.getTitle())
                .description(ticketRequest.getDescription())
                .status(ticketRequest.getStatus())
                .priority(ticketRequest.getPriority())
                .createdBy(userEntity)
                .assignedTo(assignee)
                .project(projectRepository.findById(ticketRequest.getProject()).orElseThrow())
                .build();
        ticketRepository.save(ticket);
        return new ResponseEntity<>(getTicketResponse(ticket), HttpStatus.CREATED);
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
}
