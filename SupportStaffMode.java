import java.util.Scanner;

public class SupportStaffMode {
    public static void run(SupportStaff staff, String[] args) {
        Scanner input = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\nSupport Staff Dashboard:");
            System.out.println("1. View my assigned tickets");
            System.out.println("2. View all ticket"); 
            System.out.println("3. View my interaction history");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            
            String menuChoice = input.nextLine();

            switch (menuChoice) {
                case "1":
                    // 
                    break;
                case "2": // 
                    System.out.println("Select ticket type to view:");
                    System.out.println("1. All Tickets");
                    System.out.println("2. Refund Tickets");
                    System.out.println("3. Technical Difficulty Tickets");
                    System.out.println("4. Change Request Tickets");
                    System.out.println("5. Problem Tickets");
                    System.out.println("6. Others");
                    System.out.println("0. Exit");
                    System.out.print("Enter your choice: ");
                    String typeChoice = input.nextLine();
                    String ticketType = "";
                    switch (typeChoice) {
                        case "1": ticketType = "Ticket"; break;
                        case "2": ticketType = "RefundTicket"; break;
                        case "3": ticketType = "TechnicalDifficultyTicket"; break;
                        case "4": ticketType = "ChangeRequestTicket"; break;
                        case "5": ticketType = "ProblemTicket"; break;
                        case "6": ticketType = "OtherTicket"; break;
                        case "0": continue; // Go back to main menu
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            continue; // Go back to type selection
                    }
                    
                    break;
                case "3":
                    // Implementation for adding response to ticket
                    break;
                case "0":
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}