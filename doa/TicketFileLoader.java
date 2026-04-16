package doa;

import entity.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TicketFileLoader {
    private static final String FILE_PATH = "data/tickets.csv";

    public static void saveTicketToCSV(Ticket ticket) {
        if (isTicketIdDuplicate(ticket.getTicketID())) {
            System.out.println("Error: Ticket ID '" + ticket.getTicketID() + "' already exists.");
            return;
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            String custID = (ticket.getCustomer() != null) ? ticket.getCustomer().getUserID() : "UNASSIGNED";
            String staffID = (ticket.getSupportStaff() != null) ? ticket.getSupportStaff().getUserID() : "UNASSIGNED";
            String ticketStatus = (ticket.getStatus() != null) ? ticket.getStatus() : "OPEN";

            String baseData = String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                ticket.getTicketID(), ticket.getTicketTitle(), ticket.getTicketDescription(),
                ticket.getCreationTime().toString(), custID, staffID, ticket.getPriorityLevel(), ticketStatus);

            if (ticket instanceof RefundTicket rt) {
                out.printf("%s\t%s\t%s%n", baseData, rt.getTransactionID(), rt.getRefundAmount());
            } else if (ticket instanceof TechnicalDifficultyTicket td) {
                out.printf("%s\t%s%n", baseData, td.getDeviceType());
            } else if (ticket instanceof ChangeRequestTicket cr) {
                out.printf("%s\t%s%n", baseData, cr.getMovieTicketID());
            } else if (ticket instanceof ProblemTicket pt) {
                out.printf("%s\t%s%n", baseData, pt.getResolutionSteps());
            } else {
                out.printf("%s%n", baseData);
            }
            out.flush();
        } catch (IOException e) {
            System.err.println("Error saving ticket: " + e.getMessage());
        }
    }

    private static boolean isTicketIdDuplicate(String targetID) {
        List<Ticket> tickets = loadTickets();
        return tickets.stream()
                .anyMatch(t -> t.getTicketID().equalsIgnoreCase(targetID));
    }

    public static List<Ticket> loadTickets() {
        List<Ticket> tickets = new ArrayList<>(); 
        File file = new File(FILE_PATH);

        if (!file.exists()) return tickets;

        try {
            Map<String, User> allUsers = UserFileLoader.loadUsers();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split("\\t", -1);
                    
                    if (data.length < 8) continue;

                    // Parse Base Ticket Data
                    String id = data[0];
                    String title = data[1];
                    String desc = data[2];
                    LocalDateTime creationTime = LocalDateTime.parse(data[3]);
                    String custID = data[4];
                    String staffID = data[5];
                    String priorityLevel = data[6];
                    String status = data[7]; // NEW: persisted status column

                    Customer customer = null;
                    if (allUsers.containsKey(custID) && allUsers.get(custID) instanceof Customer) {
                        customer = (Customer) allUsers.get(custID);
                    }

                    SupportStaff staff = null;
                    if (allUsers.containsKey(staffID) && allUsers.get(staffID) instanceof SupportStaff) {
                        staff = (SupportStaff) allUsers.get(staffID);
                    }

                    // Switch based on the ID Prefix to build the correct Subclass
                    // NOTE: type-specific fields now start at index 8 (status occupies index 7)
                    String prefix = id.substring(0, 2).toUpperCase();
                    Ticket created = null;

                    switch (prefix) {
                        case "RF" -> {
                            // Refund - data[8]=transID, data[9]=amount (data[10] was old 'reason' field)
                            if (data.length >= 10) {
                                String transID = data[8];
                                double amount;
                                if (data.length >= 11) {
                                    // Even older format: data[9] is reason, data[10] is amount
                                    amount = Double.parseDouble(data[10]);
                                } else {
                                    // Standard format: data[9] is amount
                                    amount = Double.parseDouble(data[9]);
                                }
                                created = new RefundTicket(id, title, desc, creationTime, customer, staff, priorityLevel, null, null, transID, amount);
                            }
                        }

                        case "TD" -> {
                            // Technical Difficulty - data[8]=deviceType
                            if (data.length >= 9) {
                                String deviceType = data[8];
                                created = new TechnicalDifficultyTicket(id, title, desc, creationTime, customer, staff, priorityLevel, null, null, deviceType);
                            }
                        }

                        case "CR" -> {
                            // Change Request - data[8]=movieTicketID
                            if (data.length >= 9) {
                                String movieTicketID = data[8];
                                created = new ChangeRequestTicket(id, title, desc, creationTime, customer, staff, priorityLevel, null, null, movieTicketID);
                            }
                        }

                        case "PR" -> {
                            // Problem - data[8]=resolutionSteps
                            if (data.length >= 9) {
                                String resolutionSteps = data[8];
                                created = new ProblemTicket(id, title, desc, creationTime, customer, staff, priorityLevel, null, null, resolutionSteps);
                            }
                        }

                        default -> // Generic base ticket
                            created = new Ticket(id, title, desc, creationTime, customer, staff, priorityLevel, null, null);
                    }

                    if (created != null) {
                        created.setStatus(status);
                        tickets.add(created);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading Ticket CSV: " + e.getMessage());
            }

            // Load discussions from CSV
            DiscussionFileLoader.loadDiscussionsIntoTickets(tickets);

            return tickets;
        } catch (Exception e) {
            System.err.println("Error loading tickets: " + e.getMessage());
            return tickets;
        }
    }

    public static boolean updateTicketInCSV(Ticket ticket) {
        List<Ticket> tickets = loadTickets();
        boolean found = false;
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getTicketID().equalsIgnoreCase(ticket.getTicketID())) {
                tickets.set(i, ticket);
                found = true;
                break;
            }
        }
        if (!found) tickets.add(ticket);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, false)))) {
            for (Ticket t : tickets) {
                String custID = (t.getCustomer() != null) ? t.getCustomer().getUserID() : "UNASSIGNED";
                String staffID = (t.getSupportStaff() != null) ? t.getSupportStaff().getUserID() : "UNASSIGNED";
                String tStatus = (t.getStatus() != null) ? t.getStatus() : "OPEN";
                String baseData = String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                    t.getTicketID(), t.getTicketTitle(), t.getTicketDescription(), t.getCreationTime().toString(), custID, staffID, t.getPriorityLevel(), tStatus);

                if (t instanceof RefundTicket rt) {
                    out.printf("%s\t%s\t%s%n", baseData, rt.getTransactionID(), rt.getRefundAmount());
                } else if (t instanceof TechnicalDifficultyTicket td) {
                    out.printf("%s\t%s%n", baseData, td.getDeviceType());
                } else if (t instanceof ChangeRequestTicket cr) {
                    out.printf("%s\t%s%n", baseData, cr.getMovieTicketID());
                } else if (t instanceof ProblemTicket pt) {
                    out.printf("%s\t%s%n", baseData, pt.getResolutionSteps());
                } else {
                    out.printf("%s%n", baseData);
                }
            }
            out.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error updating tickets: " + e.getMessage());
            return false;
        }
    }
}
