import java.time.LocalDateTime;
import java.util.List;
//ID PREFIX PR
public class ProblemTicket extends Ticket { // implement dicussable
    private String severityLevel;

    
    public ProblemTicket(String ticketID, String ticketTitle, String ticketDescription, LocalDateTime creationTime, Customer customer, SupportStaff supportStaff, String priorityLevel, InteractionLog interactionLog, List<Comment> discussionThread, String severityLevel) {
        super(ticketID, ticketTitle, ticketDescription, creationTime, customer, supportStaff, priorityLevel, interactionLog, discussionThread);
        this.severityLevel = severityLevel;
    }

    // Getters and Setters
    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }

    @Override
    public String getTicketType() {
        return "Problem Ticket";
    }

}
