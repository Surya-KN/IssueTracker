package com.suryakn.IssueTracker.repository;

import com.suryakn.IssueTracker.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByProjectId(Long pid);

    Optional<Ticket> findByProjectIdAndId(Long pid, Long tid);

    //    List<Ticket> findByTitle(String title);

}
