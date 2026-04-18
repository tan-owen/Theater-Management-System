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

    public Comment(User author, String content) {
        this.author = author;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public Comment(User author, String content, LocalDateTime timestamp) {
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    private String getUserTypeFromClass() {
        String className = author.getClass().getSimpleName();
        return switch (className) {
            case "Manager" -> "Manager";
            case "SupportStaff" -> "Support Staff";
            case "Customer" -> "Customer";
            default -> "User";
        };
    }

    private String getDerivedUserType() {
        return getUserTypeFromClass();
    }

    public String getFormattedComment() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, HH:mm");
        return String.format("[%s] %s (%s): \"%s\"",
                timestamp.format(fmt), author.getUsername(), getDerivedUserType(), content);
    }

    // Getters
    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUserType() {
        return getDerivedUserType();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setAuthor(User author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Comment [Author: %s (%s), Content: %s, Timestamp: %s]",
                author.getUsername(), getDerivedUserType(), content, timestamp.format(fmt));
    }
}
