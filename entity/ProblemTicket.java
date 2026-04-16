package entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ProblemTicket - Ticket subclass for problem reports
 */
public class ProblemTicket extends Ticket {
    private String resolutionSteps;

    public ProblemTicket(String ticketID, String ticketTitle, String ticketDescription, 
                         LocalDateTime creationTime, Customer customer, SupportStaff supportStaff, 
                         String priorityLevel, InteractionLog interactionLog, 
                         List<Comment> discussionThread, String resolutionSteps) {
        super(ticketID, ticketTitle, ticketDescription, creationTime, customer, supportStaff, 
              priorityLevel, interactionLog, discussionThread);
        this.resolutionSteps = resolutionSteps;
    }

    // Getters and Setters
    public String getResolutionSteps() { return resolutionSteps; }
    public void setResolutionSteps(String resolutionSteps) { this.resolutionSteps = resolutionSteps; }

    @Override
    public String getTicketType() {
        return "Problem Ticket";
    }

    @Override
    public String toString() {
        return String.format("ProblemTicket [ID: %s, Title: %s, Severity: %s, Status: %s, Priority: %s]",
            getTicketID(), getTicketTitle(), resolutionSteps, getStatus(), getPriorityLevel());
    }
}
