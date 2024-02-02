package com.suryakn.IssueTracker.service.duplicateTicket;

import com.suryakn.IssueTracker.dto.TicketRequest;
import com.suryakn.IssueTracker.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
@RequiredArgsConstructor
public class duplicateTicket {

    private final TicketIndexer ticketIndexer;
    private final OpenNlpPreprocessor preprocessor;

    public List<String> findDuplicates(Ticket ticket) throws IOException {
        // Combine title and description
        String combinedText = ticket.getTitle() + " " + ticket.getDescription();


        String preprocessedText = preprocessor.preprocess(combinedText);


        // Index the ticket if it's not already indexed
        ticketIndexer.indexTicket(ticket.getProject().getId(), ticket.getId(), preprocessedText);
        // Search for duplicates

        return ticketIndexer.searchDuplicates(ticket.getProject().getId(), preprocessedText);

    }

}
}
