import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StaffTicketViewHandler {

    public static void viewAssignedTickets(SupportStaff staff, Scanner input) {
        main.clearScreen();
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
                    i + 1, t.getTicketID(), t.getTicketTitle(), t.getTicketType(), t.getStatus() != null ? t.getStatus() : "Open");
            }

            System.out.println("\nEnter ticket number to view details (0 to go back): ");
            try {
                int ticketChoice = Integer.parseInt(input.nextLine());
                if (ticketChoice > 0 && ticketChoice <= assignedTickets.size()) {
                    Ticket selectedTicket = assignedTickets.get(ticketChoice - 1);
                    viewTicketDetails(selectedTicket, staff, input);
                } else if (ticketChoice != 0) {
                    System.out.println("Invalid ticket number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid ticket number.");
            }
        }
    }

    public static void viewAllTickets(SupportStaff staff, Scanner input) {
        main.clearScreen();
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
        
        String ticketType = "";
        switch (typeChoice) {
            case "1": ticketType = ""; break; // All tickets
            case "2": ticketType = "RefundTicket"; break;
            case "3": ticketType = "TechnicalDifficultyTicket"; break;
            case "4": ticketType = "ChangeRequestTicket"; break;
            case "5": ticketType = "ProblemTicket"; break;
            case "6": ticketType = "OtherTicket"; break;
            case "0": return; // Go back
            default:
                System.out.println("Invalid choice. Please try again.");
                return;
        }

        main.clearScreen();
        System.out.println("=== Viewing Tickets ===");
        List<Ticket> allTickets = TicketFileLoader.loadTickets();
        List<Ticket> filteredTickets = new ArrayList<>();

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
                String customerName = t.getCustomer() != null ? t.getCustomer().getFirstName() + " " + t.getCustomer().getLastName() : "Unknown";
                System.out.printf("%d. [%s] %s - %s (%s)%n", 
                    i + 1, t.getTicketID(), t.getTicketTitle(), customerName, t.getTicketType());
            }

            System.out.println("\nEnter ticket number to view details (0 to go back): ");
            try {
                int ticketChoice = Integer.parseInt(input.nextLine());
                if (ticketChoice > 0 && ticketChoice <= filteredTickets.size()) {
                    Ticket selectedTicket = filteredTickets.get(ticketChoice - 1);
                    viewTicketDetails(selectedTicket, staff, input);
                } else if (ticketChoice != 0) {
                    System.out.println("Invalid ticket number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid ticket number.");
            }
        }
    }

    private static void viewTicketDetails(Ticket ticket, SupportStaff staff, Scanner input) {
        main.clearScreen();
        System.out.println("Ticket Details:");
        System.out.println("===============");
        System.out.println("ID: " + ticket.getTicketID());
        System.out.println("Title: " + ticket.getTicketTitle());
        System.out.println("Type: " + ticket.getTicketType());
        System.out.println("Description: " + ticket.getTicketDescription());
        System.out.println("Status: " + (ticket.getStatus() != null ? ticket.getStatus() : "Open"));
        System.out.println("Priority: " + ticket.getPriorityLevel());
        System.out.println("Created: " + ticket.getCreationTime());
        String customerName = ticket.getCustomer() != null ? ticket.getCustomer().getFirstName() + " " + ticket.getCustomer().getLastName() : "Unknown";
        System.out.println("Customer: " + customerName);

        // Show ticket-specific details
        if (ticket instanceof RefundTicket) {
            RefundTicket rt = (RefundTicket) ticket;
            System.out.println("Transaction ID: " + rt.getTransactionID());
            System.out.println("Refund Reason: " + rt.getRefundReason());
            System.out.println("Refund Amount: $" + rt.getRefundAmount());
        } else if (ticket instanceof ProblemTicket) {
            ProblemTicket pt = (ProblemTicket) ticket;
            System.out.println("Severity Level: " + pt.getSeverityLevel());
        } else if (ticket instanceof ChangeRequestTicket) {
            ChangeRequestTicket cr = (ChangeRequestTicket) ticket;
            System.out.println("Movie Ticket ID: " + cr.getMovieTicketID());
        } else if (ticket instanceof TechnicalDifficultyTicket) {
            TechnicalDifficultyTicket td = (TechnicalDifficultyTicket) ticket;
            System.out.println("Device Type: " + td.getDeviceType());
        }

        System.out.println("\nDiscussion Thread:");
        System.out.println("==================");
        if (ticket.getDiscussionThread() != null && !ticket.getDiscussionThread().isEmpty()) {
            for (Comment comment : ticket.getDiscussionThread()) {
                System.out.println(comment.getFormattedComment());
            }
        } else {
            System.out.println("No comments yet.");
        }

        System.out.println("\nAdd a response (or press ENTER to skip): ");
        String commentText = input.nextLine();
        if (!commentText.trim().isEmpty()) {
            ticket.addComment(staff, commentText);
            Comment newComment = new Comment(staff, commentText);
            System.out.println("Response added: " + newComment.getFormattedComment());
        }
    }
}
