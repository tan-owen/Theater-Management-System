package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Comment class representing a discussion comment on a ticket
 */
public class Comment {
    private User author;
    private LocalDateTime timestamp;
    private String content;
    private String userType;

    public Comment(User author, String content) {
        this.author = author;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.userType = getUserTypeFromClass(author);
    }

    public Comment(User author, String content, String userType) {
        this.author = author;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.userType = userType;
    }

    public Comment(User author, String content, LocalDateTime timestamp, String userType) {
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
        this.userType = userType;
    }

    private static String getUserTypeFromClass(User user) {
        String className = user.getClass().getSimpleName();
        switch (className) {
            case "Manager":
                return "Manager";
            case "SupportStaff":
                return "Support Staff";
            case "Customer":
                return "Customer";
            default:
                return "User";
        }
    }

    public String getFormattedComment() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, HH:mm");
        return String.format("[%s] %s (%s): \"%s\"", 
                timestamp.format(fmt), author.getUsername(), userType, content);
    }

    // Getters
    public User getAuthor() { return author; }
    public User getCommenter() { return author; }
    public String getContent() { return content; }
    public String getMessage() { return content; }
    public String getUserType() { return userType; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setters
    public void setAuthor(User author) { this.author = author; }
    public void setContent(String content) { this.content = content; }
    public void setUserType(String userType) { this.userType = userType; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
