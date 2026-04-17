package control;

import doa.DiscussionFileLoader;
import doa.TicketFileLoader;
import entity.Customer;
import entity.InteractionLog;
import entity.Manager;
import entity.ProblemTicket;
import entity.SupportStaff;
import entity.Ticket;
import entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for staff and manager ticket viewing, searching, and actions.
 * Provides:
 * <ul>
 * <li>Viewing a staff member's own assigned tickets</li>
 * <li>Browse/search/filter across all tickets (for managers and staff)</li>
 * <li>Ticket actions: add response, update status, provide resolution steps,
 * close ticket</li>
 * </ul>
 */
public class StaffTicketViewHandler {

    // Entry points

    /**
     * Shows only the tickets currently assigned to {@code staff} and allows
     * them to select one to view details and perform actions.
     */
    public static void viewAssignedTickets(SupportStaff staff, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Your Assigned Tickets ===");

        List<Ticket> allTickets = TicketFileLoader.loadTickets();
        List<Ticket> assignedTickets = new ArrayList<>();
        for (Ticket t : allTickets) {
            if (t.getSupportStaff() != null
                    && t.getSupportStaff().getUserID().equals(staff.getUserID())) {
                assignedTickets.add(t);
            }
        }

        if (assignedTickets.isEmpty()) {
            System.out.println("You have no assigned tickets.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
            return;
        }

        System.out.println("=====================");
        for (int i = 0; i < assignedTickets.size(); i++) {
            Ticket t = assignedTickets.get(i);
            System.out.printf("%d. [%s] %s - %s (%s)%n",
                    i + 1, t.getTicketID(), t.getTicketTitle(), t.getTicketType(),
                    t.getStatus() != null ? t.getStatus() : "OPEN");
        }

        System.out.print("\nEnter ticket number to view details (0 to go back): ");
        try {
            int ticketChoice = Integer.parseInt(input.nextLine());
            if (ticketChoice > 0 && ticketChoice <= assignedTickets.size()) {
                Ticket selectedTicket = assignedTickets.get(ticketChoice - 1);
                TicketHandler.displayTicketDetails(selectedTicket, staff);
                selectTicketAction(selectedTicket, staff, input);
            } else if (ticketChoice != 0) {
                System.out.println("Invalid ticket number. Please try again.");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid ticket number.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
        }
    }

    /**
     * Entry point for managers and staff — browse/search/filter across all tickets.
     */
    public static void viewAllTickets(User user, Scanner input) {
        viewAllTicketsInternal(user, input);
    }

    /**
     * Customer entry point — same browse/filter/search flow, but limited to
     * comment-only actions.
     */
    public static void viewAllTickets(Customer customer, Scanner input) {
        viewAllTicketsInternal(customer, input);
    }

    // Browse / filter / search

    private static void viewAllTicketsInternal(User user, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Browse / Search Tickets ===");
        System.out.println("1. Filter by ticket type");
        System.out.println("2. Filter by status");
        System.out.println("3. Search by keyword (title / description)");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");
        String choice = input.nextLine().trim();

        switch (choice) {
            case "1" -> filterByType(user, input);
            case "2" -> filterByStatus(user, input);
            case "3" -> searchByKeyword(user, input);
            case "0" -> {
                /* return to caller */ }
            default -> {
                System.out.println("Invalid choice.");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
            }
        }
    }

    private static void filterByType(User user, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("Select ticket type to view:");
        System.out.println("1. All Tickets");
        System.out.println("2. Refund Tickets");
        System.out.println("3. Technical Difficulty Tickets");
        System.out.println("4. Change Request Tickets");
        System.out.println("5. Problem Tickets");
        System.out.println("6. Other Tickets");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");
        String typeChoice = input.nextLine();

        String ticketType;
        switch (typeChoice) {
            case "1" -> ticketType = "";
            case "2" -> ticketType = "RefundTicket";
            case "3" -> ticketType = "TechnicalDifficultyTicket";
            case "4" -> ticketType = "ChangeRequestTicket";
            case "5" -> ticketType = "ProblemTicket";
            case "6" -> ticketType = "OtherTicket";
            case "0" -> {
                return;
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        List<Ticket> filtered = new ArrayList<>();
        for (Ticket t : TicketFileLoader.loadTickets()) {
            if (ticketType.isEmpty() || t.getClass().getSimpleName().equals(ticketType)) {
                filtered.add(t);
            }
        }
        showTicketListAndSelect(filtered, "Type filter: " + (ticketType.isEmpty() ? "All" : ticketType), user, input);
    }

    private static void filterByStatus(User user, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("Select status to filter by:");
        System.out.println("1. OPEN");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. ON_HOLD");
        System.out.println("4. CLOSED");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");

        String statusFilter;
        switch (input.nextLine().trim()) {
            case "1" -> statusFilter = "OPEN";
            case "2" -> statusFilter = "IN_PROGRESS";
            case "3" -> statusFilter = "ON_HOLD";
            case "4" -> statusFilter = "CLOSED";
            case "0" -> {
                return;
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        List<Ticket> filtered = new ArrayList<>();
        for (Ticket t : TicketFileLoader.loadTickets()) {
            if (statusFilter.equalsIgnoreCase(t.getStatus())) {
                filtered.add(t);
            }
        }
        showTicketListAndSelect(filtered, "Status: " + statusFilter, user, input);
    }

    private static void searchByKeyword(User user, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.print("Enter search keyword (title or description): ");
        String keyword = input.nextLine().trim().toLowerCase();
        if (keyword.isEmpty()) {
            System.out.println("No keyword entered.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
            return;
        }

        List<Ticket> results = new ArrayList<>();
        for (Ticket t : TicketFileLoader.loadTickets()) {
            boolean titleMatch = t.getTicketTitle() != null
                    && t.getTicketTitle().toLowerCase().contains(keyword);
            boolean descMatch = t.getTicketDescription() != null
                    && t.getTicketDescription().toLowerCase().contains(keyword);
            boolean idMatch = t.getTicketID().toLowerCase().contains(keyword);
            if (titleMatch || descMatch || idMatch) {
                results.add(t);
            }
        }
        showTicketListAndSelect(results, "Keyword: \"" + keyword + "\"", user, input);
    }

    // Shared list display + selection

    private static void showTicketListAndSelect(List<Ticket> tickets, String header,
            User user, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== " + header + " ===");
        if (tickets.isEmpty()) {
            System.out.println("No tickets found.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
            return;
        }

        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            String customerName = t.getCustomer() != null
                    ? t.getCustomer().getFirstName() + " " + t.getCustomer().getLastName()
                    : "Unknown";
            System.out.printf("%d. [%s] %s | %s | Customer: %s | Status: %s%n",
                    i + 1, t.getTicketID(), t.getTicketTitle(), t.getTicketType(),
                    customerName, t.getStatus() != null ? t.getStatus() : "OPEN");
        }

        System.out.print("\nEnter ticket number to view details (0 to go back): ");
        try {
            int ticketChoice = Integer.parseInt(input.nextLine());
            if (ticketChoice > 0 && ticketChoice <= tickets.size()) {
                Ticket selected = tickets.get(ticketChoice - 1);
                TicketHandler.displayTicketDetails(selected, user);
                selectTicketAction(selected, user, input);
            } else if (ticketChoice != 0) {
                System.out.println("Invalid ticket number.");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
        }
    }

    // Ticket actions

    private static void selectTicketAction(Ticket ticket, User user, Scanner input) {

        // Customers can only comment — no status changes or closures
        if (user instanceof Customer) {
            System.out.println("\n=== Actions ===");
            System.out.println("1. Add Comment");
            System.out.println("0. Back to Ticket List");
            System.out.print("Enter your choice: ");
            String choice = input.nextLine();
            if (choice.equals("1")) {
                TicketHandler.addCommentToTicket(ticket, user, input);
            }
            return;
        }

        // ProblemTickets assigned to support staff get an extra "Resolution Steps"
        // option
        boolean isProblem = ticket instanceof ProblemTicket && user instanceof SupportStaff;

        System.out.println("\n=== Actions ===");
        System.out.println("1. Add Response (Discussion)");
        System.out.println("2. Update Ticket Status");
        if (isProblem) {
            System.out.println("3. Provide Resolution Steps");
            System.out.println("4. Close Ticket");
        } else {
            System.out.println("3. Close Ticket");
        }
        System.out.println("0. Back to Ticket List");
        System.out.print("Enter your choice: ");

        String choice = input.nextLine();

        if (isProblem) {
            switch (choice) {
                case "0" -> {
                    /* return */ }
                case "1" -> TicketHandler.addCommentToTicket(ticket, user, input);
                case "2" -> updateTicketStatus(ticket, user, input);
                case "3" -> provideResolutionSteps((ProblemTicket) ticket, user, input);
                case "4" -> closeTicket(ticket, user, input);
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press [ENTER] to return...");
                    input.nextLine();
                }
            }
        } else {
            switch (choice) {
                case "0" -> {
                    /* return */ }
                case "1" -> TicketHandler.addCommentToTicket(ticket, user, input);
                case "2" -> updateTicketStatus(ticket, user, input);
                case "3" -> closeTicket(ticket, user, input);
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press [ENTER] to return...");
                    input.nextLine();
                }
            }
        }
    }

    private static void updateTicketStatus(Ticket ticket, User user, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Update Ticket Status ===");
        System.out.println("Ticket : [" + ticket.getTicketID() + "] " + ticket.getTicketTitle());
        System.out.println("Current Status: " + (ticket.getStatus() != null ? ticket.getStatus() : "OPEN"));
        System.out.println("\nSelect New Status:");
        System.out.println("1. OPEN");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. ON_HOLD");
        System.out.println("4. CLOSED");
        System.out.println("0. Cancel");
        System.out.print("Enter your choice: ");

        String newStatus;
        switch (input.nextLine().trim()) {
            case "1" -> newStatus = "OPEN";
            case "2" -> newStatus = "IN_PROGRESS";
            case "3" -> newStatus = "ON_HOLD";
            case "4" -> newStatus = "CLOSED";
            case "0" -> {
                System.out.println("Status change cancelled.");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
                return;
            }
            default -> {
                System.out.println("Invalid choice.");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
                return;
            }
        }

        boolean success = TicketHandler.updateTicketStatus(ticket, newStatus, user);
        if (success) {
            System.out.println("Ticket status updated to: " + newStatus);
        } else {
            System.out.println("Error updating ticket status. Please try again.");
        }
        System.out.println("Press [ENTER] to return...");
        input.nextLine();
    }

    private static void provideResolutionSteps(ProblemTicket pt, User user, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Provide Resolution Steps ===");
        System.out.println("Ticket: [" + pt.getTicketID() + "] " + pt.getTicketTitle());

        String currentSteps = pt.getResolutionSteps();
        if (currentSteps != null && !currentSteps.isBlank()
                && !currentSteps.equalsIgnoreCase("null")
                && !currentSteps.equalsIgnoreCase("UNDETERMINED")) {
            System.out.println("Current resolution steps: " + currentSteps);
        } else {
            System.out.println("No resolution steps have been provided yet.");
        }

        System.out.print("\nEnter resolution steps (or press ENTER to cancel): ");
        String resolutionInput = input.nextLine().trim();

        if (resolutionInput.isEmpty()) {
            System.out.println("No changes made.");
        } else {
            pt.setResolutionSteps(resolutionInput);

            boolean saved = TicketFileLoader.updateTicketInCSV(pt);
            if (saved) {
                // Sanitise for CSV: strip tabs and newlines
                String safeSteps = resolutionInput.replaceAll("\t", " ").replaceAll("[\\r\\n]+", " ");
                InteractionLog resolutionLog = new InteractionLog(user, "Resolution steps provided: " + safeSteps);
                DiscussionFileLoader.saveInteractionLogToCSV(pt.getTicketID(), resolutionLog);
                System.out.println("Resolution steps saved successfully!");
            } else {
                System.out.println("Error saving resolution steps. Please try again.");
            }
        }
        System.out.println("Press [ENTER] to return...");
        input.nextLine();
    }

    private static void closeTicket(Ticket ticket, User user, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Close Ticket ===");
        System.out.println("Ticket : [" + ticket.getTicketID() + "] " + ticket.getTicketTitle());
        System.out.println("Are you sure you want to close this ticket?");
        System.out.println("1. Yes, close ticket");
        System.out.println("2. No, cancel");
        System.out.print("Enter your choice: ");

        if (input.nextLine().equals("1")) {
            boolean success = TicketHandler.closeTicket(ticket, user);
            if (success) {
                String closedBy = user instanceof Manager ? "manager" : "support staff";
                InteractionLog closeLog = new InteractionLog(user, "Ticket closed by " + closedBy + ".");
                DiscussionFileLoader.saveInteractionLogToCSV(ticket.getTicketID(), closeLog);
                System.out.println("Ticket closed successfully.");
            } else {
                System.out.println("Error closing ticket. Please try again.");
            }
        } else {
            System.out.println("Closure cancelled.");
        }
        System.out.println("Press [ENTER] to return...");
        input.nextLine();
    }
}
