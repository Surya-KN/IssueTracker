package com.suryakn.IssueTracker.service;

import com.suryakn.IssueTracker.controller.TicketRequest;
import com.suryakn.IssueTracker.controller.TicketResponse;
import com.suryakn.IssueTracker.entity.Ticket;
import com.suryakn.IssueTracker.entity.UserEntity;
import com.suryakn.IssueTracker.repository.TicketRepository;
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

    private TicketResponse getTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .createdAt(ticket.getCreatedAt())
                .modifiedAt(ticket.getModifiedAt())
                .comments(ticket.getComments())
                .firstName(ticket.getUser().getFirstName())
                .lastName(ticket.getUser().getFirstName())
                .email(ticket.getUser().getEmail())
                .build();
    }

    public ResponseEntity<TicketResponse> addTicket(TicketRequest ticketRequest) {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userEntity);
        Ticket ticket = Ticket.builder()
                .title(ticketRequest.getTitle())
                .description(ticketRequest.getDescription())
                .status(ticketRequest.getStatus())
                .priority(ticketRequest.getPriority())
                .user(userEntity)
                .build();
//        newTicket.setUser(userEntity);
        return new ResponseEntity<>(getTicketResponse(ticketRepository.save(ticket)), HttpStatus.CREATED);
    }

    public ResponseEntity<TicketResponse> updateTicket(TicketRequest ticketRequest, Long id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        return optionalTicket.map(ticket -> {
            ticket.setTitle(ticketRequest.getTitle());
            ticket.setStatus(ticketRequest.getStatus());
            ticket.setDescription(ticketRequest.getDescription());
            ticket.setPriority(ticketRequest.getPriority());
            //            ticket.setModified_at(LocalDateTime.now());
            return new ResponseEntity<>(getTicketResponse(ticket), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
