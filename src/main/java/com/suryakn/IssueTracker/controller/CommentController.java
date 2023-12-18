package com.suryakn.IssueTracker.controller;

import com.suryakn.IssueTracker.dto.CommentDto;
import com.suryakn.IssueTracker.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/tickets/{ticketId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> all(@PathVariable Long ticketId) {
        return commentService.getAllComments(ticketId);
    }

    @PostMapping
    public ResponseEntity<CommentDto> saveComment(@PathVariable Long ticketId, @RequestBody Map<String, String> body) {
        return commentService.addComment(ticketId, body);
    }

//    @DeleteMapping
//    TODO: delete individual comment
//    public void deleteComment(@PathVariable Long ticketId) {
//        commentRepository.
//    }
}
