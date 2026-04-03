import java.util.List;
import java.util.Scanner;


public class main {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<User> userArr = FileHandler.loadUsers();
        for (User u : userArr) {
            System.out.println("Loaded: " + u.getUsername() + " as " + u.getClass().getSimpleName());
        }




        System.out.println("Welcome to Theather Management System");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
        
        switch (input.nextInt()) {
            case 1: { // Login
                clearScreen();
                System.out.println("--- Login ---");
                System.out.println("Please enter your username: ");
                String username = input.next();
                System.out.println("Please enter your password: ");
                String password = input.next();
                boolean found = false;
                for (User u : userArr) {
                    if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                        System.out.println("Login successful! Welcome, " + u.getUsername() + " (" + u.getClass().getSimpleName() + ")");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Invalid username or password.");
                }
                break;}
            case 2: { // Register
                clearScreen();
                System.out.println("--- Register ---");
                System.out.println("Please enter your desired username: ");
                String username = input.next();
                System.out.println("Please enter your password: ");
                String password = input.next();
                System.out.println("Please enter your email: ");
                String email = input.next();
                System.out.println("Please enter your phone number: ");
                String phoneNum = input.next();

                String userID = "C" + (userArr.size() + 101);
                FileHandler.saveUserToCSV(new Customer(userID, username, password, email, phoneNum));
                break;}
            case 0: // Back
                System.out.println("Returning to main menu...");
                System.out.println("Press Enter to continue...");
                input.nextLine(); 
                input.nextLine(); 
                
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }



 
  
        
    }
}
