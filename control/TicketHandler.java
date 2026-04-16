package control;

import doa.TicketFileLoader;
import entity.*;
import utility.ConsoleUtil;

/**
 * Common handler for shared ticket operations between Staff and Manager
 */
public class TicketHandler {

    /**
     * Display detailed information about a ticket and show discussion thread
     * Returns true to indicate ticket was viewed successfully
     */
    public static void displayTicketDetails(Ticket ticket, User user) {
        ConsoleUtil.clearScreen();
        System.out.println("Ticket Details:");
        System.out.println("===============");
        System.out.println("ID: " + ticket.getTicketID());
        System.out.println("Title: " + ticket.getTicketTitle());
        System.out.println("Type: " + ticket.getTicketType());
        System.out.println("Description: " + ticket.getTicketDescription());
        System.out.println("Status: " + (ticket.getStatus() != null ? ticket.getStatus() : "Open"));
        System.out.println("Priority: " + ticket.getPriorityLevel());
        System.out.println("Created: " + ticket.getCreationTime());

        // Display user-specific information
        if (user instanceof SupportStaff) {
            String customerName = ticket.getCustomer() != null ? 
                ticket.getCustomer().getFirstName() + " " + ticket.getCustomer().getLastName() : "Unknown";
            System.out.println("Customer: " + customerName);
        } else if (user instanceof Manager) {
            System.out.println("Staff: " + (ticket.getSupportStaff() != null ? 
                ticket.getSupportStaff().getUsername() : "Unassigned"));
        }

        // Display ticket type-specific information
        displayTicketTypeDetails(ticket);

        // Display discussion thread
        System.out.println("\nDiscussion Thread:");
        System.out.println("==================");
        if (ticket.getDiscussionThread() != null && !ticket.getDiscussionThread().isEmpty()) {
            for (Comment comment : ticket.getDiscussionThread()) {
                System.out.println(comment.getFormattedComment());
            }
        } else {
            System.out.println("No comments yet.");
        }
    }

    /**
     * Display ticket type-specific details (Refund, Problem, etc.)
     */
    public static void displayTicketTypeDetails(Ticket ticket) {
        if (ticket instanceof RefundTicket rt) {
            System.out.println("Transaction ID: " + rt.getTransactionID());
            System.out.println("Refund Reason: " + rt.getRefundReason());
            System.out.println("Refund Amount: $" + rt.getRefundAmount());
        } else if (ticket instanceof ProblemTicket pt) {
            System.out.println("Severity Level: " + pt.getSeverityLevel());
        } else if (ticket instanceof ChangeRequestTicket cr) {
            System.out.println("Movie Ticket ID: " + cr.getMovieTicketID());
        } else if (ticket instanceof TechnicalDifficultyTicket td) {
            System.out.println("Device Type: " + td.getDeviceType());
        }
    }

    /**
     * Add a comment to a ticket with user input
     */
    public static void addCommentToTicket(Ticket ticket, User user, java.util.Scanner input) {
        System.out.println("\nAdd a comment (or press ENTER to skip): ");
        String commentText = input.nextLine();
        if (!commentText.trim().isEmpty()) {
            ticket.addComment(user, commentText);
            System.out.println("Comment added successfully. Press [ENTER] to continue...");

            input.nextLine();
        }
    }

    /**
     * Update ticket status to CLOSED
     */
    public static boolean closeTicket(Ticket ticket, User user) {
        ticket.setStatus("CLOSED");
        boolean success = TicketFileLoader.updateTicketInCSV(ticket);
        if (success) {
            String userType = user instanceof Manager ? "manager" : "support staff";
            ticket.addComment(user, "Ticket closed by " + userType + ".");
            return true;
        }
        return false;
    }

    /**
     * Update ticket status to a new status
     */
    public static boolean updateTicketStatus(Ticket ticket, String newStatus, User user) {
        ticket.setStatus(newStatus);
        boolean success = TicketFileLoader.updateTicketInCSV(ticket);
        if (success) {
            ticket.addComment(user, "Ticket status changed to: " + newStatus);
            return true;
        }
        return false;
    }
}
