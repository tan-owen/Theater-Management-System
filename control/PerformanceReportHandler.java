package control;

import doa.FeedbackFileLoader;
import doa.TicketFileLoader;
import entity.Comment;
import entity.Feedback;
import entity.Manager;
import entity.SupportStaff;
import entity.Ticket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import utility.ConsoleUtil;

/**
 * Control handler for staff performance reporting.
 * Provides: an all-time staff summary and a filtered monthly performance report
 * with priority/type breakdowns, response times, and feedback summaries.
 */
public class PerformanceReportHandler {

    /**
     * Displays an all-time summary of each support staff member's assigned and
     * resolved ticket counts and resolution rate.
     */
    public static void viewStaffPerformance(Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Staff Performance ===");
        List<Ticket> tickets = TicketFileLoader.loadTickets();

        // Collect unique staff usernames from assigned tickets
        List<String> staffMembers = new ArrayList<>();
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
                    if (t.getSupportStaff() != null
                            && t.getSupportStaff().getUsername().equals(staffUsername)) {
                        assigned++;
                        if ("CLOSED".equals(t.getStatus())) {
                            resolved++;
                        }
                    }
                }

                System.out.printf("Staff: %s%n", staffUsername);
                System.out.printf("  - Tickets Assigned: %d%n", assigned);
                System.out.printf("  - Tickets Resolved: %d%n", resolved);
                System.out.printf("  - Resolution Rate : %.1f%%%n%n",
                        assigned > 0 ? (resolved * 100.0 / assigned) : 0);
            }
        }

        System.out.println("Press [ENTER] to return...");
        input.nextLine();
    }

    /**
     * Prompts the user to select a month and year, then prints a detailed
     * performance report for that period including overall summary, priority
     * and type breakdowns, average response time, and customer feedback.
     */
    public static void generateMonthlyReport(Scanner input) {
        ConsoleUtil.clearScreen();
        System.out.println("=== Monthly Performance Report ===");

        // ── Month / Year selection ──────────────────────────────────────────
        int selectedYear = 0;
        while (selectedYear == 0) {
            System.out.print("Enter year  (e.g. 2026): ");
            String yearInput = input.nextLine().trim();
            try {
                int y = Integer.parseInt(yearInput);
                if (y >= 2000 && y <= 2100) {
                    selectedYear = y;
                } else {
                    System.out.println("Invalid year. Please enter a year between 2000 and 2100.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric year.");
            }
        }

        int selectedMonth = 0;
        while (selectedMonth == 0) {
            System.out.print("Enter month (1 - 12)   : ");
            String monthInput = input.nextLine().trim();
            try {
                int m = Integer.parseInt(monthInput);
                if (m >= 1 && m <= 12) {
                    selectedMonth = m;
                } else {
                    System.out.println("Invalid month. Please enter a number between 1 and 12.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric month.");
            }
        }

        // ── Month label for display ─────────────────────────────────────────
        String[] monthNames = { "", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        String reportLabel = monthNames[selectedMonth] + " " + selectedYear;

        ConsoleUtil.clearScreen();
        System.out.println("=== Monthly Performance Report: " + reportLabel + " ===");

        // ── Filter tickets to the selected month/year ───────────────────────
        List<Ticket> allTickets = TicketFileLoader.loadTickets();
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket t : allTickets) {
            if (t.getCreationTime() != null
                    && t.getCreationTime().getYear() == selectedYear
                    && t.getCreationTime().getMonthValue() == selectedMonth) {
                tickets.add(t);
            }
        }

        if (tickets.isEmpty()) {
            System.out.println("No tickets were submitted in " + reportLabel + ".");
            System.out.println("\nPress [ENTER] to return...");
            input.nextLine();
            return;
        }

        // ── Overall counts ──────────────────────────────────────────────────
        int totalTickets = tickets.size();
        int closedTickets = 0;
        int openTickets = 0;
        for (Ticket t : tickets) {
            if ("CLOSED".equals(t.getStatus())) {
                closedTickets++;
            } else {
                openTickets++;
            }
        }

        System.out.println("\n=== Overall Summary ===");
        System.out.printf("Total Tickets Submitted : %d%n", totalTickets);
        System.out.printf("Resolved (Closed)       : %d%n", closedTickets);
        System.out.printf("Open / In-Progress      : %d%n", openTickets);
        System.out.printf("Resolution Rate         : %.1f%%%n%n",
                totalTickets > 0 ? (closedTickets * 100.0 / totalTickets) : 0);

        // ── Priority breakdown ──────────────────────────────────────────────
        int criticalCount = 0, highCount = 0, mediumCount = 0, lowCount = 0;
        for (Ticket t : tickets) {
            String priority = t.getPriorityLevel();
            if (priority != null) {
                switch (priority) {
                    case "Critical" -> criticalCount++;
                    case "High"     -> highCount++;
                    case "Medium"   -> mediumCount++;
                    case "Low"      -> lowCount++;
                }
            }
        }

        System.out.println("Tickets by Priority:");
        System.out.printf("  Critical : %d%n", criticalCount);
        System.out.printf("  High     : %d%n", highCount);
        System.out.printf("  Medium   : %d%n", mediumCount);
        System.out.printf("  Low      : %d%n%n", lowCount);

        // ── Ticket type breakdown ───────────────────────────────────────────
        int refundCount = 0, techCount = 0, changeCount = 0, problemCount = 0, otherCount = 0;
        for (Ticket t : tickets) {
            switch (t.getTicketType()) {
                case "RefundTicket"              -> refundCount++;
                case "TechnicalDifficultyTicket" -> techCount++;
                case "ChangeRequestTicket"       -> changeCount++;
                case "ProblemTicket"             -> problemCount++;
                default                          -> otherCount++;
            }
        }

        System.out.println("Tickets by Type:");
        System.out.printf("  Refund Request       : %d%n", refundCount);
        System.out.printf("  Technical Difficulty : %d%n", techCount);
        System.out.printf("  Change Request       : %d%n", changeCount);
        System.out.printf("  Problem Report       : %d%n", problemCount);
        System.out.printf("  General / Other      : %d%n%n", otherCount);

        // ── Average first-response time ─────────────────────────────────────
        long totalResponseMinutes = 0;
        int responseSampleCount = 0;
        for (Ticket t : tickets) {
            if (t.getDiscussionThread() != null) {
                for (Comment c : t.getDiscussionThread()) {
                    if (c.getAuthor() instanceof SupportStaff || c.getAuthor() instanceof Manager) {
                        long minutes = Duration.between(t.getCreationTime(), c.getTimestamp()).toMinutes();
                        if (minutes >= 0) {
                            totalResponseMinutes += minutes;
                            responseSampleCount++;
                        }
                        break; // Only count the first staff comment per ticket
                    }
                }
            }
        }

        System.out.println("Response Time:");
        if (responseSampleCount > 0) {
            double avgMinutes = (double) totalResponseMinutes / responseSampleCount;
            long avgHours = (long) avgMinutes / 60;
            long avgMins  = (long) avgMinutes % 60;
            System.out.printf("  Average First-Response Time : %d h %02d min  (based on %d tickets)%n%n",
                    avgHours, avgMins, responseSampleCount);
        } else {
            System.out.println("  No response-time data available for this period.\n");
        }

        // ── Feedback summary for the selected month ─────────────────────────
        List<Feedback> allFeedbacks = FeedbackFileLoader.loadAll();
        Set<String> monthTicketIDs = new HashSet<>();
        for (Ticket t : tickets) {
            monthTicketIDs.add(t.getTicketID());
        }

        List<Feedback> monthFeedbacks = allFeedbacks.stream()
                .filter(f -> monthTicketIDs.contains(f.getTicketID()))
                .toList();

        System.out.println("Customer Feedback:");
        if (!monthFeedbacks.isEmpty()) {
            double sum = monthFeedbacks.stream().mapToInt(Feedback::getRating).sum();
            System.out.printf("  Average Rating : %.2f / 5.00  (%d responses)%n",
                    sum / monthFeedbacks.size(), monthFeedbacks.size());
        } else {
            System.out.println("  No feedback submitted for tickets in this period.");
        }

        System.out.println("\nPress [ENTER] to return...");
        input.nextLine();
    }
}
