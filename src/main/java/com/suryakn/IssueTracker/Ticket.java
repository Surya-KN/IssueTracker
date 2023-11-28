package com.suryakn.IssueTracker;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;
    private String description;
    private String status;
    private String priority;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdTime;
    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedTime;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments;

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", createdTime=" + createdTime +
                ", modifiedTime=" + modifiedTime +
                ", comments=" + comments +
                '}';
    }

    private static final Logger log = LoggerFactory.getLogger(IssueTrackerApplication.class);

    protected Ticket() {
    }

    public Ticket(Long id, String title, String description, String status, String priority, LocalDateTime createdTime, LocalDateTime modifiedTime, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.comments = comments;
    }

    public Ticket(String title, String description, String status, String priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    public Ticket(String title, String description, String status, String priority, List<String> commentsList) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        List<Comment> comments = new ArrayList<>();
        log.info(commentsList.toString());
        for (String comment :
                commentsList) {
            comments.add(new Comment(comment, this));
        }
        log.info(comments.toString());
        this.comments = comments;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime created_at) {
        this.createdTime = created_at;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modified_at) {
        this.modifiedTime = modified_at;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

