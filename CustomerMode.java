import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerMode {

    private static String generateTicketID(String prefix) {
        List<Ticket> tickets = TicketFileLoader.loadTickets();
        long count = tickets.stream()
                .filter(t -> t.getTicketID().startsWith(prefix))    
                .count();
        return prefix + String.format("%03d", count + 1);
    }

    public static void run(Customer customer, String[] args) {
        Scanner input = new Scanner(System.in);
        boolean keepRunning = true; // FIX: Added a loop so we don't have to call main.main(args)

        while (keepRunning) {
            main.clearScreen(); // Assuming 'main' is your entry class name
            System.out.println("Welcome, " + customer.getPronounce() + ". " + customer.getFirstName() + " " + customer.getLastName()  + ".");
            System.out.println("1. Submit a new ticket"); 
            System.out.println("2. View my tickets"); 
            System.out.println("3. Ticket Discussion"); 
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            
            String menuChoice = input.nextLine();

            switch (menuChoice) {
                case "1":
                    main.clearScreen();
                    System.out.println("What problems are you facing?");
                    System.out.println("1. I want a refund");
                    System.out.println("2. I'm facing an issue");
                    System.out.println("3. I want to request a change");
                    System.out.println("4. There is a technical difficulty");
                    System.out.println("5. Others");
                    System.out.println("0. Back to main menu.");
                    System.out.print("Enter your choice: ");
                    String choice = input.nextLine();

                    if (choice.equals("0")) break; // Go back to top of loop

                    // System.out.println("Describe your issue in detail ([ENTER] to submit): ");
                    // String description = input.nextLine();
                    String description = "";
                    
                    switch (choice) { 
                        case "1": // Refund Ticket
                            System.out.println("Enter the purchase transaction ID for the refund: ");
                            String transactionID = input.nextLine();
                            System.out.println("Enter the reason for the refund: ");
                            String refundReason = input.nextLine();
                            System.out.println("Enter the refund amount: ");    
                            double refundAmount = input.nextDouble();
                            input.nextLine(); // Clear the scanner buffer
                            
                            String refundId = generateTicketID("RF");
                            RefundTicket refundTicket = new RefundTicket(
                                refundId, "Refund Request", description, LocalDateTime.now(), customer, null, "Medium", 
                                new InteractionLog(customer, "Created refund ticket"), null, transactionID, refundReason, refundAmount
                            );
                            TicketFileLoader.saveTicketToCSV(refundTicket);
                            System.out.println("Refund ticket submitted successfully! Ticket ID: " + refundId);
                            break;

                        case "2": // Problem/Issue Ticket
                            System.out.println("Select severity level:");
                            System.out.println("1. Low\n2. Medium\n3. High\n4. Critical");
                            System.out.print("Enter your choice: ");
                            String severityChoice = input.nextLine();
                            String severityLevel;
                            switch (severityChoice) {
                                case "1": severityLevel = "Low"; break;
                                case "3": severityLevel = "High"; break;
                                case "4": severityLevel = "Critical"; break;
                                default: severityLevel = "Medium"; break; // Handles 2 and invalid inputs safely
                            }
                            
                            String problemId = generateTicketID("PR");
                            ProblemTicket problemTicket = new ProblemTicket(
                                problemId, "Issue Report", description, LocalDateTime.now(), customer, null, "Medium", 
                                new InteractionLog(customer, "Created problem ticket"), null, severityLevel
                            );
                            TicketFileLoader.saveTicketToCSV(problemTicket);
                            System.out.println("Problem ticket submitted successfully! Ticket ID: " + problemId);
                            break;

                        case "3": // Change Request Ticket
                            System.out.println("Enter the movie ticket ID for the change request: ");
                            String movieTicketID = input.nextLine();
                            
                            // FIX: You were missing the prompt for the newSeatID parameter!
                            System.out.println("Enter the new seat ID you want to change to: ");
                            String newSeatID = input.nextLine();
                            
                            String changeId = generateTicketID("CR");
                            ChangeRequestTicket changeTicket = new ChangeRequestTicket(
                                changeId, "Change Request", description, LocalDateTime.now(), customer, null, "Medium", 
                                new InteractionLog(customer, "Created change request ticket"), null, movieTicketID
                            );
                            TicketFileLoader.saveTicketToCSV(changeTicket);
                            System.out.println("Change request ticket submitted successfully! Ticket ID: " + changeId);
                            break;

                        case "4": // Technical Difficulty Ticket
                            System.out.println("Select device type:");
                            System.out.println("1. Desktop Computer\n2. Laptop\n3. Mobile Phone\n4. Tablet\n5. Other");
                            System.out.print("Enter your choice: ");
                            String deviceChoice = input.nextLine();
                            String deviceType;
                            switch (deviceChoice) {
                                case "1": deviceType = "Desktop Computer"; break;
                                case "2": deviceType = "Laptop"; break;
                                case "3": deviceType = "Mobile Phone"; break;
                                case "4": deviceType = "Tablet"; break;
                                default: deviceType = "Other"; break;
                            }
                            
                            String techId = generateTicketID("TD");
                            TechnicalDifficultyTicket techTicket = new TechnicalDifficultyTicket(
                                techId, "Technical Difficulty", description, LocalDateTime.now(), customer, null, "Medium", 
                                new InteractionLog(customer, "Created technical difficulty ticket"), null, deviceType
                            );
                            TicketFileLoader.saveTicketToCSV(techTicket);
                            System.out.println("Technical difficulty ticket submitted successfully! Ticket ID: " + techId);
                            break;

                        case "5": // Other Ticket
                            String otherId = generateTicketID("TK");
                            Ticket otherTicket = new Ticket(
                                otherId, "General Inquiry", description, LocalDateTime.now(), customer, null, "Medium", 
                                new InteractionLog(customer, "Created general ticket"), null
                            );
                            TicketFileLoader.saveTicketToCSV(otherTicket);
                            System.out.println("General ticket submitted successfully! Ticket ID: " + otherId);
                            break;
                            
                        default:
                            System.out.println("Invalid ticket type selected.");
                            break;
                    }
                    
                    System.out.println("Press [ENTER] to return to main menu...");
                    input.nextLine();
                    break; // Ends outer case "1"

                case "2":
                    main.clearScreen();
                    List<Ticket> allTickets = TicketFileLoader.loadTickets();
                    List<Ticket> myTickets = new ArrayList<>();
                    for (Ticket t : allTickets) {
                        if (t.getCustomer() != null && t.getCustomer().getUserID().equals(customer.getUserID())) {
                            myTickets.add(t);
                        }
                    }
                    
                    if (myTickets.isEmpty()) {
                        System.out.println("You have no tickets yet.");
                    } else {
                        System.out.println("Your Tickets:");
                        System.out.println("=============");
                        for (int i = 0; i < myTickets.size(); i++) {
                            Ticket t = myTickets.get(i);
                            System.out.printf("%d. [%s] %s - %s (%s)%n", 
                                i + 1, t.getTicketID(), t.getTicketTitle(), t.getTicketType(), t.getStatus() != null ? t.getStatus() : "Open");
                        }
                        
                        System.out.println("\nEnter ticket number to view details (0 to go back): ");
                        try {
                            int ticketChoice = Integer.parseInt(input.nextLine());
                            if (ticketChoice > 0 && ticketChoice <= myTickets.size()) {
                                Ticket selectedTicket = myTickets.get(ticketChoice - 1);
                                main.clearScreen();
                                System.out.println("Ticket Details:");
                                System.out.println("===============");
                                System.out.println("ID: " + selectedTicket.getTicketID());
                                System.out.println("Title: " + selectedTicket.getTicketTitle());
                                System.out.println("Type: " + selectedTicket.getTicketType());
                                System.out.println("Description: " + selectedTicket.getTicketDescription());
                                System.out.println("Status: " + (selectedTicket.getStatus() != null ? selectedTicket.getStatus() : "Open"));
                                System.out.println("Priority: " + selectedTicket.getPriorityLevel());
                                System.out.println("Created: " + selectedTicket.getCreationTime());
                                
                                // Show ticket-specific details
                                if (selectedTicket instanceof RefundTicket) {
                                    RefundTicket rt = (RefundTicket) selectedTicket;
                                    System.out.println("Transaction ID: " + rt.getTransactionID());
                                    System.out.println("Refund Reason: " + rt.getRefundReason());
                                    System.out.println("Refund Amount: $" + rt.getRefundAmount());
                                } else if (selectedTicket instanceof ProblemTicket) {
                                    ProblemTicket pt = (ProblemTicket) selectedTicket;
                                    System.out.println("Severity Level: " + pt.getSeverityLevel());
                                } else if (selectedTicket instanceof ChangeRequestTicket) {
                                    ChangeRequestTicket cr = (ChangeRequestTicket) selectedTicket;
                                    System.out.println("Movie Ticket ID: " + cr.getMovieTicketID());
                                } else if (selectedTicket instanceof TechnicalDifficultyTicket) {
                                    TechnicalDifficultyTicket td = (TechnicalDifficultyTicket) selectedTicket;
                                    System.out.println("Device Type: " + td.getDeviceType());
                                }
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                    System.out.println("Press [ENTER] to return to main menu...");
                    input.nextLine();
                    break;

                case "3":
                    main.clearScreen();
                    List<Ticket> allDiscussionTickets = TicketFileLoader.loadTickets();
                    List<Ticket> openTickets = new ArrayList<>();
                    for (Ticket t : allDiscussionTickets) {
                        if (t.getStatus() == null || !t.getStatus().equals("Closed")) {
                            openTickets.add(t);
                        }
                    }
                    
                    if (openTickets.isEmpty()) {
                        System.out.println("No open tickets available for discussion.");
                    } else {
                        System.out.println("Open Tickets for Discussion:");
                        System.out.println("============================");
                        for (int i = 0; i < openTickets.size(); i++) {
                            Ticket t = openTickets.get(i);
                            String customerName = t.getCustomer() != null ? t.getCustomer().getFirstName() + " " + t.getCustomer().getLastName() : "Unknown";
                            System.out.printf("%d. [%s] %s - %s (%s)%n", 
                                i + 1, t.getTicketID(), t.getTicketTitle(), customerName, t.getTicketType());
                        }
                        
                        System.out.println("\nEnter ticket number to join discussion (0 to go back): ");
                        try {
                            int ticketChoice = Integer.parseInt(input.nextLine());
                            if (ticketChoice > 0 && ticketChoice <= openTickets.size()) {
                                Ticket selectedTicket = openTickets.get(ticketChoice - 1);
                                main.clearScreen();
                                System.out.println("Discussion for Ticket: " + selectedTicket.getTicketID());
                                System.out.println("Title: " + selectedTicket.getTicketTitle());
                                System.out.println("Description: " + selectedTicket.getTicketDescription());
                                System.out.println("========================================");
                                
                                if (selectedTicket.getDiscussionThread() != null && !selectedTicket.getDiscussionThread().isEmpty()) {
                                    System.out.println("Discussion History:");
                                    for (Comment comment : selectedTicket.getDiscussionThread()) {
                                        System.out.println(comment.getFormattedComment());
                                    }
                                    System.out.println("========================================");
                                } else {
                                    System.out.println("No comments yet.");
                                    System.out.println("========================================");
                                }
                                
                                System.out.println("Enter your comment (or press ENTER to go back): ");
                                String commentText = input.nextLine();
                                if (!commentText.trim().isEmpty()) {
                                    Comment newComment = new Comment(customer, commentText);
                                    System.out.println("Comment added: " + newComment.getFormattedComment());
                                    System.out.println("Note: Comments are not persisted in this demo version.");
                                }
                            }
                        } catch (NumberFormatException e) {

                        }
                    }
                    System.out.println("Press [ENTER] to return to main menu...");
                    input.nextLine();
                    break;

                case "0":
                    System.out.println("Logging out...");
                    keepRunning = false;
                    main.main(args); 
                    break;

                default:

                    System.out.println("Invalid choice. Press [ENTER] to try again.");
                    input.nextLine();
                    break;
            }
        }
    
    }

}