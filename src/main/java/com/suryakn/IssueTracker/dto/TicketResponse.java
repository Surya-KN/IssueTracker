package com.suryakn.IssueTracker.dto;

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
    private List<CommentDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private CreatedByDto created;
    private CreatedByDto assigned;

}
