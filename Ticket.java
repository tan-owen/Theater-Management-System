import java.time.LocalDateTime;

public class Ticket {
    private String ticketID;
    private String description;
    private LocalDateTime dateTimeSubmitted;


    public Ticket(String ticketID, String description, LocalDateTime dateSubmitted) {
        this.ticketID = ticketID;
        this.description = description;
        this.dateTimeSubmitted = dateSubmitted;
    }

}
