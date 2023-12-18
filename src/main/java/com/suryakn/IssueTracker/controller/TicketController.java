package com.suryakn.IssueTracker.controller;

import com.suryakn.IssueTracker.dto.AssignRequest;
import com.suryakn.IssueTracker.dto.TicketRequest;
import com.suryakn.IssueTracker.dto.TicketResponse;
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
    public ResponseEntity<List<TicketResponse>> all() {
        return ticketService.getAllTickets();
    }

    @GetMapping("{id}")
    public ResponseEntity<TicketResponse> ticketWithId(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @PostMapping("{ticketId}/assign")
    public void assignTicket(@PathVariable Long ticketId, @RequestBody AssignRequest assignRequest) {
        ticketService.assignTicker(ticketId, assignRequest);
    }

    @PostMapping
    public ResponseEntity<TicketResponse> addTicket(@RequestBody TicketRequest newTicket) {
//        newTicket.setCreated_at(LocalDateTime.now());
//        newTicket.setModified_at(LocalDateTime.now());
        return ticketService.addTicket(newTicket);
    }

    @PutMapping("{id}")

    public ResponseEntity<TicketResponse> replaceTicket(@RequestBody TicketRequest ticketRequest, @PathVariable Long id) {
        return ticketService.updateTicket(ticketRequest, id);

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
