package control;

import doa.TicketFileLoader;
import entity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for viewing staff performance reports
 */
public class PerformanceReportHandler {

    public static void viewStaffPerformance(Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Staff Performance ===");
        List<Ticket> tickets = TicketFileLoader.loadTickets();
        
        ArrayList<String> staffMembers = new ArrayList<>();
        
        for (Ticket t : tickets) {
            if (t.getSupportStaff() != null) {
                String staffUsername = t.getSupportStaff().getUsername();
                if (!staffMembers.contains(staffUsername)) {
                    staffMembers.add(staffUsername);
                }
            }
        }

        if (staffMembers.isEmpty()) {
            System.out.println("No staff members have been assigned tickets yet.");
        } else {
            System.out.println("Staff Performance Summary:");
            System.out.println("==========================");
            for (String staffUsername : staffMembers) {
                int assigned = 0;
                int resolved = 0;

                for (Ticket t : tickets) {
                    if (t.getSupportStaff() != null && t.getSupportStaff().getUsername().equals(staffUsername)) {
                        assigned++;
                        if ("CLOSED".equals(t.getStatus())) {
                            resolved++;
                        }
                    }
                }

                System.out.printf("Staff: %s%n", staffUsername);
                System.out.printf("  - Tickets Assigned: %d%n", assigned);
                System.out.printf("  - Tickets Resolved: %d%n", resolved);
                System.out.printf("  - Resolution Rate: %.1f%%%n%n", assigned > 0 ? (resolved * 100.0 / assigned) : 0);
            }
        }
        System.out.println("Press [ENTER] to return...");
        input.nextLine();
    }

    public static void generateMonthlyReport(Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Monthly Performance Report ===");
        List<Ticket> tickets = TicketFileLoader.loadTickets();

        int totalTickets = tickets.size();
        int resolvedTickets = 0;
        int openTickets = 0;

        for (Ticket t : tickets) {
            if ("CLOSED".equals(t.getStatus())) {
                resolvedTickets++;
            } else {
                openTickets++;
            }
        }

        System.out.println("Report Summary:");
        System.out.println("===============");
        System.out.printf("Total Tickets Received: %d%n", totalTickets);
        System.out.printf("Resolved Tickets: %d%n", resolvedTickets);
        System.out.printf("Open Tickets: %d%n", openTickets);
        System.out.printf("Resolution Rate: %.1f%%%n%n", totalTickets > 0 ? (resolvedTickets * 100.0 / totalTickets) : 0);

        int criticalCount = 0, highCount = 0, mediumCount = 0, lowCount = 0;
        for (Ticket t : tickets) {
            String priority = t.getPriorityLevel();
            if (priority != null) {
                switch (priority) {
                    case "Critical": criticalCount++; break;
                    case "High": highCount++; break;
                    case "Medium": mediumCount++; break;
                    case "Low": lowCount++; break;
                }
            }
        }

        System.out.println("Tickets by Priority:");
        System.out.printf("  - Critical: %d%n", criticalCount);
        System.out.printf("  - High: %d%n", highCount);
        System.out.printf("  - Medium: %d%n", mediumCount);
        System.out.printf("  - Low: %d%n", lowCount);

        System.out.println("\nPress [ENTER] to return...");
        input.nextLine();
    }
}
