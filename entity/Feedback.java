package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Feedback entity for post-ticket-closure ratings and comments from customers.
 */
public class Feedback {
    private String ticketID;
    private String customerID;
    private int rating;          // 1-5 stars
    private String comment;
    private LocalDateTime submittedAt;

    public Feedback(String ticketID, String customerID, int rating, String comment) {
        this.ticketID = ticketID;
        this.customerID = customerID;
        this.rating = Math.max(1, Math.min(5, rating)); // clamp 1-5
        this.comment = comment;
        this.submittedAt = LocalDateTime.now();
    }

    public Feedback(String ticketID, String customerID, int rating, String comment, LocalDateTime submittedAt) {
        this.ticketID = ticketID;
        this.customerID = customerID;
        this.rating = Math.max(1, Math.min(5, rating));
        this.comment = comment;
        this.submittedAt = submittedAt;
    }

    // Getters
    public String getTicketID()    { return ticketID; }
    public String getCustomerID()  { return customerID; }
    public int getRating()         { return rating; }
    public String getComment()     { return comment; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }

    /** Returns a star-string representation, e.g. "★★★☆☆" */
    public String getStarDisplay() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            sb.append(i <= rating ? "\u2605" : "\u2606");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Feedback [Ticket: %s, Rating: %s (%d/5), Comment: %s, Date: %s]",
            ticketID, getStarDisplay(), rating, comment, submittedAt.format(fmt));
    }
}
