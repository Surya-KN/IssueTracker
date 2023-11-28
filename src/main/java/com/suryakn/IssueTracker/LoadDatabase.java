package com.suryakn.IssueTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(IssueTrackerApplication.class);

    @Bean
    public CommandLineRunner demo(TicketRepository ticketRepository, CommentRepository commentRepository) {
        return (args) -> {
            Ticket ticket = ticketRepository.save(new Ticket("Database Connectivity", "Application not connecting to the database", "Open", "High"));
            commentRepository.saveAll(Arrays.asList(new Comment("This is the first comment.", ticket), new Comment("This is the second comment.", ticket)));

            // repository.save(new Ticket("Database Connectivity", "Application not connecting to the database", "Open", "priority"));
//            repository.save(new Ticket("Page Load Error", "Home page is not loading", "Open", "priority"));
//            repository.save(new Ticket("Broken Link", "Link to the user guide is broken", "Open", "priority"));
//            repository.save(new Ticket("Performance Issue", "Application is running slow", "Open", "priority"));
//            repository.save(new Ticket("UI Misalignment", "Buttons on the form are misaligned", "Open", "priority"));
//            repository.save(new Ticket("Missing Feature", "Export to PDF feature is missing", "Open", "priority"));
//            repository.save(new Ticket("Security Bug", "Password field is not encrypted", "Open", "priority"));
//            repository.save(new Ticket("Data Loss", "Data entered in the form is not getting saved", "Open", "priority"));
//            repository.save(new Ticket("Crash", "Application crashes on clicking 'Submit'", "Open", "priority"));

//            log.info("Tickets found with findAll():");
//            log.info("-------------------------------");
//            repository.findAll().forEach(ticket -> log.info(ticket.toString()));
//            log.info("");
//
//            Ticket ticket = repository.findById(1L).orElseThrow();
//            log.info("Ticket found with findById(1):");
//            log.info("--------------------------------");
//            log.info(ticket.toString());
//            log.info("");
        };
    }
}
