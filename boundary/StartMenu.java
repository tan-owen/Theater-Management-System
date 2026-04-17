package boundary;

import control.LoginRegistrationHandler;
import doa.*;
import entity.*;
import java.util.Map;
import java.util.Scanner;
import utility.ConsoleUtil;

public class StartMenu {

    public static void run(String[] args) {

        ConsoleUtil.clearScreen();

        boolean running = true;
        while (running) {
            // Reload user data each loop iteration to handle newly registered users
            Scanner input = new Scanner(System.in);
            Map<String, User> userMap = UserFileLoader.loadUsers();

            ConsoleUtil.clearScreen();
            System.out.println("Welcome to Theater CRM Support System");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String choice = input.nextLine();

            switch (choice) {
                case "1" -> LoginRegistrationHandler.handleLogin(input, userMap, args);
                case "2" -> LoginRegistrationHandler.handleRegistration(input, userMap, args);
                case "0" -> {
                    System.out.println("Exiting...");
                    running = false;
                }
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    input.nextLine();
                }
            }
        }
    }

}
