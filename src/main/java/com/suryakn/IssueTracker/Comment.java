package com.suryakn.IssueTracker;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;
    @ManyToOne
    @JoinColumn(name = "ticketid", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Ticket ticket;

    public Comment() {
    }

    public Comment(String comment, Ticket ticket) {
        this.comment = comment;
        this.ticket = ticket;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", ticket=" + ticket +
                '}';
    }
}
