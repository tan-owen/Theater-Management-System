import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_PATH = "data/accounts.csv";

    // Writeto CSV
    public static void saveUserToCSV(User user) {
        if (isUserIdDuplicate(user.getUserID())) {
            System.out.println("Error: User ID '" + user.getUserID() + "' already exists.");
            return;
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            out.printf("%s,%s,%s,%s,%s%n", 
                user.getUserID(), user.getUsername(), user.getPassword(), user.getEmail(), user.getPhoneNum());
        } catch (IOException e) {
            System.err.println("Error writing: " + e.getMessage());
        }
    }

    private static boolean isUserIdDuplicate(String targetID) {
        return loadUsers().stream().anyMatch(u -> u.getUserID().equalsIgnoreCase(targetID));
    }

    // Read to CSV
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 5) continue; // Skip malformed lines

                String id = data[0];
                String name = data[1];
                String pass = data[2];
                String email = data[3];
                String phone = data[4];

                // Determine user type
                char type = id.toUpperCase().charAt(0);

                switch (type) {
                    case 'C':
                        users.add(new Customer(id, name, pass, email, phone));
                        break;
                    case 'S':
                        users.add(new SupportStaff(id, name, pass, email, phone));
                        break;
                    case 'M':
                        users.add(new Manager(id, name, pass, email, phone));
                        break;
                    default:
                        System.out.println("Unknown user type for ID: " + id);
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
        return users;
    }
}