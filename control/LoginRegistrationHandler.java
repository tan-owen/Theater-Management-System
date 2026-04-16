package control;

import boundary.CustomerMode;
import boundary.ManagerMode;
import boundary.SupportStaffMode;
import doa.*;
import entity.*;
import java.util.Map;
import java.util.Scanner;
import utility.ConsoleUtil;

public class LoginRegistrationHandler {
    public static void handleLogin(Scanner input, Map<String, User> userMap, String[] args) {
        ConsoleUtil.clearScreen();
        System.out.println("--- Login ---");
        System.out.print("Please enter your username: ");
        String username = input.nextLine();
        System.out.print("Please enter your password: ");
        String password = input.nextLine();

        // Search for user with matching username and correct password
        User foundUser = null;
        for (User u : userMap.values()) {
            if (u.getUsername().equals(username) && 
                utility.PasswordHasher.verifyPasswordWithCombined(password, u.getPasswordHash())) {
                foundUser = u;
                break;
            }
        }

        if (foundUser != null) {
            System.out.println("Login successful! Press [ENTER] to continue...");
            input.nextLine();

            // Send user to their mode based on class type
            if (foundUser instanceof Manager manager) {
                ManagerMode.run(manager, userMap, args);
            } else if (foundUser instanceof Customer customer) {
                CustomerMode.run(customer, args);
            } else if (foundUser instanceof SupportStaff supportStaff) {
                SupportStaffMode.run(supportStaff, args);
            }
        } else {
            ConsoleUtil.clearScreen();
            System.out.println("Invalid username or password.");
            System.out.println("Press [ENTER] to return to main menu...");
            input.nextLine();
        }
    }

    public static void handleRegistration(Scanner input, Map<String, User> userMap, String[] args) {
        ConsoleUtil.clearScreen();
        System.out.println("--- Register ---");
        
        // Username validation - check for duplicates
        String username = "";
        boolean validUsername = false;
        while (!validUsername) {
            System.out.print("Please enter your desired username: ");
            username = input.nextLine();
            
            if (username.trim().isEmpty()) {
                System.out.println("Error: Username cannot be empty. Please try again.");
                continue;
            }
            
            if (ConsoleUtil.usernameExists(username, userMap)) {
                System.out.println("Error: This username is already taken. Please choose another username.");
                continue;
            }
            
            validUsername = true;
        }
        
        System.out.print("Please enter your password: ");
        String password = input.nextLine();
        
        // Email validation
        String email = "";
        boolean validEmail = false;
        while (!validEmail) {
            System.out.print("Please enter your email: ");
            email = input.nextLine();
            
            if (!ConsoleUtil.isValidEmail(email)) {
                System.out.println("Error: Invalid email format. Please enter a valid email (e.g., user@example.com)");
                continue;
            }
            
            validEmail = true;
        }
        
        System.out.print("Please enter your phone number: ");
        String phoneNum = input.nextLine();
        System.out.print("Please enter your first name: ");
        String firstName = input.nextLine();
        System.out.print("Please enter your last name: ");
        String lastName = input.nextLine();
        System.out.print("Please enter your gender (M/F): ");
        String gender = input.nextLine();
        
        // Map gender input to pronouns
        String pronounce;
        if (gender.equalsIgnoreCase("M")) {
            pronounce = "Mr";
        } else if (gender.equalsIgnoreCase("F")) {
            pronounce = "Ms";
        } else {
            pronounce = "Mr/Ms";
        }

        // Generate unique customer ID based on current user count
        String userID = "C" + (userMap.size() + 101);
        Customer newCustomer = new Customer(userID, username, "", email, phoneNum, firstName, lastName, pronounce);
        
        // Save the new user to both CSV files (hashed and unhashed versions)
        UserFileLoader.saveUserToCSV(newCustomer, password);
        
        System.out.println("Registration successful! Your user ID is: " + userID);
        System.out.println("Press [ENTER] to return to main menu...");
        input.nextLine();
    }
}
