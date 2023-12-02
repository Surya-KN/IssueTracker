package com.suryakn.IssueTracker.repository;

import com.suryakn.IssueTracker.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    //    List<Ticket> findByTitle(String title);

}
