package control;

import doa.TicketFileLoader;
import entity.*;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for viewing interaction history
 */
public class InteractionHistoryHandler {

    public static void viewInteractionHistory(Manager manager, Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Interaction History ===");
        List<Ticket> tickets = TicketFileLoader.loadTickets();

        System.out.println("Your interactions:");
        System.out.println("==================");
        boolean foundInteractions = false;

        for (Ticket t : tickets) {
            if (t.getInteractionLog() != null && t.getInteractionLog().getUser().getUserID().equals(manager.getUserID())) {
                System.out.println(t.getInteractionLog().getFormattedLog());
                foundInteractions = true;
            }
            
            if (t.getDiscussionThread() != null) {
                for (Comment c : t.getDiscussionThread()) {
                    if (c.getAuthor().getUserID().equals(manager.getUserID())) {
                        System.out.println("[Ticket: " + t.getTicketID() + "] " + c.getFormattedComment());
                        foundInteractions = true;
                    }
                }
            }
        }

        if (!foundInteractions) {
            System.out.println("No interactions found.");
        }

        System.out.println("\nPress [ENTER] to return...");
        input.nextLine();
    }
}
