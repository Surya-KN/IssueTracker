package com.suryakn.IssueTracker.service;

import com.suryakn.IssueTracker.dto.CommentDto;
import com.suryakn.IssueTracker.entity.Comment;
import com.suryakn.IssueTracker.entity.Ticket;
import com.suryakn.IssueTracker.entity.UserEntity;
import com.suryakn.IssueTracker.repository.CommentRepository;
import com.suryakn.IssueTracker.repository.TicketRepository;
import com.suryakn.IssueTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public List<CommentDto> commentList(List<Comment> comments) {
        List<CommentDto> commentString = new ArrayList<>();
        for (Comment c : comments) {
            commentString.add(CommentDto.builder().comment(c.getComment()).build());
        }
        return commentString;
    }

    public ResponseEntity<CommentDto> addComment(Long ticketId, Map<String, String> body) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userEntity.getId();

        return optionalTicket.map(ticket -> {
            Comment comment = Comment.builder()
                    .comment(body.get("comment"))
                    .ticket(ticket)
                    .createdBy(userRepository.findById(userId).orElseThrow())
                    .build();
//            System.out.println(userEntity);
            return new ResponseEntity<>(CommentDto.builder().comment(commentRepository.save(comment).getComment()).build(), HttpStatus.CREATED);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<CommentDto>> getAllComments(Long ticketId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            return new ResponseEntity<>(commentList(commentRepository.findByTicket_Id(ticketId)), HttpStatus.OK);
        }
    }
}
