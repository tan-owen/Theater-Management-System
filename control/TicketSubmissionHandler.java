package control;

import doa.TicketFileLoader;
import entity.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for ticket submission
 */
public class TicketSubmissionHandler {

    private static String generateTicketID(String prefix) {
        List<Ticket> tickets = TicketFileLoader.loadTickets();
        long count = tickets.stream()
                .filter(t -> t.getTicketID().startsWith(prefix))    
                .count();
        return prefix + String.format("%03d", count + 1);
    }

    public static void submitNewTicket(Customer customer, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("What problems are you facing?");
        System.out.println("1. I want a refund");
        System.out.println("2. I'm facing an issue");
        System.out.println("3. I want to request a change");
        System.out.println("4. There is a technical difficulty");
        System.out.println("5. Others");
        System.out.println("0. Back to main menu.");
        System.out.print("Enter your choice: ");
        String choice = input.nextLine();

        if (choice.equals("0")) return;

        String description = "";
        
        switch (choice) { 
            case "1" -> submitRefundTicket(customer, input, description);
            case "2" -> submitProblemTicket(customer, input, description);
            case "3" -> submitChangeRequestTicket(customer, input, description);
            case "4" -> submitTechnicalDifficultyTicket(customer, input, description);
            case "5" -> submitGeneralTicket(customer, description);
            default -> System.out.println("Invalid ticket type selected.");
        }
        
        System.out.println("Press [ENTER] to return to main menu...");
        input.nextLine();
    }

    private static void submitRefundTicket(Customer customer, Scanner input, String description) {
        System.out.println("Enter the purchase transaction ID for the refund: ");
        String transactionID = input.nextLine();
        System.out.println("Enter the reason for the refund: ");
        String refundReason = input.nextLine();
        System.out.println("Enter the refund amount: ");
        double refundAmount;
        try {
            refundAmount = Double.parseDouble(input.nextLine());
            if (refundAmount <= 0) {
                System.out.println("Error: Refund amount must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount. Please enter a valid number.");
            return;
        }
        
        String refundId = generateTicketID("RF");
        RefundTicket refundTicket = new RefundTicket(
            refundId, "Refund Request", description, LocalDateTime.now(), customer, null, "Medium", 
            new InteractionLog(customer, "Created refund ticket"), null, transactionID, refundReason, refundAmount
        );
        TicketFileLoader.saveTicketToCSV(refundTicket);
        System.out.println("Refund ticket submitted successfully! Ticket ID: " + refundId);
    }

    private static void submitProblemTicket(Customer customer, Scanner input, String description) {
        System.out.println("Select severity level:");
        System.out.println("1. Low\n2. Medium\n3. High\n4. Critical");
        System.out.print("Enter your choice: ");
        String severityChoice = input.nextLine();
        String severityLevel;
        severityLevel = switch (severityChoice) {
            case "1" -> "Low";
            case "3" -> "High";
            case "4" -> "Critical";
            default -> "Medium";
        };
        
        String problemId = generateTicketID("PR");
        ProblemTicket problemTicket = new ProblemTicket(
            problemId, "Issue Report", description, LocalDateTime.now(), customer, null, "Medium", 
            new InteractionLog(customer, "Created problem ticket"), null, severityLevel
        );
        TicketFileLoader.saveTicketToCSV(problemTicket);
        System.out.println("Problem ticket submitted successfully! Ticket ID: " + problemId);
    }

    private static void submitChangeRequestTicket(Customer customer, Scanner input, String description) {
        System.out.println("Enter the movie ticket ID for the change request: ");
        String movieTicketID = input.nextLine();
        
        String changeId = generateTicketID("CR");
        ChangeRequestTicket changeTicket = new ChangeRequestTicket(
            changeId, "Change Request", description, LocalDateTime.now(), customer, null, "Medium", 
            new InteractionLog(customer, "Created change request ticket"), null, movieTicketID
        );
        TicketFileLoader.saveTicketToCSV(changeTicket);
        System.out.println("Change request ticket submitted successfully! Ticket ID: " + changeId);
    }

    private static void submitTechnicalDifficultyTicket(Customer customer, Scanner input, String description) {
        System.out.println("Select device type:");
        System.out.println("1. Desktop Computer\n2. Laptop\n3. Mobile Phone\n4. Tablet\n5. Other");
        System.out.print("Enter your choice: ");
        String deviceChoice = input.nextLine();
        String deviceType;
        deviceType = switch (deviceChoice) {
            case "1" -> "Desktop Computer";
            case "2" -> "Laptop";
            case "3" -> "Mobile Phone";
            case "4" -> "Tablet";
            default -> "Other";
        };
        
        String techId = generateTicketID("TD");
        TechnicalDifficultyTicket techTicket = new TechnicalDifficultyTicket(
            techId, "Technical Difficulty", description, LocalDateTime.now(), customer, null, "Medium", 
            new InteractionLog(customer, "Created technical difficulty ticket"), null, deviceType
        );
        TicketFileLoader.saveTicketToCSV(techTicket);
        System.out.println("Technical difficulty ticket submitted successfully! Ticket ID: " + techId);
    }

    private static void submitGeneralTicket(Customer customer, String description) {
        String otherId = generateTicketID("TK");
        Ticket otherTicket = new Ticket(
            otherId, "General Inquiry", description, LocalDateTime.now(), customer, null, "Medium", 
            new InteractionLog(customer, "Created general ticket"), null
        );
        TicketFileLoader.saveTicketToCSV(otherTicket);
        System.out.println("General ticket submitted successfully! Ticket ID: " + otherId);
    }
}
