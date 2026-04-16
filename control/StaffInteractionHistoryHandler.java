package control;

import doa.TicketFileLoader;
import entity.*;
import java.time.LocalDateTime;
import java.util.*;
import utility.ConsoleUtil;

/**
 * Control handler for viewing staff interaction history
 */
public class StaffInteractionHistoryHandler {

    public static void viewInteractionHistory(SupportStaff staff, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Interaction History ===");
        List<Ticket> tickets = TicketFileLoader.loadTickets();

        // List to hold interactions with their timestamps for sorting
        List<InteractionEntry> interactions = new ArrayList<>();

        for (Ticket t : tickets) {
            // Collect interaction logs
            if (t.getInteractionLog() != null && t.getInteractionLog().getUser().getUserID().equals(staff.getUserID())) {
                interactions.add(new InteractionEntry(
                    t.getInteractionLog().getTimestamp(),
                    t.getInteractionLog().getFormattedLog()
                ));
            }
            
            // Collect comments
            if (t.getDiscussionThread() != null) {
                for (Comment c : t.getDiscussionThread()) {
                    if (c.getAuthor().getUserID().equals(staff.getUserID())) {
                        interactions.add(new InteractionEntry(
                            c.getTimestamp(),
                            "[Ticket: " + t.getTicketID() + "] " + c.getFormattedComment()
                        ));
                    }
                }
            }
        }

        // Sort interactions by timestamp in ascending order
        interactions.sort(Comparator.comparing(InteractionEntry::getTimestamp));

        if (interactions.isEmpty()) {
            System.out.println("No interactions found.");
        } else {
            for (InteractionEntry entry : interactions) {
                System.out.println(entry.getText());
            }
        }

        System.out.println("\nPress [ENTER] to return...");
        input.nextLine();
    }

    /**
     * Simple helper class to hold interaction data with timestamp
     */
    private static class InteractionEntry {
        private final LocalDateTime timestamp;
        private final String text;

        InteractionEntry(LocalDateTime timestamp, String text) {
            this.timestamp = timestamp;
            this.text = text;
        }

        LocalDateTime getTimestamp() {
            return timestamp;
        }

        String getText() {
            return text;
        }
    }
}
