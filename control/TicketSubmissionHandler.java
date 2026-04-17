package control;

import doa.DiscussionFileLoader;
import doa.TicketFileLoader;
import entity.ChangeRequestTicket;
import entity.Customer;
import entity.InteractionLog;
import entity.ProblemTicket;
import entity.RefundTicket;
import entity.TechnicalDifficultyTicket;
import entity.Ticket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for customer ticket submission.
 * Supports five ticket types: Refund, Problem, Change Request,
 * Technical Difficulty, and General/Other.
 */
public class TicketSubmissionHandler {

        /** Generates a sequential ticket ID with the given prefix (e.g., "RF001"). */
        private static String generateTicketID(String prefix) {
                List<Ticket> tickets = TicketFileLoader.loadTickets();
                long count = tickets.stream()
                                .filter(t -> t.getTicketID().startsWith(prefix))
                                .count();
                return prefix + String.format("%03d", count + 1);
        }

        /**
         * Main entry point: prompts the customer to choose a ticket type, collects
         * the common fields (title, description, priority), then delegates to the
         * appropriate type-specific submission method.
         */
        public static void submitNewTicket(Customer customer, Scanner input) {
                ConsoleUtil.clearScreen();

                System.out.println("\nWhat type of issue is this?");
                System.out.println("1. I want a refund");
                System.out.println("2. I'm facing an issue");
                System.out.println("3. I want to request a change");
                System.out.println("4. There is a technical difficulty");
                System.out.println("5. Others");
                System.out.println("0. Back to main menu.");
                System.out.print("Enter your choice: ");
                String choice = input.nextLine();

                if (choice.equals("0"))
                        return;

                System.out.println("=== Create New Ticket ===");
                System.out.print("Enter ticket title: ");
                String title = input.nextLine();
                if (title.trim().isEmpty()) {
                        System.out.println("Error: Title cannot be empty.");
                        return;
                }

                System.out.print("Enter ticket description: ");
                String description = input.nextLine();
                if (description.trim().isEmpty()) {
                        System.out.println("Error: Description cannot be empty.");
                        return;
                }

                System.out.println("Priority Level");
                System.out.println("1. Low\n2. Medium\n3. High\n4. Critical");
                System.out.print("Enter your choice: ");
                String priorityChoice = input.nextLine();
                String priorityLevel = switch (priorityChoice) {
                        case "1" -> "Low";
                        case "3" -> "High";
                        case "4" -> "Critical";
                        default -> "Medium";
                };

                switch (choice) {
                        case "1" -> submitRefundTicket(customer, input, title, description, priorityLevel);
                        case "2" -> submitProblemTicket(customer, title, description, priorityLevel);
                        case "3" -> submitChangeRequestTicket(customer, input, title, description, priorityLevel);
                        case "4" -> submitTechnicalDifficultyTicket(customer, input, title, description, priorityLevel);
                        case "5" -> submitGeneralTicket(customer, title, description, priorityLevel);
                        default -> System.out.println("Invalid ticket type selected.");
                }

                System.out.println("Press [ENTER] to return to main menu...");
                input.nextLine();
        }

        // Type-specific submission methods

        private static void submitRefundTicket(Customer customer, Scanner input,
                        String title, String description, String priorityLevel) {
                System.out.print("Enter the purchase transaction ID for the refund: ");
                String transactionID = input.nextLine();
                System.out.print("Enter the refund amount: RM ");
                double refundAmount = input.nextDouble();
                input.nextLine(); // consume trailing newline

                String refundId = generateTicketID("RF");
                RefundTicket refundTicket = new RefundTicket(
                                refundId, title, description, LocalDateTime.now(), customer, null, priorityLevel,
                                new InteractionLog(customer, "Created refund ticket - Title: " + title
                                                + ", Priority: " + priorityLevel
                                                + ", Transaction: " + transactionID
                                                + ", Amount: " + refundAmount),
                                null, transactionID, refundAmount);
                TicketFileLoader.saveTicketToCSV(refundTicket);
                DiscussionFileLoader.saveInteractionLogToCSV(refundId, refundTicket.getInteractionLog());
                System.out.println("Refund ticket submitted successfully! Ticket ID: " + refundId);
        }

        private static void submitProblemTicket(Customer customer,
                        String title, String description, String priorityLevel) {
                String resolutionSteps = "UNDETERMINED";

                String problemId = generateTicketID("PR");
                ProblemTicket problemTicket = new ProblemTicket(
                                problemId, title, description, LocalDateTime.now(), customer, null, priorityLevel,
                                new InteractionLog(customer, "Created problem ticket - Title: " + title
                                                + ", Priority: " + priorityLevel
                                                + ", Resolution: " + resolutionSteps),
                                null, resolutionSteps);
                TicketFileLoader.saveTicketToCSV(problemTicket);
                DiscussionFileLoader.saveInteractionLogToCSV(problemId, problemTicket.getInteractionLog());
                System.out.println("Problem ticket submitted successfully! Ticket ID: " + problemId);
        }

        private static void submitChangeRequestTicket(Customer customer, Scanner input,
                        String title, String description, String priorityLevel) {
                System.out.print("Enter the movie ticket ID for the change request: ");
                String movieTicketID = input.nextLine();

                String changeId = generateTicketID("CR");
                ChangeRequestTicket changeTicket = new ChangeRequestTicket(
                                changeId, title, description, LocalDateTime.now(), customer, null, priorityLevel,
                                new InteractionLog(customer, "Created change request ticket - Title: " + title
                                                + ", Priority: " + priorityLevel
                                                + ", MovieTicketID: " + movieTicketID),
                                null, movieTicketID);
                TicketFileLoader.saveTicketToCSV(changeTicket);
                DiscussionFileLoader.saveInteractionLogToCSV(changeId, changeTicket.getInteractionLog());
                System.out.println("Change request ticket submitted successfully! Ticket ID: " + changeId);
        }

        private static void submitTechnicalDifficultyTicket(Customer customer, Scanner input,
                        String title, String description, String priorityLevel) {
                System.out.println("Select device type:");
                System.out.println("1. Desktop Computer\n2. Laptop\n3. Mobile Phone\n4. Tablet\n5. Other");
                System.out.print("Enter your choice: ");
                String deviceChoice = input.nextLine();
                String deviceType = switch (deviceChoice) {
                        case "1" -> "Desktop Computer";
                        case "2" -> "Laptop";
                        case "3" -> "Mobile Phone";
                        case "4" -> "Tablet";
                        default -> "Other";
                };

                String techId = generateTicketID("TD");
                TechnicalDifficultyTicket techTicket = new TechnicalDifficultyTicket(
                                techId, title, description, LocalDateTime.now(), customer, null, priorityLevel,
                                new InteractionLog(customer, "Created technical difficulty ticket - Title: " + title
                                                + ", Priority: " + priorityLevel
                                                + ", Device: " + deviceType),
                                null, deviceType);
                TicketFileLoader.saveTicketToCSV(techTicket);
                DiscussionFileLoader.saveInteractionLogToCSV(techId, techTicket.getInteractionLog());
                System.out.println("Technical difficulty ticket submitted successfully! Ticket ID: " + techId);
        }

        private static void submitGeneralTicket(Customer customer,
                        String title, String description, String priorityLevel) {
                String otherId = generateTicketID("TK");
                Ticket otherTicket = new Ticket(
                                otherId, title, description, LocalDateTime.now(), customer, null, priorityLevel,
                                new InteractionLog(customer, "Created general ticket - Title: " + title
                                                + ", Priority: " + priorityLevel),
                                null);
                TicketFileLoader.saveTicketToCSV(otherTicket);
                DiscussionFileLoader.saveInteractionLogToCSV(otherId, otherTicket.getInteractionLog());
                System.out.println("General ticket submitted successfully! Ticket ID: " + otherId);
        }
}
