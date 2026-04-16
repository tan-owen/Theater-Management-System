package doa;

import entity.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import utility.PasswordHasher;
import utility.ThreadSafeFileManager;

public class UserFileLoader {
    private static final String HASHED_FILE_PATH = "data/accounts.csv";
    private static final String UNHASHED_FILE_PATH = "data/accounts_unhashed.csv"; 

    public static void saveUserToCSV(User user, String plainPassword) {
        // Check for Duplicate ID
        if (isUserIdDuplicate(user.getUserID())) {
            System.out.println("Error: User ID '" + user.getUserID() + "' already exists.");
            return;
        }

        // Check for Duplicate Username
        if (isUsernameDuplicate(user.getUsername())) {
            System.out.println("Error: Username '" + user.getUsername() + "' is already taken.");
            return;
        }

        // Hash the password with salt for secure storage
        String hashedPassword = PasswordHasher.createHashWithSalt(plainPassword);
        user.setPasswordHash(hashedPassword);

        // Save to both the hashed CSV (for security) and unhashed CSV (for reference)
        saveToHashedFile(user, hashedPassword);
        saveToUnhashedFile(user, plainPassword);
    }


    private static void saveToHashedFile(User user, String hashedPassword) {
        ThreadSafeFileManager.performFileOperationWithRetry(() -> {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(HASHED_FILE_PATH, true)))) {
                if (user instanceof Customer) {
                    Customer c = (Customer) user;
                    out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n", 
                        user.getUserID(), user.getUsername(), hashedPassword, user.getFirstName(), 
                        user.getLastName(), user.getPronounce(),  
                        c.getEmail(), c.getPhoneNum());
                } else {
                    out.printf("%s\t%s\t%s\t\t\t%s\t%s\t%s%n", 
                        user.getUserID(), user.getUsername(), hashedPassword, 
                        user.getFirstName(), user.getLastName(), user.getPronounce());
                }
            }
        }, "Save user to hashed CSV");
    }

    private static void saveToUnhashedFile(User user, String plainPassword) {
        ThreadSafeFileManager.performFileOperationWithRetry(() -> {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(UNHASHED_FILE_PATH, true)))) {
                if (user instanceof Customer) {
                    Customer c = (Customer) user;
                    out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n", 
                        user.getUserID(), user.getUsername(), plainPassword, 
                        c.getEmail(), c.getPhoneNum(), user.getFirstName(), 
                        user.getLastName(), user.getPronounce());
                } else {
                    out.printf("%s\t%s\t%s\t\t\t%s\t%s\t%s%n", 
                        user.getUserID(), user.getUsername(), plainPassword, 
                        user.getFirstName(), user.getLastName(), user.getPronounce());
                }
            }
        }, "Save user to unhashed CSV");
    }


    public static boolean verifyPassword(String username, String plainPassword) {
        User user = loadUserByUsername(username);
        if (user == null) {
            return false;
        }
        return PasswordHasher.verifyPasswordWithCombined(plainPassword, user.getPasswordHash());
    }

    public static User loadUserByUsername(String username) {
        Map<String, User> users = loadUsers();
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    private static boolean isUserIdDuplicate(String targetID) {
        return loadUsers().containsKey(targetID);
    }

    private static boolean isUsernameDuplicate(String targetUsername) {
        return loadUsers().values().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(targetUsername));
    }

    public static Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>(); 
        File file = new File(HASHED_FILE_PATH);

        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t", -1);  // Keep empty fields
                if (data.length < 6) continue;  // Skip incomplete records

                // Parse common fields
                String id = data[0].trim();
                String name = data[1].trim();
                String passHash = data[2].trim();
                String firstName = data[3].trim();
                String lastName = data[4].trim();
                String pronounce = data[5].trim();
                
                // Parse optional fields (for customers)
                String email = (data.length > 6 && !data[6].isEmpty()) ? data[6].trim() : "";
                String phone = (data.length > 7 && !data[7].isEmpty()) ? data[7].trim() : "";

                // Create user object based on ID prefix
                User user = null;
                switch (id.charAt(0)) {
                    case 'C':  // Customer
                        user = new Customer(id, name, passHash, email, phone, firstName, lastName, pronounce);
                        break;
                    case 'S':  // Support Staff
                        user = new SupportStaff(id, name, passHash, firstName, lastName, pronounce);
                        break;
                    case 'M':  // Manager
                        user = new Manager(id, name, passHash, firstName, lastName, pronounce);
                        break;
                }
                if (user != null) {
                    users.put(id, user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return users;
    }
}
