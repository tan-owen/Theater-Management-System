package doa;

import entity.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;


public class DiscussionFileLoader {
    private static final String FILE_PATH = "data/discussion.csv";
    private static final String INTERACTION_FILE_PATH = "data/interaction_log.csv";

    private static String getUserType(User user) {
        String className = user.getClass().getSimpleName();
        return switch (className) {
            case "Manager" -> "Manager";
            case "SupportStaff" -> "Support Staff";
            case "Customer" -> "Customer";
            default -> "User";
        };
    }

    public static void saveCommentToCSV(String ticketID, User author, String message) {
        try {
            LocalDateTime timestamp = LocalDateTime.now();
            String userType = getUserType(author);
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
                out.printf("%s\t%s\t%s\t%s\t%s%n", 
                    ticketID, author.getUserID(), userType, timestamp.toString(), message);
                out.flush();
            }
            // Also append a corresponding interaction log entry for auditing
            try (PrintWriter interactionOut = new PrintWriter(new BufferedWriter(new FileWriter(INTERACTION_FILE_PATH, true)))) {
                // Sanitize message to avoid tabs/newlines breaking the CSV format
                String safeMessage = message.replaceAll("\\t", " ").replaceAll("[\\r\\n]+", " ");
                String actionDetail = "Comment: " + safeMessage;
                interactionOut.printf("%s\t%s\t%s\t%s%n", ticketID, timestamp.toString(), author.getUserID(), actionDetail);
                interactionOut.flush();
            }
        } catch (IOException e) {
            System.err.println("Error saving comment: " + e.getMessage());
        }
    }

    public static void saveInteractionLogToCSV(String ticketID, InteractionLog log) {
        if (log == null || log.getUser() == null) {
            return;
        }
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(INTERACTION_FILE_PATH, true)))) {
            String safeActionDetail = log.getActionDetail().replaceAll("\\t", " ").replaceAll("[\\r\\n]+", " ");
            out.printf("%s\t%s\t%s\t%s%n", 
                ticketID, log.getTimestamp().toString(), log.getUser().getUserID(), safeActionDetail);
            out.flush();
        } catch (IOException e) {
            System.err.println("Error saving interaction log: " + e.getMessage());
        }
    }

    public static void loadDiscussionsIntoTickets(List<Ticket> tickets) {
        Map<String, User> allUsers = UserFileLoader.loadUsers();
        Map<String, List<Comment>> discussions = new HashMap<>();

        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\t", -1);
                
                if (data.length < 4) continue;

                String ticketID = data[0];
                String userID = data[1];
                String timestamp = "";
                String message = "";

                // Handle both old format (4 columns) and new format (5 columns)
                if (data.length == 4) {
                    timestamp = data[2];
                    message = data[3];
                } else if (data.length >= 5) {
                    timestamp = data[3];
                    message = data[4];
                }

                // Get the user
                User author;
                if (allUsers.containsKey(userID)) {
                    author = allUsers.get(userID);
                } else {
                    // Create a dummy user if not found
                    author = new User(userID, userID, "", "", "", "");
                }

                // Create comment with timestamp (userType is now derived from User class)
                Comment comment = new Comment(author, message, LocalDateTime.parse(timestamp));

                // Add to the discussions map
                discussions.putIfAbsent(ticketID, new ArrayList<>());
                discussions.get(ticketID).add(comment);
            }
        } catch (IOException e) {
            System.err.println("Error loading discussions: " + e.getMessage());
        }

        // Populate the discussions into tickets
        for (Ticket ticket : tickets) {
            if (discussions.containsKey(ticket.getTicketID())) {
                ticket.getDiscussionThread().addAll(discussions.get(ticket.getTicketID()));
            }
        }
    }
}
