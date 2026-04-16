package control;

import doa.DiscussionFileLoader;
import doa.TicketFileLoader;
import entity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for manager to assign tickets to support staff
 */
public class TicketAssignmentHandler {

    public static void assignTicket(Manager manager, Scanner input, Map<String, User> userMap) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Assign Ticket ===");
        List<Ticket> tickets = TicketFileLoader.loadTickets();
        List<Ticket> unassignedTickets = new ArrayList<>();

        for (Ticket t : tickets) {
            if (t.getSupportStaff() == null) {
                unassignedTickets.add(t);
            }
        }

        if (unassignedTickets.isEmpty()) {
            System.out.println("No unassigned tickets available.");
        } else {
            System.out.println("Unassigned Tickets:");
            for (int i = 0; i < unassignedTickets.size(); i++) {
                Ticket t = unassignedTickets.get(i);
                System.out.printf("%d. [%s] %s - %s (%s)%n", 
                    i + 1, t.getTicketID(), t.getTicketTitle(), t.getTicketType(), t.getPriorityLevel());
            }

            System.out.println("\nEnter ticket number to assign (0 to go back): ");
            try {
                int ticketChoice = Integer.parseInt(input.nextLine());
                if (ticketChoice > 0 && ticketChoice <= unassignedTickets.size()) {
                    Ticket selectedTicket = unassignedTickets.get(ticketChoice - 1);
                    
                    System.out.println("\nSupport Staff:");
                    for (User u : userMap.values()) {
                        if (u instanceof SupportStaff) {
                            System.out.printf("- %s (%s %s)%n", u.getUsername(), u.getFirstName(), u.getLastName());
                        }
                    }

                    System.out.println("Enter support staff username to assign to: ");
                    String staffUsername = input.nextLine();
                    
                    System.out.println("Enter reason/comment for assignment: ");
                    String assignmentComment = input.nextLine();
                    
                    SupportStaff assignedStaff = null;
                    for (User u : userMap.values()) {
                        if (u instanceof SupportStaff && u.getUsername().equals(staffUsername)) {
                            assignedStaff = (SupportStaff) u;
                            break;
                        }
                    }
                    
                    if (assignedStaff != null) {
                        selectedTicket.setSupportStaff(assignedStaff);
                        String logMessage = "Assigned to " + assignedStaff.getUsername() + ": " + assignmentComment;
                        InteractionLog assignmentLog = new InteractionLog(manager, logMessage);
                        selectedTicket.setInteractionLog(assignmentLog);
                        
                        boolean ok = TicketFileLoader.updateTicketInCSV(selectedTicket);
                        if (ok) {
                            DiscussionFileLoader.saveInteractionLogToCSV(selectedTicket.getTicketID(), assignmentLog);
                            System.out.println("Assignment recorded for ticket: " + selectedTicket.getTicketID());
                            System.out.println("Assigned to: " + staffUsername);
                            System.out.println("Reason: " + assignmentComment);
                        } else {
                            System.out.println("Error: failed to save assignment for ticket: " + selectedTicket.getTicketID());
                        }
                    } else {
                        System.out.println("Support staff not found. Assignment cancelled.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
        System.out.println("Press [ENTER] to return...");
        input.nextLine();
    }
}
