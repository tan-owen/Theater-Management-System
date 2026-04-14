import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class main {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        
        clearScreen();
        // Load users from CSV
        Scanner input = new Scanner(System.in);
        Map<String, User> userMap = UserFileLoader.loadUsers();
        List<Ticket> tickets = TicketFileLoader.loadTickets();

        for (User u : userMap.values()) {
            if(u instanceof User) {
                System.out.println("Loaded: " + u.getUsername() + " / " + u.getPassword());
                System.out.println("First Name: " + u.getFirstName());
                System.out.println("Last Name: " + u.getLastName());
                
            }
        }

        System.out.println("Total users loaded: " + userMap.size());
        



        // Main menu
        System.out.println("Welcome to Theather Management System");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        
        switch (input.nextLine()) {
            case "1": {
                clearScreen();
                System.out.println("--- Login ---");
                System.out.print("Please enter your username: ");
                String username = input.next();
                System.out.print("Please enter your password: ");
                String password = input.next();
                boolean found = false;
                for (User u : userMap.values()) {
                    if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                        System.out.println("Login successful! Press [ENTER] to continue...");
                        input.nextLine();
                        input.nextLine();


                        found = true;
                        if (u instanceof Manager) {
                            ManagerMode.run((Manager) u, userMap, args);
                        } else if (u instanceof Customer) {
                            CustomerMode.run((Customer) u, args);
                        } else if (u instanceof SupportStaff) {
                            SupportStaffMode.run((SupportStaff) u, args);
                        }
                        break;
                    }
                }
                if (!found) {
                    clearScreen();
                    System.out.println("Invalid username or password.");
                    System.out.println("Press [ENTER] to return to main menu...");
                    input.nextLine();
                    input.nextLine();
                    main(args);
                }
                break;}
            case "2": {
                clearScreen();
                System.out.print("--- Register ---");
                System.out.print("Please enter your desired username: ");
                String username = input.next();
                System.out.print("Please enter your password: ");
                String password = input.next();
                System.out.print("Please enter your email: ");
                String email = input.next();
                System.out.print("Please enter your phone number: ");
                String phoneNum = input.next();
                System.out.print("Please enter your first name: ");
                String firstName = input.next();
                System.out.print("Please enter your last name: ");
                String lastName = input.next();
                System.out.print("Please enter your gender (M/F): ");
                input.nextLine(); // Consume newline character
                String pronounce;
                String gender = input.nextLine(); // Store the input in a variable
                if(gender.equalsIgnoreCase("M")) {
                    pronounce = "Mr";
                } else if(gender.equalsIgnoreCase("F")) {
                    pronounce = "Ms";
                } else {
                    pronounce = "Mr/Ms";
                }


                String userID = "C" + (userMap.size() + 101);
                UserFileLoader.saveUserToCSV(new Customer(userID, username, password, email, phoneNum, firstName, lastName, pronounce));
                System.out.println("Registration successful! Your user ID is: " + userID);
                System.out.println("Press [ENTER] to return to main menu...");
                input.nextLine();
                main(args);    
                break;
            }
            case "0": // Exit
                System.out.println("Exiting...");
        
                
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                System.out.println("Press [ENTER] to return to main menu...");
                input.nextLine();
                main(args);
        }



 
  
        
    }
}
