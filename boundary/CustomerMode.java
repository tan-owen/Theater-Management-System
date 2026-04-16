package boundary;

import control.*;
import entity.Customer;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Boundary class for customer UI mode
 */
public class CustomerMode {

    public static void run(Customer customer, String[] args) {
        Scanner input = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            ConsoleUtil.clearScreen();
            System.out.println("Welcome, " + customer.getPronounce() + ". " + customer.getFirstName() + " " + customer.getLastName() + ".");
            System.out.println("1. Submit a new ticket"); 
            System.out.println("2. View my tickets"); 
            System.out.println("3. Ticket Discussion"); 
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            switch (input.nextLine()) {
                case "1":
                    TicketSubmissionHandler.submitNewTicket(customer, input);
                    break;
                case "2":
                    MyTicketsHandler.viewMyTickets(customer, input);
                    break;
                case "3":
                    DiscussionHandler.viewTicketDiscussion(customer, input);
                    break;
                case "0":
                    System.out.println("Logging out...");
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Press [ENTER] to try again.");
                    input.nextLine();
                    break;
            }
        }
    }
}
