package entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TechnicalDifficultyTicket - Ticket subclass for technical issues
 */
public class TechnicalDifficultyTicket extends Ticket {
    private String deviceType;

    public TechnicalDifficultyTicket(String ticketID, String ticketTitle, String ticketDescription, 
                                      LocalDateTime creationTime, Customer customer, SupportStaff supportStaff, 
                                      String priorityLevel, InteractionLog interactionLog, 
                                      List<Comment> discussionThread, String deviceType) {
        super(ticketID, ticketTitle, ticketDescription, creationTime, customer, supportStaff, 
              priorityLevel, interactionLog, discussionThread);
        this.deviceType = deviceType;
    }
    
    // Getters and Setters
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    @Override
    public String getTicketType() {
        return "Incident Ticket";
    }
}
