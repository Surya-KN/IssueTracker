package com.suryakn.IssueTracker.entity;

import com.suryakn.IssueTracker.IssueTrackerApplication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private static final Logger log = LoggerFactory.getLogger(IssueTrackerApplication.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;

    //    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonManagedReference
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity assignedTo;

    public Ticket(String title, String description, String status, String priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

}

