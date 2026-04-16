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
        TicketHandler.displayTicketDetails(selectedTicket, manager);
        TicketHandler.addCommentToTicket(selectedTicket, manager, input);
        selectTicketAction(selectedTicket, manager, input);
    }

    private static void selectTicketAction(Ticket ticket, Manager manager, Scanner input) {
        System.out.println("\n=== Manager Actions ===");
        System.out.println("1. Change Ticket Status");
        System.out.println("2. Return to ticket list");
        System.out.print("Enter your choice: ");

        String choice = input.nextLine();
        if (choice.equals("1")) {
            changeTicketStatus(ticket, manager, input);
        }
    }

    private static void changeTicketStatus(Ticket ticket, Manager manager, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Change Ticket Status ===");
        System.out.println("Current Status: " + (ticket.getStatus() != null ? ticket.getStatus() : "Open"));
        System.out.println("\nSelect New Status:");
        System.out.println("1. OPEN");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. CLOSED");
        System.out.println("4. ON_HOLD");
        System.out.println("0. Cancel");
        System.out.print("Enter your choice: ");

        String choice = input.nextLine();
        String newStatus;
        
        switch (choice) {
            case "1" -> newStatus = "OPEN";
            case "2" -> newStatus = "IN_PROGRESS";
            case "3" -> newStatus = "CLOSED";
            case "4" -> newStatus = "ON_HOLD";
            case "0" -> {
                System.out.println("Status change cancelled.");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
                return;
            }
            default -> {
                System.out.println("Invalid choice. Please try again.");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
                return;
            }
        }

        // Confirm the change
        System.out.println("\nAre you sure you want to change the status to " + newStatus + "?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("Enter your choice: ");

        if (input.nextLine().equals("1")) {
            boolean success = TicketHandler.updateTicketStatus(ticket, newStatus, manager);
            if (success) {
                System.out.println("Ticket status updated successfully to: " + newStatus);
            } else {
                System.out.println("Error updating ticket status. Please try again.");
            }
        } else {
            System.out.println("Status change cancelled.");
        }
        System.out.println("Press [ENTER] to return...");
        input.nextLine();
    }
}
