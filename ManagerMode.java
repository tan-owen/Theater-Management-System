import java.util.Scanner;

public class ManagerMode {
    public static void run(Manager manager, String[] args) {
        Scanner input = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\nManager Dashboard:");
            System.out.println("1. Assign a ticket"); // filter by assigned and unassigned tocket and allows the manager to assign or reassign a ticket to a different support staff, with an option to add a comment explaining the reason for reassignment
            System.out.println("2. View Staff Performance"); // calculates the number of tickets assigned and resolved for each support staff, and average response and resolution time
            System.out.println("3. Generate Montly performance report"); // generates a report summarizing key metrics for the past month, such as total tickets received, resolved, average resolution time, and staff performance highlights
            System.out.println("4. View all ticket"); // allow manager to view all tickets in the system, with filters for ticket type, status, priority, etc then join discussion on a specific ticket
            System.out.println("5. View my interaction history"); // shows all interactions (comments, status changes) that the manager has been involved in across all tickets
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            
            String menuChoice = input.nextLine();

        }   
    }
}
