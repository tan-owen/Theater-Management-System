package utility;

import entity.User;

public class ConsoleUtil {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printSeparator() {
        System.out.println("========================================");
    }

    public static void printSeparator(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    public static void waitForEnter(java.util.Scanner scanner) {
        System.out.println("Press [ENTER] to continue...");
        scanner.nextLine();
    }

    /**
     * Validates email format using a basic regex pattern.
     * Accepts emails with format: localpart@domain.extension
     * @param email The email address to validate
     * @return true if email format is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Basic email validation regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Checks if username already exists in the user map.
     * @param username The username to check
     * @param userMap The map of existing users
     * @return true if username already exists, false otherwise
     */
    public static boolean usernameExists(String username, java.util.Map<String, User> userMap) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        for (User user : userMap.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
}
