package com.suryakn.IssueTracker.dto;

import com.suryakn.IssueTracker.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Comment> comments;
    private CreatedByDto created;
    private CreatedByDto assigned;

}
