package entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * OtherTicket - Ticket subclass for miscellaneous tickets
 */
public class OtherTicket extends Ticket {
    public OtherTicket(String ticketID, String ticketTitle, String ticketDescription, 
                       LocalDateTime creationTime, Customer customer, SupportStaff supportStaff, 
                       String priorityLevel, InteractionLog interactionLog, 
                       List<Comment> discussionThread) {
        super(ticketID, ticketTitle, ticketDescription, creationTime, customer, supportStaff, 
              priorityLevel, interactionLog, discussionThread);
    }

    @Override
    public String getTicketType() {
        return "Other Ticket";
    }
}
