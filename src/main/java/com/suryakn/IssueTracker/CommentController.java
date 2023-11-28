package com.suryakn.IssueTracker;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets/{ticketid}/comments")
public class CommentController {
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;


    public CommentController(CommentRepository commentRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostMapping
    public Comment saveComment(@PathVariable Long ticketid, @RequestBody Map<String, String> body) {
        Ticket ticket = ticketRepository.findById(ticketid).orElseThrow();
        return commentRepository.save(new Comment(body.get("comment"), ticket));
    }

    @GetMapping
    public List<Comment> all(@PathVariable Long ticketid) {
        return commentRepository.findByTicket_Id(ticketid);
    }

//    @DeleteMapping
//    TODO: delete individual comment
//    public void deleteComment(@PathVariable Long ticketid) {
//        commentRepository.
//    }
}
