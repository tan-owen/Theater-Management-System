import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Write to File
public class UserFileLoader {
    private static final String FILE_PATH = "data/accounts.csv"; 

    public static void saveUserToCSV(User user) {
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

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            if (user instanceof Customer) {
                Customer c = (Customer) user;
                // Using \t for tab separation
                out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n", 
                    user.getUserID(), user.getUsername(), user.getPassword(), 
                    c.getEmail(), c.getPhoneNum(), user.getFirstName(), 
                    user.getLastName(), user.getPronounce());
            } else {
                out.printf("%s\t%s\t%s\t\t\t%s\t%s\t%s%n", 
                    user.getUserID(), user.getUsername(), user.getPassword(), 
                    user.getFirstName(), user.getLastName(), user.getPronounce());
            }
        } catch (IOException e) {
            System.err.println("Error writing: " + e.getMessage());
        }
    }


    private static boolean isUserIdDuplicate(String targetID) {
        return loadUsers().containsKey(targetID);
    }


    private static boolean isUsernameDuplicate(String targetUsername) {
        return loadUsers().values().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(targetUsername));
    }

    // Read from File
    public static Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>(); 
        File file = new File(FILE_PATH);

        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t", -1); 
                if (data.length < 5) continue; 

                String id = data[0].trim();
                String name = data[1].trim();
                String pass = data[2].trim();
                String firstName = data[3].trim();
                String lastName = data[4].trim();
                String pronounce = data[5].trim();
                String email = (data.length > 6 && !data[6].isEmpty()) ? data[6].trim() : "";
                String phone = (data.length > 7 && !data[7].isEmpty()) ? data[7].trim() : "";

                switch (id.charAt(0)) {
                    case 'C':
                        users.put(id, new Customer(id, name, pass, email, phone, firstName, lastName, pronounce));
                        break;
                    case 'S':
                        // Staff/Managers don't need the email/phone variables passed to their constructor
                        users.put(id, new SupportStaff(id, name, pass, firstName, lastName, pronounce));
                        break;
                    case 'M':
                        users.put(id, new Manager(id, name, pass, firstName, lastName, pronounce));
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return users;
    }
}