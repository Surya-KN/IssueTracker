package com.suryakn.IssueTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "ticketid", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Ticket ticket;

    public Comment(String comment, Ticket ticket) {
        this.comment = comment;
        this.ticket = ticket;
    }

}
