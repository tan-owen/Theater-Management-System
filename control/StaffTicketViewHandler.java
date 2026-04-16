package control;

import doa.DiscussionFileLoader;
import doa.TicketFileLoader;
import entity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for support staff to view assigned tickets
 */
public class StaffTicketViewHandler {

    public static void viewAssignedTickets(SupportStaff staff, Scanner input) {
        ConsoleUtil.clearScreen();

        System.out.println("=== Your Assigned Tickets ===");
        List<Ticket> allTickets = TicketFileLoader.loadTickets();
        List<Ticket> assignedTickets = new ArrayList<>();

        for (Ticket t : allTickets) {
            if (t.getSupportStaff() != null && t.getSupportStaff().getUserID().equals(staff.getUserID())) {
                assignedTickets.add(t);
            }
        }

        if (assignedTickets.isEmpty()) {
            System.out.println("You have no assigned tickets.");
        } else {
            System.out.println("Your Assigned Tickets:");
            System.out.println("=====================");
            for (int i = 0; i < assignedTickets.size(); i++) {
                Ticket t = assignedTickets.get(i);
                System.out.printf("%d. [%s] %s - %s (%s)%n",
                        i + 1, t.getTicketID(), t.getTicketTitle(), t.getTicketType(),
                        t.getStatus() != null ? t.getStatus() : "Open");
            }

            System.out.println("\nEnter ticket number to view details (0 to go back): ");
            try {
                int ticketChoice = Integer.parseInt(input.nextLine());
                if (ticketChoice > 0 && ticketChoice <= assignedTickets.size()) {
                    Ticket selectedTicket = assignedTickets.get(ticketChoice - 1);
                    selectTicketAction(selectedTicket, staff, input);
                } else if (ticketChoice != 0) {
                    System.out.println("Invalid ticket number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid ticket number.");
            }
        }
    }

    public static void viewAllTickets(SupportStaff staff, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("Select ticket type to view:");
        System.out.println("1. All Tickets");
        System.out.println("2. Refund Tickets");
        System.out.println("3. Technical Difficulty Tickets");
        System.out.println("4. Change Request Tickets");
        System.out.println("5. Problem Tickets");
        System.out.println("6. Other Tickets");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        String typeChoice = input.nextLine();

        String ticketType;
        switch (typeChoice) {
            case "1" -> ticketType = "";
            case "2" -> ticketType = "Refund Ticket";
            case "3" -> ticketType = "Technical Difficulty Ticket";
            case "4" -> ticketType = "Change Request Ticket";
            case "5" -> ticketType = "Problem Ticket";
            case "6" -> ticketType = "Other Ticket";
            case "0" -> {
                return;
            }
            default -> {
                System.out.println("Invalid choice. Please try again.");
                return;
            }
        }

        ConsoleUtil.clearScreen();
        List<Ticket> allTickets = TicketFileLoader.loadTickets();
        List<Ticket> filteredTickets = new ArrayList<>();

        System.out.println("=== Viewing " + ticketType + "s ===");
        for (Ticket t : allTickets) {

            if (ticketType.isEmpty() || t.getTicketType().equals(ticketType)) {
                filteredTickets.add(t);
            }
        }

        if (filteredTickets.isEmpty()) {
            System.out.println("No tickets found for this type.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
        } else {
            System.out.println("Available Tickets:");
            System.out.println("==================");
            for (int i = 0; i < filteredTickets.size(); i++) {
                Ticket t = filteredTickets.get(i);
                String customerName = t.getCustomer() != null
                        ? t.getCustomer().getFirstName() + " " + t.getCustomer().getLastName()
                        : "Unknown";
                System.out.printf("%d. [%s] %s - %s (%s)%n",
                        i + 1, t.getTicketID(), t.getTicketTitle(), customerName, t.getTicketType());
            }

            System.out.println("\nEnter ticket number to view details (0 to go back): ");
            try {
                int ticketChoice = Integer.parseInt(input.nextLine());
                if (ticketChoice > 0 && ticketChoice <= filteredTickets.size()) {
                    Ticket selectedTicket = filteredTickets.get(ticketChoice - 1);
                    TicketHandler.displayTicketDetails(selectedTicket, staff);
                    TicketHandler.addCommentToTicket(selectedTicket, staff, input);
                } else if (ticketChoice != 0) {
                    System.out.println("Invalid ticket number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid ticket number.");
            }
        }
    }

    private static void closeTicket(Ticket ticket, SupportStaff staff, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Close Ticket ===");
        System.out.println("Are you sure you want to close ticket: " + ticket.getTicketID() + "?");
        System.out.println("1. Yes, close this ticket");
        System.out.println("2. No, cancel");
        System.out.print("Enter your choice: ");

        String choice = input.nextLine();
        if (choice.equals("1")) {
            boolean success = TicketHandler.closeTicket(ticket, staff);
            if (success) {
                System.out.println("Ticket closed successfully!");
            } else {
                System.out.println("Error closing ticket. Please try again.");
            }
        } else {
            System.out.println("Ticket closing cancelled.");
        }
        System.out.println("Press [ENTER] to return...");
        input.nextLine();
    }

    private static void selectTicketAction(Ticket ticket, SupportStaff staff, Scanner input) {
        if (ticket instanceof ProblemTicket) {
            System.out.println("\n=== Staff Actions ===");
            System.out.println("1. Discussion Thread");
            System.out.println("2. Provide Resolution Steps");
            System.out.println("3. Close Ticket");
            System.out.println("0. Back to Ticket List");
            System.out.print("Enter your choice: ");

            String choice = input.nextLine();
            switch (choice) {
                case "0" -> {
                    return;
                }
                case "1" -> {
                    TicketHandler.displayTicketDetails(ticket, staff);
                    TicketHandler.addCommentToTicket(ticket, staff, input);
                }
                case "2" -> {
                    ProblemTicket pt = (ProblemTicket) ticket;
                    ConsoleUtil.clearScreen();
                    System.out.println("=== Provide Resolution Steps ===");
                    System.out.println("Ticket: [" + pt.getTicketID() + "] " + pt.getTicketTitle());

                    String currentSteps = pt.getResolutionSteps();
                    if (currentSteps != null && !currentSteps.isBlank() && !currentSteps.equalsIgnoreCase("null")) {
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
                            // Log the action in interaction_log.csv
                            String safeSteps = resolutionInput.replaceAll("\t", " ").replaceAll("[\r\n]+", " ");
                            InteractionLog resolutionLog = new InteractionLog(
                                    staff, "Resolution steps provided: " + safeSteps);
                            DiscussionFileLoader.saveInteractionLogToCSV(pt.getTicketID(), resolutionLog);

                            System.out.println("Resolution steps saved successfully!");
                        } else {
                            System.out.println("Error saving resolution steps. Please try again.");
                        }
                    }
                    System.out.println("Press [ENTER] to return...");
                    input.nextLine();
                }
                case "3" -> closeTicket(ticket, staff, input);
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press [ENTER] to return...");
                    input.nextLine();
                }
            }
        } else {
            System.out.println("\n=== Staff Actions ===");
            System.out.println("1. Discussion Thread");
            System.out.println("2. Close Ticket");
            System.out.println("0. Back to Ticket List");
            System.out.print("Enter your choice: ");

            String choice = input.nextLine();
            switch (choice) {
                case "0" -> {
                    return;
                }
                case "1" -> {
                    TicketHandler.displayTicketDetails(ticket, staff);
                    TicketHandler.addCommentToTicket(ticket, staff, input);
                }
                case "2" -> closeTicket(ticket, staff, input);
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press [ENTER] to return...");
                    input.nextLine();
                }
            }

        }

    }

}
