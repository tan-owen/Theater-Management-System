package control;

import doa.TicketFileLoader;
import entity.*;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for manager to view all tickets
 */
public class ManagerTicketViewHandler {

    public static void viewAllTickets(Manager manager, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== View All Tickets ===");
        List<Ticket> tickets = TicketFileLoader.loadTickets();

        if (tickets.isEmpty()) {
            System.out.println("No tickets in the system.");
        } else {
            System.out.println("All Tickets:");
            System.out.println("============");
            for (int i = 0; i < tickets.size(); i++) {
                Ticket t = tickets.get(i);
                String staffName = t.getSupportStaff() != null ? t.getSupportStaff().getUsername() : "Unassigned";
                System.out.printf("%d. [%s] %s - %s (Priority: %s, Staff: %s)%n", 
                    i + 1, t.getTicketID(), t.getTicketTitle(), t.getTicketType(), t.getPriorityLevel(), staffName);
            }

            System.out.println("\nEnter ticket number to view details (0 to go back): ");
            try {
                int ticketChoice = Integer.parseInt(input.nextLine());
                if (ticketChoice > 0 && ticketChoice <= tickets.size()) {
                    Ticket selectedTicket = tickets.get(ticketChoice - 1);
                    viewTicketDetails(selectedTicket, manager, input);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
        System.out.println("Press [ENTER] to return...");
        input.nextLine();
    }

    private static void viewTicketDetails(Ticket selectedTicket, Manager manager, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("Ticket Details:");
        System.out.println("===============");
        System.out.println("ID: " + selectedTicket.getTicketID());
        System.out.println("Title: " + selectedTicket.getTicketTitle());
        System.out.println("Type: " + selectedTicket.getTicketType());
        System.out.println("Description: " + selectedTicket.getTicketDescription());
        System.out.println("Status: " + (selectedTicket.getStatus() != null ? selectedTicket.getStatus() : "Open"));
        System.out.println("Priority: " + selectedTicket.getPriorityLevel());
        System.out.println("Created: " + selectedTicket.getCreationTime());
        System.out.println("Staff: " + (selectedTicket.getSupportStaff() != null ? selectedTicket.getSupportStaff().getUsername() : "Unassigned"));

        if (selectedTicket.getDiscussionThread() != null && !selectedTicket.getDiscussionThread().isEmpty()) {
            System.out.println("\nDiscussion Thread:");
            for (Comment comment : selectedTicket.getDiscussionThread()) {
                System.out.println(comment.getFormattedComment());
            }
        }

        System.out.println("\nAdd a comment (or press ENTER to skip): ");
        String commentText = input.nextLine();
        if (!commentText.trim().isEmpty()) {
            selectedTicket.addComment(manager, commentText);
            System.out.println("Comment added successfully.");
        }
    }
}
