import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TicketFileLoader {

    private static final String FILE_PATH = "data/tickets.csv";

    // Write to File
    public static void saveTicketToCSV(Ticket ticket) {
        if (isTicketIdDuplicate(ticket.getTicketID())) {
            System.out.println("Error: Ticket ID '" + ticket.getTicketID() + "' already exists.");
            return;
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            
            // Extract common fields that EVERY ticket has
            String custID = (ticket.getCustomer() != null) ? ticket.getCustomer().getUserID() : "UNASSIGNED";
            String staffID = (ticket.getSupportStaff() != null) ? ticket.getSupportStaff().getUserID() : "UNASSIGNED";
            
            String baseData = String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s", 
                ticket.getTicketID(), ticket.getTicketTitle(), ticket.getTicketDescription(), 
                ticket.getCreationTime().toString(), custID, staffID, ticket.getPriorityLevel());

            // 2. Append subclass-specific fields based on ticket type
            if (ticket instanceof RefundTicket) {
                RefundTicket rt = (RefundTicket) ticket;
                out.printf("%s\t%s\t%s\t%s%n", baseData, rt.getTransactionID(), rt.getRefundReason(), rt.getRefundAmount());
                
            } else if (ticket instanceof TechnicalDifficultyTicket) {
                TechnicalDifficultyTicket td = (TechnicalDifficultyTicket) ticket;
                out.printf("%s\t%s%n", baseData, td.getDeviceType());
                
            } else if (ticket instanceof ChangeRequestTicket) {
                ChangeRequestTicket cr = (ChangeRequestTicket) ticket;
                out.printf("%s§%s§%s%n", baseData, cr.getMovieTicketID()); 
                
            } else if (ticket instanceof ProblemTicket) {
                ProblemTicket pt = (ProblemTicket) ticket;
                out.printf("%s\t%s%n", baseData, pt.getSeverityLevel());
                
            } else {
                // Generic fallback for base Ticket
                out.printf("%s%n", baseData);
            }

        } catch (IOException e) {
            System.err.println("Error writing ticket: " + e.getMessage());
        }
    }

    private static boolean isTicketIdDuplicate(String targetID) {
        return loadTickets().stream()
                .anyMatch(t -> t.getTicketID().equalsIgnoreCase(targetID));
    }

    // Read from File
        public static List<Ticket> loadTickets() {
        List<Ticket> tickets = new ArrayList<>(); 
        File file = new File(FILE_PATH);

        if (!file.exists()) return tickets;

        Map<String, User> allUsers = UserFileLoader.loadUsers();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\t", -1);
                
                if (data.length < 7) continue; 

                // 1. Parse Base Ticket Data
                String id = data[0];
                String title = data[1];
                String desc = data[2];
                LocalDateTime creationTime = LocalDateTime.parse(data[3]);
                String custID = data[4];
                String staffID = data[5];
                String priorityLevel = data[6];

                Customer customer = null; 
                if (allUsers.containsKey(custID) && allUsers.get(custID) instanceof Customer) {
                    customer = (Customer) allUsers.get(custID);
                }

                SupportStaff staff = null;
                if (allUsers.containsKey(staffID) && allUsers.get(staffID) instanceof SupportStaff) {
                    staff = (SupportStaff) allUsers.get(staffID);
                }

                // 3. Switch based on the ID Prefix to build the correct Subclass
                String prefix = id.substring(0, 2).toUpperCase();
                
                switch (prefix) {
                    case "RF": // Refund
                        if (data.length >= 10) {
                            String transID = data[7];
                            String reason = data[8];
                            double amount = Double.parseDouble(data[9]);
                            tickets.add(new RefundTicket(id, title, desc, creationTime, customer, staff, priorityLevel, null, null, transID, reason, amount));
                        }
                        break;

                    case "TD": // Technical Difficulty (Incident)
                        if (data.length >= 8) {
                            String deviceType = data[7];
                            tickets.add(new TechnicalDifficultyTicket(id, title, desc, creationTime, customer, staff, priorityLevel, null, null, deviceType));
                        }
                        break;

                    case "CR": // Change Request
                        if (data.length >= 9) {
                            String movieTicketID = data[7];
                            String newSeatID = data[8]; 
                            tickets.add(new ChangeRequestTicket(id, title, desc, creationTime, customer, staff, priorityLevel, null, null, movieTicketID));
                        }
                        break;

                    case "PR": // Problem
                        if (data.length >= 8) {
                            String severity = data[7];
                            tickets.add(new ProblemTicket(id, title, desc, creationTime, customer, staff, priorityLevel, null, null, severity));
                        }
                        break;
                        
                    default:
                        // Generic base ticket
                        tickets.add(new Ticket(id, title, desc, creationTime, customer, staff, priorityLevel, null, null));
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Ticket CSV: " + e.getMessage());
        }
        return tickets;
    }
}