package control;

import boundary.CustomerMode;
import boundary.ManagerMode;
import boundary.SupportStaffMode;
import doa.UserFileLoader;
import entity.Customer;
import entity.Manager;
import entity.SupportStaff;
import entity.User;
import java.util.Map;
import java.util.Scanner;
import utility.ConsoleUtil;
import utility.PasswordHasher;

/**
 * Control handler for user login and self-registration.
 * Validates credentials, routes users to their respective modes,
 * and handles new customer account creation with input validation.
 */
public class LoginRegistrationHandler {

    /**
     * Prompts for username/password and routes the authenticated user to their
     * mode (Manager, Customer, or SupportStaff).
     */
    public static void handleLogin(Scanner input, Map<String, User> userMap, String[] args) {
        ConsoleUtil.clearScreen();
        System.out.println("--- Login ---");
        System.out.print("Please enter your username: ");
        String username = input.nextLine();
        System.out.print("Please enter your password: ");
        String password = input.nextLine();

        // Search for a user with a matching username and correct password
        User foundUser = null;
        for (User u : userMap.values()) {
            if (u.getUsername().equals(username)
                    && PasswordHasher.verifyPasswordWithCombined(password, u.getPassword())) {
                foundUser = u;
                break;
            }
        }

        if (foundUser != null) {
            System.out.println("Login successful! Press [ENTER] to continue...");
            input.nextLine();

            // Route user to their appropriate mode based on concrete type
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

    /**
     * Guides a new user through account registration with validation for
     * username uniqueness, email format, and phone number format.
     */
    public static void handleRegistration(Scanner input, Map<String, User> userMap, String[] args) {
        ConsoleUtil.clearScreen();
        System.out.println("--- Register ---");

        // Username validation — must be non-empty and unique
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

        // Phone number validation — 10 or 11 digits starting with "01"
        String phoneNum = "";
        boolean validPhone = false;
        while (!validPhone) {
            System.out.print("Please enter your phone number (e.g., 01XXXXXXXX): ");
            phoneNum = input.nextLine().trim();

            if (!phoneNum.matches("01\\d{8,9}")) {
                System.out.println("Error: Invalid phone number. Must start with '01' and be 10 or 11 digits total.");
                continue;
            }

            validPhone = true;
        }

        System.out.print("Please enter your first name: ");
        String firstName = input.nextLine();
        System.out.print("Please enter your last name: ");
        String lastName = input.nextLine();
        System.out.print("Please enter your gender (M/F): ");
        String gender = input.nextLine();

        // Map gender input to salutation
        String pronounce;
        if (gender.equalsIgnoreCase("M")) {
            pronounce = "Mr";
        } else if (gender.equalsIgnoreCase("F")) {
            pronounce = "Ms";
        } else {
            pronounce = "Mr/Ms";
        }

        // Generate a unique customer ID based on current user count
        String userID = "C" + (userMap.size() + 101);
        Customer newCustomer = new Customer(userID, username, "", email, phoneNum, firstName, lastName, pronounce);

        // Persist the new customer (hashed password stored in CSV)
        UserFileLoader.saveUserToCSV(newCustomer, password);

        System.out.println("Registration successful! Your user ID is: " + userID);
        System.out.println("Press [ENTER] to return to main menu...");
        input.nextLine();
    }
}
