package com.suryakn.IssueTracker.service;

import com.suryakn.IssueTracker.entity.Comment;
import com.suryakn.IssueTracker.entity.Ticket;
import com.suryakn.IssueTracker.repository.CommentRepository;
import com.suryakn.IssueTracker.repository.TicketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }


    public ResponseEntity<Comment> addComment(Long ticketId, Map<String, String> body) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        return optionalTicket.map(ticket -> {
            Comment comment = new Comment(body.get("comment"), ticket);
            Comment savedComment = commentRepository.save(comment);
            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<Comment>> getAllComments(Long ticketId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(commentRepository.findByTicket_Id(ticketId), HttpStatus.OK);
        }
    }
}
