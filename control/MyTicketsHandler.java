package control;

import doa.DiscussionFileLoader;
import doa.FeedbackFileLoader;
import doa.TicketFileLoader;
import entity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

public class MyTicketsHandler {

    public static void viewMyTickets(Customer customer, Scanner input) {
        ConsoleUtil.clearScreen();
        List<Ticket> allTickets = TicketFileLoader.loadTickets();
        List<Ticket> myTickets = new ArrayList<>();
        for (Ticket t : allTickets) {
            if (t.getCustomer() != null && t.getCustomer().getUserID().equals(customer.getUserID())) {
                myTickets.add(t);
            }
        }

        if (myTickets.isEmpty()) {
            System.out.println("You have no tickets yet.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
            return;
        }

        System.out.println("Your Tickets:");
        System.out.println("=============");
        for (int i = 0; i < myTickets.size(); i++) {
            Ticket t = myTickets.get(i);
            System.out.printf("%d. [%s] %s - %s (%s)%n",
                    i + 1, t.getTicketID(), t.getTicketTitle(), t.getTicketType(),
                    t.getStatus() != null ? t.getStatus() : "OPEN");
        }

        System.out.print("\nEnter ticket number to view details (0 to go back): ");
        try {
            int ticketChoice = Integer.parseInt(input.nextLine());
            if (ticketChoice > 0 && ticketChoice <= myTickets.size()) {
                Ticket selectedTicket = myTickets.get(ticketChoice - 1);
                viewTicketDetails(selectedTicket, customer, input);
            } else if (ticketChoice != 0) {
                System.out.println("Invalid choice.");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid ticket number.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
        }
    }

    private static void viewTicketDetails(Ticket selectedTicket, Customer customer, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Ticket Details ===");
        System.out.println("ID          : " + selectedTicket.getTicketID());
        System.out.println("Title       : " + selectedTicket.getTicketTitle());
        System.out.println("Type        : " + selectedTicket.getTicketType());
        System.out.println("Description : " + selectedTicket.getTicketDescription());
        System.out
                .println("Status      : " + (selectedTicket.getStatus() != null ? selectedTicket.getStatus() : "OPEN"));
        System.out.println("Priority    : " + selectedTicket.getPriorityLevel());
        System.out.println("Submitted   : " + selectedTicket.getCreationTime());

        String assignedStaff = selectedTicket.getSupportStaff() != null
                ? selectedTicket.getSupportStaff().getFirstName() + " " + selectedTicket.getSupportStaff().getLastName()
                : "Not assigned yet";
        System.out.println("Assigned To : " + assignedStaff);

        // Display ticket type-specific details
        printTypeDetails(selectedTicket);

        // Staff Responses
        System.out.println("\n--- Staff Responses ---");
        boolean hasStaffResponse = false;
        if (selectedTicket.getDiscussionThread() != null) {
            for (Comment c : selectedTicket.getDiscussionThread()) {
                if (c.getAuthor() instanceof SupportStaff || c.getAuthor() instanceof Manager) {
                    System.out.println(c.getFormattedComment());
                    hasStaffResponse = true;
                }
            }
        }
        if (!hasStaffResponse) {
            System.out.println("No staff response yet.");
        }

        // Show existing feedback (if any)
        if ("CLOSED".equalsIgnoreCase(selectedTicket.getStatus())) {
            FeedbackHandler.viewFeedbackForTicket(selectedTicket.getTicketID(), input);
        }

        // Actions available to the customer
        System.out.println();
        boolean isClosed = "CLOSED".equalsIgnoreCase(selectedTicket.getStatus());
        if (!isClosed) {
            System.out.println("1. Add Comment");
            System.out.println("2. Close this ticket (issue is resolved)");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");
            String choice = input.nextLine().trim();
            if (choice.equals("1")) {
                // implement here
            } else if (choice.equals("2")) {
                closeOwnTicket(selectedTicket, customer, input);
            }
        } else {
            // Ticket is already closed – offer feedback if not yet given
            if (!FeedbackFileLoader.hasFeedback(selectedTicket.getTicketID())) {
                System.out.println("1. Submit feedback & rating");
                System.out.println("0. Back");
                System.out.print("Enter your choice: ");
                String choice = input.nextLine().trim();
                if (choice.equals("1")) {
                    FeedbackHandler.promptFeedback(selectedTicket, customer, input);
                }
            } else {
                System.out.println("(Ticket closed - feedback already submitted)");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
            }
        }
    }

    private static void closeOwnTicket(Ticket ticket, Customer customer, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Close Ticket ===");
        System.out.println("Ticket : [" + ticket.getTicketID() + "] " + ticket.getTicketTitle());
        System.out.println("Are you sure you want to mark this ticket as resolved/closed?");
        System.out.println("1. Yes, close this ticket");
        System.out.println("2. No, cancel");
        System.out.print("Enter your choice: ");

        String choice = input.nextLine();
        if (choice.equals("1")) {
            ticket.setStatus("CLOSED");
            InteractionLog closeLog = new InteractionLog(customer, "Ticket closed by customer (issue resolved).");
            ticket.setInteractionLog(closeLog);
            boolean success = TicketFileLoader.updateTicketInCSV(ticket);
            if (success) {
                DiscussionFileLoader.saveInteractionLogToCSV(ticket.getTicketID(), closeLog);
                System.out.println("Ticket closed successfully!");
                // Prompt for feedback right after closure
                FeedbackHandler.promptFeedback(ticket, customer, input);
            } else {
                System.out.println("Error closing ticket. Please try again.");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
            }
        } else {
            System.out.println("Ticket closure cancelled.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
        }
    }

    private static void printTypeDetails(Ticket selectedTicket) {
        if (selectedTicket instanceof RefundTicket rt) {
            System.out.println("Transaction ID : " + rt.getTransactionID());
            System.out.println("Refund Amount  : RM" + String.format("%.2f", rt.getRefundAmount()));
        } else if (selectedTicket instanceof ProblemTicket pt) {
            String steps = pt.getResolutionSteps();
            if (steps != null && !steps.isBlank() && !steps.equalsIgnoreCase("UNDETERMINED")) {
                System.out.println("Resolution Steps : " + steps);
            }
        } else if (selectedTicket instanceof ChangeRequestTicket cr) {
            System.out.println("Movie Ticket ID : " + cr.getMovieTicketID());
        } else if (selectedTicket instanceof TechnicalDifficultyTicket td) {
            System.out.println("Device Type     : " + td.getDeviceType());
        }
    }
}
