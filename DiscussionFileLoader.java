import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class DiscussionFileLoader {

    private static final String FILE_PATH = "data/discussion.csv";

    // Determine user type from User object
    private static String getUserType(User user) {
        String className = user.getClass().getSimpleName();
        switch (className) {
            case "Manager":
                return "Manager";
            case "SupportStaff":
                return "Support Staff";
            case "Customer":
                return "Customer";
            default:
                return "User";
        }
    }

    // Save a single comment to the discussion CSV
    public static void saveCommentToCSV(String ticketID, User author, String message) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            LocalDateTime timestamp = LocalDateTime.now();
            String userType = getUserType(author);
            out.printf("%s\t%s\t%s\t%s\t%s%n", 
                ticketID, author.getUserID(), userType, timestamp.toString(), message);
        } catch (IOException e) {
            System.err.println("Error writing discussion comment: " + e.getMessage());
        }
    }

    // Load all discussions and populate ticket discussion threads
    public static void loadDiscussionsIntoTickets(List<Ticket> tickets) {
        Map<String, User> allUsers = UserFileLoader.loadUsers();
        Map<String, List<Comment>> discussions = new HashMap<>();

        // Load all comments from CSV
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\t", -1);
                
                if (data.length < 4) continue;

                String ticketID = data[0];
                String userID = data[1];
                String userType = "";
                String timestamp = "";
                String message = "";

                // Handle both old format (4 columns) and new format (5 columns)
                if (data.length == 4) {
                    // Old format: ticketID\tuserID\ttimestamp\tmessage
                    timestamp = data[2];
                    message = data[3];
                } else if (data.length >= 5) {
                    // New format: ticketID\tuserID\tuserType\ttimestamp\tmessage
                    userType = data[2];
                    timestamp = data[3];
                    message = data[4];
                }

                // Get the user
                User author = null;
                if (allUsers.containsKey(userID)) {
                    author = allUsers.get(userID);
                    // If userType is empty, derive it from the user object
                    if (userType.isEmpty()) {
                        userType = getUserType(author);
                    }
                } else {
                    // Create a dummy user if not found
                    author = new User(userID, userID, "", "", "", "");
                    if (userType.isEmpty()) {
                        userType = "User";
                    }
                }

                // Create comment with user type
                Comment comment = new Comment(author, message, LocalDateTime.parse(timestamp), userType);

                // Add to the discussions map
                discussions.putIfAbsent(ticketID, new ArrayList<>());
                discussions.get(ticketID).add(comment);
            }
        } catch (IOException e) {
            System.err.println("Error reading Discussion CSV: " + e.getMessage());
        }

        // Populate the discussions into tickets
        for (Ticket ticket : tickets) {
            if (discussions.containsKey(ticket.getTicketID())) {
                ticket.getDiscussionThread().addAll(discussions.get(ticket.getTicketID()));
            }
        }
    }
}
