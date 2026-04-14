import java.util.Scanner;

public class CustomerMode {

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