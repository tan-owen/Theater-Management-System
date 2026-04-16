package control;

import doa.TicketFileLoader;
import doa.UserFileLoader;
import entity.*;
import java.time.LocalDateTime;
import java.util.*;
import utility.ConsoleUtil;

/**
 * Control handler for viewing staff interaction history
 */
public class StaffInteractionHistoryHandler {

    /**
     * Manager entry point: displays a numbered list of all support staff,
     * then shows the selected staff member's interaction history.
     */
    public static void viewStaffInteractionHistory(Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== View Staff Interaction History ===");

        // Collect all support staff from accounts
        Map<String, User> allUsers = UserFileLoader.loadUsers();
        List<SupportStaff> staffList = new ArrayList<>();
        for (User u : allUsers.values()) {
            if (u instanceof SupportStaff ss) {
                staffList.add(ss);
            }
        }

        if (staffList.isEmpty()) {
            System.out.println("No support staff accounts found.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
            return;
        }

        // Display numbered list
        System.out.println("Select a staff member to view:");
        System.out.println("==============================");
        for (int i = 0; i < staffList.size(); i++) {
            SupportStaff ss = staffList.get(i);
            System.out.printf("%d. [%s] %s %s (%s)%n",
                    i + 1, ss.getUserID(), ss.getFirstName(), ss.getLastName(), ss.getUsername());
        }
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");

        try {
            int choice = Integer.parseInt(input.nextLine().trim());
            if (choice == 0) return;
            if (choice < 1 || choice > staffList.size()) {
                System.out.println("Invalid choice. Please try again.");
                System.out.println("Press [ENTER] to return...");
                input.nextLine();
                return;
            }
            viewInteractionHistory(staffList.get(choice - 1), input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            System.out.println("Press [ENTER] to return...");
            input.nextLine();
        }
    }

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
