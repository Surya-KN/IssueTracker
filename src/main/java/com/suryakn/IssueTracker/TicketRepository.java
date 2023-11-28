package com.suryakn.IssueTracker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    //    List<Ticket> findByTitle(String title);

}
