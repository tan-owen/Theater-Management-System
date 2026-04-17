package boundary;

import control.*;
import entity.SupportStaff;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Boundary class for support staff UI mode
 */
public class SupportStaffMode {
    public static void run(SupportStaff staff, String[] args) {
        Scanner input = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            ConsoleUtil.clearScreen();
            System.out.println("Welcome, " + staff.getPronounce() + ". " + staff.getFirstName() + " " + staff.getLastName() + ".");
            System.out.println("\nSupport Staff Dashboard:");
            System.out.println("1. View my assigned tickets");
            System.out.println("2. View all tickets"); 
            System.out.println("3. View my interaction history");
            System.out.println("4. View Feedback & Ratings");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            
            String menuChoice = input.nextLine();

            switch (menuChoice) {
                case "1" -> StaffTicketViewHandler.viewAssignedTickets(staff, input);
                case "2" -> StaffTicketViewHandler.viewAllTickets(staff, input);
                case "3" -> StaffInteractionHistoryHandler.viewInteractionHistory(staff, input);
                case "4" -> FeedbackHandler.viewAllFeedback(input);
                case "0" -> {
                    System.out.println("Logging out...");
                    keepRunning = false;
                }
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    input.nextLine();
                }
            }
        }
    }
}
