package com.suryakn.IssueTracker;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;

    TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    List<Ticket> all() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    Ticket ticketWithId(@PathVariable Long id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    @PostMapping
    Ticket addTicket(@RequestBody Ticket newTicket) {
//        newTicket.setCreated_at(LocalDateTime.now());
//        newTicket.setModified_at(LocalDateTime.now());
        return ticketRepository.save(newTicket);
    }

    @PutMapping("/{id}")
    Ticket replaceTicket(@RequestBody Ticket newTicket, @PathVariable Long id) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setTitle(newTicket.getTitle());
            ticket.setStatus(newTicket.getStatus());
            ticket.setDescription(newTicket.getDescription());
            ticket.setPriority(newTicket.getPriority());
//            ticket.setModified_at(LocalDateTime.now());
            return ticketRepository.save(ticket);
        }).orElseThrow();

    }

    @DeleteMapping("/{id}")
    void deleteTicket(@PathVariable Long id) {
        ticketRepository.deleteById(id);
    }
}
