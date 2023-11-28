package com.suryakn.IssueTracker;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;

    TicketController(TicketRepository ticketRepository, CommentRepository commentRepository) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
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
//    @Transactional
    public void deleteTicket(@PathVariable Long id) {
//        commentRepository.deleteAllByTicket_Id(id);
        ticketRepository.deleteById(id);
    }
}
