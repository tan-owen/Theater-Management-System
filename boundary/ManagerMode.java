package boundary;

import control.*;
import entity.Manager;
import entity.User;
import java.util.Map;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Boundary class for manager UI mode
 */
public class ManagerMode {
    public static void run(Manager manager, Map<String, User> userMap, String[] args) {
        Scanner input = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            ConsoleUtil.clearScreen();
            System.out.println("Welcome, " + manager.getPronounce() + ". " + manager.getFirstName() + " " + manager.getLastName() + ".");
            System.out.println("\nManager Dashboard:");
            System.out.println("1. Assign a ticket");
            System.out.println("2. View Staff Performance");
            System.out.println("3. Generate Monthly performance report");
            System.out.println("4. View all tickets");
            System.out.println("5. View staff interaction history");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            
            String menuChoice = input.nextLine();

            switch (menuChoice) {
                case "1":
                    TicketAssignmentHandler.assignTicket(manager, input, userMap);
                    break;
                case "2":
                    PerformanceReportHandler.viewStaffPerformance(input);
                    break;
                case "3":
                    PerformanceReportHandler.generateMonthlyReport(input);
                    break;
                case "4":
                    ManagerTicketViewHandler.viewAllTickets(manager, input);
                    break;
                case "5":
                    StaffInteractionHistoryHandler.viewStaffInteractionHistory(input);
                    break;
                case "0":
                    System.out.println("Logging out...");
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    input.nextLine();
                    break;
            }
        }
    }
}
