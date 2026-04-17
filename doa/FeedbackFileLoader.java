package doa;

import entity.Feedback;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for persisting Feedback records to data/feedback.csv.
 * Format (tab-separated): ticketID | customerID | rating | comment | submittedAt
 */
public class FeedbackFileLoader {
    private static final String FILE_PATH = "data/feedback.csv";

    /** Save a new feedback entry. Ignores duplicate entries for the same ticket. */
    public static boolean saveFeedback(Feedback feedback) {
        // Prevent duplicate feedback for the same ticket
        for (Feedback existing : loadAll()) {
            if (existing.getTicketID().equalsIgnoreCase(feedback.getTicketID())) {
                return false; // already rated
            }
        }
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            String safeComment = feedback.getComment()
                    .replaceAll("\t", " ")
                    .replaceAll("[\\r\\n]+", " ");
            out.printf("%s\t%s\t%d\t%s\t%s%n",
                feedback.getTicketID(),
                feedback.getCustomerID(),
                feedback.getRating(),
                safeComment,
                feedback.getSubmittedAt().toString());
            out.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving feedback: " + e.getMessage());
            return false;
        }
    }

    /** Load all feedback records from file. */
    public static List<Feedback> loadAll() {
        List<Feedback> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split("\t", -1);
                if (d.length < 5) continue;
                try {
                    String ticketID    = d[0];
                    String customerID  = d[1];
                    int rating         = Integer.parseInt(d[2]);
                    String comment     = d[3];
                    LocalDateTime when = LocalDateTime.parse(d[4]);
                    list.add(new Feedback(ticketID, customerID, rating, comment, when));
                } catch (Exception ignored) { /* skip malformed lines */ }
            }
        } catch (IOException e) {
            System.err.println("Error loading feedback: " + e.getMessage());
        }
        return list;
    }

    /** Return all feedback records for a specific ticket ID. */
    public static List<Feedback> loadForTicket(String ticketID) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback f : loadAll()) {
            if (f.getTicketID().equalsIgnoreCase(ticketID)) {
                result.add(f);
            }
        }
        return result;
    }

    /** Check whether feedback has already been submitted for the given ticket. */
    public static boolean hasFeedback(String ticketID) {
        for (Feedback f : loadAll()) {
            if (f.getTicketID().equalsIgnoreCase(ticketID)) return true;
        }
        return false;
    }
}
