import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {
    private User author;          // Could be Customer, SupportStaff, or Manager
    private LocalDateTime timestamp;
    private String content;
    private String userType;      // "Customer", "Support Staff", or "Manager"

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
        // Example: [Oct 27, 14:30] Alice (Customer): "I still haven't received my refund."
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
}