package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * InteractionLog class for recording user interactions
 */
public class InteractionLog {
    private LocalDateTime timestamp;
    private User user;
    private String actionDetail;

    // Constructor
    public InteractionLog(User actor, String actionDetail) {
        this.timestamp = LocalDateTime.now();
        this.user = actor;
        this.actionDetail = actionDetail;
    }

    public InteractionLog(LocalDateTime timestamp, User user, String actionDetail) {
        this.timestamp = timestamp;
        this.user = user;
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

    // Setters
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setUser(User user) { this.user = user; }
    public void setActionDetail(String actionDetail) { this.actionDetail = actionDetail; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("InteractionLog [User: %s, Action: %s, Timestamp: %s]",
            user.getUsername(), actionDetail, timestamp.format(formatter));
    }
}
