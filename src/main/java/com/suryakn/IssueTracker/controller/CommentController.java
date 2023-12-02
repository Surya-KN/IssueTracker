package com.suryakn.IssueTracker.controller;

import com.suryakn.IssueTracker.entity.Comment;
import com.suryakn.IssueTracker.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/tickets/{ticketId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> all(@PathVariable Long ticketId) {
        return commentService.getAllComments(ticketId);
    }

    @PostMapping
    public ResponseEntity<Comment> saveComment(@PathVariable Long ticketId, @RequestBody Map<String, String> body) {
        return commentService.addComment(ticketId, body);
    }

//    @DeleteMapping
//    TODO: delete individual comment
//    public void deleteComment(@PathVariable Long ticketId) {
//        commentRepository.
//    }
}
