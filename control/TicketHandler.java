package control;

import doa.DiscussionFileLoader;
import doa.TicketFileLoader;
import entity.ChangeRequestTicket;
import entity.Comment;
import entity.InteractionLog;
import entity.Manager;
import entity.ProblemTicket;
import entity.RefundTicket;
import entity.SupportStaff;
import entity.TechnicalDifficultyTicket;
import entity.Ticket;
import entity.User;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Shared control handler for common ticket operations used by both staff and
 * managers. Provides: displaying full ticket details, adding comments, updating
 * status, and closing tickets.
 */
public class TicketHandler {

    /**
     * Clears the screen and prints all ticket details including the discussion
     * thread. Shows customer name for staff and assigned-staff name for managers.
     */
    public static void displayTicketDetails(Ticket ticket, User user) {
        ConsoleUtil.clearScreen();
        System.out.println("Ticket Details:");
        System.out.println("===============");
        System.out.println("ID          : " + ticket.getTicketID());
        System.out.println("Title       : " + ticket.getTicketTitle());
        System.out.println("Type        : " + ticket.getTicketType());
        System.out.println("Description : " + ticket.getTicketDescription());
        System.out.println("Status      : " + (ticket.getStatus() != null ? ticket.getStatus() : "Open"));
        System.out.println("Priority    : " + ticket.getPriorityLevel());
        System.out.println("Created     : " + ticket.getCreationTime());

        // Role-specific contextual field
        if (user instanceof SupportStaff) {
            String customerName = ticket.getCustomer() != null
                    ? ticket.getCustomer().getFirstName() + " " + ticket.getCustomer().getLastName()
                    : "Unknown";
            System.out.println("Customer    : " + customerName);
        } else if (user instanceof Manager) {
            System.out.println("Staff       : "
                    + (ticket.getSupportStaff() != null ? ticket.getSupportStaff().getUsername() : "Unassigned"));
        }

        // Type-specific extra fields
        displayTicketTypeDetails(ticket);

        // Full discussion thread
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
     * Prints ticket-type-specific fields (Refund amount, Resolution steps,
     * Movie ticket ID, or Device type).
     */
    public static void displayTicketTypeDetails(Ticket ticket) {
        if (ticket instanceof RefundTicket rt) {
            System.out.println("Transaction ID  : " + rt.getTransactionID());
            System.out.println("Refund Amount   : RM" + String.format("%.2f", rt.getRefundAmount()));
        } else if (ticket instanceof ProblemTicket pt) {
            System.out.println("Resolution Steps: " + pt.getResolutionSteps());
        } else if (ticket instanceof ChangeRequestTicket cr) {
            System.out.println("Movie Ticket ID : " + cr.getMovieTicketID());
        } else if (ticket instanceof TechnicalDifficultyTicket td) {
            System.out.println("Device Type     : " + td.getDeviceType());
        }
    }

    /**
     * Prompts the user to enter a comment and appends it to the ticket's
     * discussion thread if the input is non-empty.
     */
    public static void addCommentToTicket(Ticket ticket, User user, Scanner input) {
        System.out.println("\nAdd a comment (or press ENTER to skip): ");
        String commentText = input.nextLine();
        if (!commentText.trim().isEmpty()) {
            ticket.addComment(user, commentText);
            System.out.println("Comment added successfully. Press [ENTER] to continue...");
            input.nextLine();
        }
    }

    /**
     * Sets the ticket status to CLOSED, records an interaction log entry,
     * and persists the change.
     */
    public static boolean closeTicket(Ticket ticket, User user) {
        ticket.setStatus("CLOSED");
        String userType = user instanceof Manager ? "manager" : "support staff";
        InteractionLog closeLog = new InteractionLog(user, "Ticket closed by " + userType + ".");
        ticket.setInteractionLog(closeLog);
        return TicketFileLoader.updateTicketInCSV(ticket);
    }

    /**
     * Changes the ticket's status, logs the transition, and persists the change.
     * Also appends an auto-comment to the discussion thread.
     */
    public static boolean updateTicketStatus(Ticket ticket, String newStatus, User user) {
        String oldStatus = ticket.getStatus();
        ticket.setStatus(newStatus);
        InteractionLog statusLog = new InteractionLog(
                user, "Status changed from " + oldStatus + " to " + newStatus);
        ticket.setInteractionLog(statusLog);
        boolean success = TicketFileLoader.updateTicketInCSV(ticket);
        if (success) {
            DiscussionFileLoader.saveInteractionLogToCSV(ticket.getTicketID(), statusLog);
            ticket.addComment(user, "Ticket status changed to: " + newStatus);
        }
        return success;
    }
}
