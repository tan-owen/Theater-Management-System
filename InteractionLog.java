import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InteractionLog {
    private LocalDateTime timestamp;
    private User user;         
    private String actionDetail;

    // Constructor
    public InteractionLog(User actor, String actionDetail) {
        this.timestamp = LocalDateTime.now(); // Automatically grabs the exact current time
        this.user = actor;
        this.actionDetail = actionDetail;
    }


    public String getFormattedLog() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeString = timestamp.format(formatter);
        

        return String.format("[%s] %s: %s", timeString, user.getUsername(), actionDetail);
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public User getUser() { return user; }
    public String getActionDetail() { return actionDetail; }
}