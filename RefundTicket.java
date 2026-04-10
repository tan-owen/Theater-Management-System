import java.time.LocalDateTime;
import java.util.List;
//ID PREFIX RF
public class RefundTicket extends Ticket { // implement dicussable
    private String transactionID;
    private String refundReason;
    private double refundAmount;

    public RefundTicket(String ticketID, String ticketTitle, String ticketDescription, LocalDateTime creationTime, Customer customer, SupportStaff supportStaff, String priorityLevel, InteractionLog interactionLog, List<Comment> discussionThread, String transactionID, String refundReason, double refundAmount) {
        super(ticketID, ticketTitle, ticketDescription, creationTime, customer, supportStaff, priorityLevel, interactionLog, discussionThread);
        this.transactionID = transactionID;
        this.refundReason = refundReason;
        this.refundAmount = refundAmount;
    }

    // Getters and Setters
    public String getTransactionID() { return this.transactionID; }
    public String getRefundReason() { return this.refundReason; }
    public double getRefundAmount() { return this.refundAmount; }

    @Override
    public String getTicketType() {
        return "Refund Ticket";
    }
}
