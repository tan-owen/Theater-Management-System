package control;

import doa.TicketFileLoader;
import entity.Comment;
import entity.Customer;
import entity.Ticket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for customer ticket discussion threads.
 * Allows customers to view open tickets and post comments.
 */
public class DiscussionHandler {

    public static void viewTicketDiscussion(Customer customer, Scanner input) {
        ConsoleUtil.clearScreen();
        List<Ticket> allDiscussionTickets = TicketFileLoader.loadTickets();
        List<Ticket> openTickets = new ArrayList<>();

        // Only show tickets that are not yet closed
        for (Ticket t : allDiscussionTickets) {
            if (t.getStatus() == null || !t.getStatus().equalsIgnoreCase("CLOSED")) {
                openTickets.add(t);
            }
        }

        if (openTickets.isEmpty()) {
            System.out.println("No ticket available for discussion.");
        } else {
            System.out.println("Tickets for Discussion:");
            System.out.println("============================");
            for (int i = 0; i < openTickets.size(); i++) {
                Ticket t = openTickets.get(i);
                String customerName = t.getCustomer() != null
                        ? t.getCustomer().getFirstName() + " " + t.getCustomer().getLastName()
                        : "Unknown";
                System.out.printf("%d. [%s] %s - %s (%s)%n",
                        i + 1, t.getTicketID(), t.getTicketTitle(), customerName, t.getTicketType());
            }

            System.out.print("\nEnter ticket number to join discussion (0 to go back): ");
            try {
                int ticketChoice = Integer.parseInt(input.nextLine());
                if (ticketChoice > 0 && ticketChoice <= openTickets.size()) {
                    Ticket selectedTicket = openTickets.get(ticketChoice - 1);
                    joinDiscussion(selectedTicket, customer, input);
                    System.out.println("Press [ENTER] to return to main menu...");
                    input.nextLine();
                }
            } catch (NumberFormatException e) {
                // Invalid input — silently return to caller
            }
        }
    }

    private static void joinDiscussion(Ticket selectedTicket, Customer customer, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("Discussion for Ticket: " + selectedTicket.getTicketID());
        System.out.println("Title: " + selectedTicket.getTicketTitle());
        System.out.println("Description: " + selectedTicket.getTicketDescription());
        System.out.println("========================================");

        if (selectedTicket.getDiscussionThread() != null && !selectedTicket.getDiscussionThread().isEmpty()) {
            System.out.println("Discussion History:");
            for (Comment comment : selectedTicket.getDiscussionThread()) {
                System.out.println(comment.getFormattedComment());
            }
        } else {
            System.out.println("No comments yet.");
        }
        System.out.println("========================================");

        System.out.println("Enter your comment (or press ENTER to go back): ");
        String commentText = input.nextLine();
        if (!commentText.trim().isEmpty()) {
            selectedTicket.addComment(customer, commentText);
            Comment newComment = new Comment(customer, commentText);
            System.out.println("Comment added: " + newComment.getFormattedComment());
        }
    }
}
