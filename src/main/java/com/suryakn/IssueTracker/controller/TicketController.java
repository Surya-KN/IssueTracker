package com.suryakn.IssueTracker.controller;

import com.suryakn.IssueTracker.entity.Ticket;
import com.suryakn.IssueTracker.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<Ticket>> all() {
        return ticketService.getAllTickets();
    }

    @GetMapping("{id}")
    public ResponseEntity<Ticket> ticketWithId(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @PostMapping
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket newTicket) {
//        newTicket.setCreated_at(LocalDateTime.now());
//        newTicket.setModified_at(LocalDateTime.now());
        return ticketService.addTicket(newTicket);
    }

    @PutMapping("{id}")
    public ResponseEntity<Ticket> replaceTicket(@RequestBody Ticket newTicket, @PathVariable Long id) {
        return ticketService.updateTicket(newTicket, id);

    }

    //    @PatchMapping("/{id}") TODO: if possible
//    Ticket updateTicket(@PathVariable Long id, @RequestBody Map<String, String> body) {
//        body.forEach((key,value) -> {
//            any
//        });
//    }
    @DeleteMapping("{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
}
