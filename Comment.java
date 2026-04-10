import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {
    private User author;          // Could be Alice (Customer) or Bob (Staff)
    private LocalDateTime timestamp;
    private String content;

    public Comment(User author, String content) {
        this.author = author;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getFormattedComment() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, HH:mm");
        // Example: [Oct 27, 14:30] Alice: "I still haven't received my refund."
        return String.format("[%s] %s: \"%s\"", 
                timestamp.format(fmt), author.getUsername(), content);
    }

    // Getters
    public User getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
}   