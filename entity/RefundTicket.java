package entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RefundTicket - Ticket subclass for refund requests
 */
public class RefundTicket extends Ticket {
    private String transactionID;
    private double refundAmount;

    public RefundTicket(String ticketID, String ticketTitle, String ticketDescription, 
                        LocalDateTime creationTime, Customer customer, SupportStaff supportStaff, 
                        String priorityLevel, InteractionLog interactionLog, 
                        List<Comment> discussionThread, String transactionID, 
                        double refundAmount) {
        super(ticketID, ticketTitle, ticketDescription, creationTime, customer, supportStaff, 
              priorityLevel, interactionLog, discussionThread);
        this.transactionID = transactionID;
        this.refundAmount = refundAmount;
    }

    // Getters and Setters
    public String getTransactionID() { return this.transactionID; }
    public double getRefundAmount() { return this.refundAmount; }
    
    public void setTransactionID(String transactionID) { this.transactionID = transactionID; }
    public void setRefundAmount(double refundAmount) { this.refundAmount = refundAmount; }

    @Override
    public String getTicketType() {
        return "Refund Ticket";
    }

    @Override
    public String toString() {
        return String.format("RefundTicket [ID: %s, Title: %s, Amount: RM %.2f, Status: %s]",
            getTicketID(), getTicketTitle(), refundAmount, getStatus());
    }
}
