package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Ticket class implementing Discussable with thread-safe discussion support
 */
public class Ticket implements Discussable, Ticketable {
    private String ticketID;
    private String ticketTitle;
    private String ticketDescription;
    private LocalDateTime creationTime;
    private String status;
    private Customer customer;
    private SupportStaff supportStaff;
    private String priorityLevel;
    private InteractionLog interactionLog;
    private List<Comment> discussionThread;

    public Ticket(String ticketID, String ticketTitle, String ticketDescription, LocalDateTime creationTime,
            Customer customer, SupportStaff supportStaff, String priorityLevel,
            InteractionLog interactionLog, List<Comment> discussionThread) {
        this.ticketID = ticketID;
        this.ticketTitle = ticketTitle;
        this.ticketDescription = ticketDescription;
        this.creationTime = creationTime;
        this.customer = customer;
        this.supportStaff = supportStaff;
        this.priorityLevel = priorityLevel;
        this.interactionLog = interactionLog;
        // Use regular list for discussion thread
        this.discussionThread = discussionThread != null ? discussionThread : new ArrayList<>();
        this.status = "OPEN";
    }

    // Getters
    public String getTicketID() {
        return ticketID;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public String getStatus() {
        return status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public SupportStaff getSupportStaff() {
        return supportStaff;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public InteractionLog getInteractionLog() {
        return interactionLog;
    }

    public List<Comment> getDiscussionThread() {
        return discussionThread;
    }

    // Setters
    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setSupportStaff(SupportStaff supportStaff) {
        this.supportStaff = supportStaff;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public void setInteractionLog(InteractionLog interactionLog) {
        this.interactionLog = interactionLog;
    }

    public void setDiscussionThread(List<Comment> discussionThread) {
        this.discussionThread = discussionThread;
    }

    /**
     * Update priority level and log the change
     */
    public void setPriorityLevelWithLog(String newPriorityLevel, User user) {
        String oldPriority = this.priorityLevel;
        this.priorityLevel = newPriorityLevel;
        // Update InteractionLog with priority change
        if (user != null) {
            this.interactionLog = new InteractionLog(user,
                    "Priority changed from " + oldPriority + " to " + newPriorityLevel);
        }
    }

    public String getTicketType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void addComment(User author, String message) {
        Comment newComment = new Comment(author, message);
        this.discussionThread.add(newComment);
        // Update InteractionLog with comment
        if (this.interactionLog != null) {
            this.interactionLog = new InteractionLog(author, "Comment added: " + message);
        }
        // Save the comment to CSV for persistence
        doa.DiscussionFileLoader.saveCommentToCSV(this.ticketID, author, message);
    }

    @Override
    public String getDiscussion() {
        StringBuilder discussion = new StringBuilder();

        if (this.discussionThread == null || this.discussionThread.isEmpty()) {
            discussion.append("No comments yet.");
        } else {
            for (Comment comment : this.discussionThread) {
                discussion.append(comment.getFormattedComment()).append("\n");
            }
        }

        return discussion.toString();
    }

    @Override
    public String toString() {
        return String.format("Ticket [ID: %s, Title: %s, Status: %s, Priority: %s, Type: %s, Created: %s]",
                ticketID, ticketTitle, status, priorityLevel, getTicketType(), creationTime);
    }
}
