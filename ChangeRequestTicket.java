import java.time.LocalDateTime;
import java.util.List;
//ID PREFIX CR
public class ChangeRequestTicket extends Ticket { // implement dicussable
    private String movieTicketID;


    public ChangeRequestTicket(String ticketID, String ticketTitle, String ticketDescription, LocalDateTime creationTime, Customer customer, SupportStaff supportStaff, String priorityLevel, InteractionLog interactionLog, List<Comment> discussionThread, String movieTicketID) {
        super(ticketID, ticketTitle, ticketDescription, creationTime, customer, supportStaff, priorityLevel, interactionLog, discussionThread);
        this.movieTicketID = movieTicketID;

    }
    
    // Getters and Setters
    public String getMovieTicketID() { return movieTicketID; }


    @Override
    public String getTicketType() {
        return "Change Request Ticket";
    }
}