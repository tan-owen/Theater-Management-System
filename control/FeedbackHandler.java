package control;

import doa.FeedbackFileLoader;
import entity.Customer;
import entity.Feedback;
import entity.Ticket;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for customer feedback and ratings on closed tickets.
 * Supports: submitting feedback, viewing all feedback (manager), viewing
 * per-ticket feedback.
 */
public class FeedbackHandler {

    /**
     * Prompts the customer to leave feedback for a closed ticket.
     * Does nothing if the ticket is not closed or feedback already exists.
     */
    public static void promptFeedback(Ticket ticket, Customer customer, Scanner input) {
        if (!"CLOSED".equalsIgnoreCase(ticket.getStatus()))
            return;
        if (FeedbackFileLoader.hasFeedback(ticket.getTicketID())) {
            System.out.println("(Feedback already submitted for this ticket.)");
            return;
        }

        ConsoleUtil.clearScreen();
        System.out.println("=== Feedback & Rating ===");
        System.out.println("Ticket [" + ticket.getTicketID() + "] has been closed.");
        System.out.println("Please rate your support experience (1 = Poor, 5 = Excellent):");
        System.out.println("  1 - Poor");
        System.out.println("  2 - Fair");
        System.out.println("  3 - Good");
        System.out.println("  4 - Very Good");
        System.out.println("  5 - Excellent");
        System.out.print("Enter rating (0 to skip): ");

        int rating = 0;
        try {
            rating = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException ignored) {
        }

        if (rating == 0) {
            System.out.println("Feedback skipped.");
            System.out.println("Press [ENTER] to continue...");
            input.nextLine();
            return;
        }

        if (rating < 1 || rating > 5) {
            System.out.println("Invalid rating. Feedback skipped.");
            System.out.println("Press [ENTER] to continue...");
            input.nextLine();
            return;
        }

        System.out.print("Enter a comment (or press ENTER to skip): ");
        String comment = input.nextLine().trim();
        if (comment.isEmpty())
            comment = "(No comment)";

        Feedback feedback = new Feedback(ticket.getTicketID(), customer.getUserID(), rating, comment);
        boolean saved = FeedbackFileLoader.saveFeedback(feedback);
        if (saved) {
            System.out.println("Thank you for your feedback! " + feedback.getStarDisplay());
        } else {
            System.out.println("Feedback could not be saved.");
        }
        System.out.println("Press [ENTER] to continue...");
        input.nextLine();
    }

    /**
     * Displays all feedback entries with a summary average rating.
     * Intended for manager use.
     */
    public static void viewAllFeedback(Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Feedback & Ratings Report ===");

        List<Feedback> feedbacks = FeedbackFileLoader.loadAll();
        if (feedbacks.isEmpty()) {
            System.out.println("No feedback has been submitted yet.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
            return;
        }

        double totalRating = 0;
        System.out.printf("%-12s %-10s %-8s %s%n", "Ticket ID", "Customer", "Rating", "Comment");
        System.out.println("----------------------------------------------------------");
        for (Feedback f : feedbacks) {
            System.out.printf("%-12s %-10s %-8s %s%n",
                    f.getTicketID(),
                    f.getCustomerID(),
                    f.getStarDisplay(),
                    f.getComment());
            totalRating += f.getRating();
        }

        double avgRating = totalRating / feedbacks.size();
        System.out.println("----------------------------------------------------------");
        System.out.printf("Total Feedback Entries : %d%n", feedbacks.size());
        System.out.printf("Average Rating         : %.2f / 5.00%n", avgRating);

        System.out.println("\nPress [ENTER] to return...");
        input.nextLine();
    }

    /**
     * Prints all feedback for a specific ticket inline (no prompt/wait).
     * Used when displaying ticket details to the customer.
     */
    public static void viewFeedbackForTicket(String ticketID, Scanner input) {
        List<Feedback> feedbacks = FeedbackFileLoader.loadForTicket(ticketID);
        System.out.println("\n--- Customer Feedback ---");
        if (feedbacks.isEmpty()) {
            System.out.println("No feedback submitted for this ticket.");
        } else {
            for (Feedback f : feedbacks) {
                System.out.println(f.toString());
            }
        }
    }
}
