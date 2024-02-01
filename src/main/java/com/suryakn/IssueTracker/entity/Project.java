package com.suryakn.IssueTracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Project {

    @GeneratedValue
    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
//    @JsonManagedReference
    private List<Ticket> tickets;

    public Project(String name) {
        this.name = name;
    }
}
