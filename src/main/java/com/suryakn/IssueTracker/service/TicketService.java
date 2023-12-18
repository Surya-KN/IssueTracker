package com.suryakn.IssueTracker.service;

import com.suryakn.IssueTracker.dto.*;
import com.suryakn.IssueTracker.entity.Comment;
import com.suryakn.IssueTracker.entity.Ticket;
import com.suryakn.IssueTracker.entity.UserEntity;
import com.suryakn.IssueTracker.repository.TicketRepository;
import com.suryakn.IssueTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {


    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
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
        return new ResponseEntity<>(getTicketResponse(ticket), HttpStatus.OK);
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
                .build();
    }

    public ResponseEntity<TicketResponse> addTicket(TicketRequest ticketRequest) {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(userEntity);
        Ticket ticket = Ticket.builder()
                .title(ticketRequest.getTitle())
                .description(ticketRequest.getDescription())
                .status(ticketRequest.getStatus())
                .priority(ticketRequest.getPriority())
                .createdBy(userEntity)
                .build();
        ticketRepository.save(ticket);
        return new ResponseEntity<>(getTicketResponse(ticket), HttpStatus.CREATED);
    }

    public ResponseEntity<TicketResponse> updateTicket(TicketRequest ticketRequest, Long id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        return optionalTicket.map(ticket -> {
            ticket.setTitle(ticketRequest.getTitle());
            ticket.setStatus(ticketRequest.getStatus());
            ticket.setDescription(ticketRequest.getDescription());
            ticket.setPriority(ticketRequest.getPriority());
            //            ticket.setModified_at(LocalDateTime.now());
            ticketRepository.save(ticket);
            return new ResponseEntity<>(getTicketResponse(ticket), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public void assignTicket(Long ticketId, AssignRequest assignRequest) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        Optional<UserEntity> user = userRepository.findByEmail(assignRequest.getEmail());
        if (optionalTicket.isEmpty() || user.isEmpty()) {
            System.out.println("not found");
            return;
        }
        optionalTicket.get().setAssignedTo(user.get());
        ticketRepository.save(optionalTicket.get());
    }
}
