package com.suryakn.IssueTracker.service;

import com.suryakn.IssueTracker.entity.Ticket;
import com.suryakn.IssueTracker.repository.TicketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {


    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }


    public ResponseEntity<List<Ticket>> getAllTickets() {
        return new ResponseEntity<>(ticketRepository.findAll(), HttpStatus.OK);
    }


    public ResponseEntity<Ticket> getTicketById(Long id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        return optionalTicket.map(ticket -> new ResponseEntity<>(ticket, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Ticket> addTicket(Ticket newTicket) {
        return new ResponseEntity<>(ticketRepository.save(newTicket), HttpStatus.CREATED);
    }

    public ResponseEntity<Ticket> updateTicket(Ticket newTicket, Long id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        return optionalTicket.map(ticket -> {
            ticket.setTitle(newTicket.getTitle());
            ticket.setStatus(newTicket.getStatus());
            ticket.setDescription(newTicket.getDescription());
            ticket.setPriority(newTicket.getPriority());
            //            ticket.setModified_at(LocalDateTime.now());

            return new ResponseEntity<>(ticket, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
