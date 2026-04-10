import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//ID PREFIX TK
public class Ticket {
    private String ticketID;
    private String ticketTitle;
    private String ticketDescription;
    private LocalDateTime creationTime;
    private String status; 
    private Customer customer;
    private SupportStaff supportStaff;
    private String priorityLevel;
    private InteractionLog interactionLog;  
    private List<Comment> discussionThread;


    public Ticket(String ticketID, String ticketTitle, String ticketDescription, LocalDateTime creationTime, Customer customer, SupportStaff supportStaff, String priorityLevel, InteractionLog interactionLog, List<Comment> discussionThread) {
        this.ticketID = ticketID;
        this.ticketTitle = ticketTitle;
        this.ticketDescription = ticketDescription;
        this.creationTime = creationTime;
        this.customer = customer;
        this.supportStaff = supportStaff;
        this.priorityLevel = priorityLevel;
        this.interactionLog = interactionLog;
        this.discussionThread = new ArrayList<>();

    }

    // Getters and Setters
    public String getTicketID() { return ticketID; }
    public String getTicketTitle() { return ticketTitle; }
    public String getTicketDescription() { return ticketDescription; }
    public LocalDateTime getCreationTime() { return creationTime; }
    public String getStatus() { return status; }
    public Customer getCustomer() { return customer; }
    public SupportStaff getSupportStaff() { return supportStaff; }
    public String getPriorityLevel() { return priorityLevel; }
    public InteractionLog getInteractionLog() { return interactionLog; }
    public List<Comment> getDiscussionThread() { return discussionThread; }

    public String getTicketType(){
        return this.getClass().getSimpleName();
    };

}
