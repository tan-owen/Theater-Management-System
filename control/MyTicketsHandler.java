package control;

import doa.TicketFileLoader;
import entity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import utility.ConsoleUtil;

/**
 * Control handler for viewing customer's tickets
 */
public class MyTicketsHandler {

    public static void viewMyTickets(Customer customer, Scanner input) {
        ConsoleUtil.clearScreen();
        List<Ticket> allTickets = TicketFileLoader.loadTickets();
        List<Ticket> myTickets = new ArrayList<>();
        for (Ticket t : allTickets) {
            if (t.getCustomer() != null && t.getCustomer().getUserID().equals(customer.getUserID())) {
                myTickets.add(t);
                System.out.println("Ticket ID: " + t.getTicketID());
            }
        }
        
        if (myTickets.isEmpty()) {
            System.out.println("You have no tickets yet.");
        } else {
            System.out.println("Your Tickets:");
            System.out.println("=============");
            for (int i = 0; i < myTickets.size(); i++) {
                Ticket t = myTickets.get(i);
                System.out.printf("%d. [%s] %s - %s (%s)%n", 
                    i + 1, t.getTicketID(), t.getTicketTitle(), t.getTicketType(), t.getStatus() != null ? t.getStatus() : "Open");
            }
            
            System.out.println("\nEnter ticket number to view details (0 to go back): ");
            try {
                int ticketChoice = Integer.parseInt(input.nextLine());
                if (ticketChoice > 0 && ticketChoice <= myTickets.size()) {
                    Ticket selectedTicket = myTickets.get(ticketChoice - 1);
                    viewTicketDetails(selectedTicket);
                    System.out.println("Press [ENTER] to return to main menu...");
                    input.nextLine();
                } else if (ticketChoice != 0) {
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid ticket number.");
            }
        }
        
    }

    private static void viewTicketDetails(Ticket selectedTicket) {
        ConsoleUtil.clearScreen();
        System.out.println("Ticket Details:");
        System.out.println("===============");
        System.out.println("ID: " + selectedTicket.getTicketID());
        System.out.println("Title: " + selectedTicket.getTicketTitle());
        System.out.println("Type: " + selectedTicket.getTicketType());
        System.out.println("Description: " + selectedTicket.getTicketDescription());
        System.out.println("Status: " + (selectedTicket.getStatus() != null ? selectedTicket.getStatus() : "Open"));
        System.out.println("Priority: " + selectedTicket.getPriorityLevel());
        System.out.println("Created: " + selectedTicket.getCreationTime());
        
        if (selectedTicket instanceof RefundTicket) {
            RefundTicket rt = (RefundTicket) selectedTicket;
            System.out.println("Transaction ID: " + rt.getTransactionID());
            System.out.println("Refund Amount: RM" + String.format("%.2f", rt.getRefundAmount()));
        } else if (selectedTicket instanceof ProblemTicket) {
            ProblemTicket pt = (ProblemTicket) selectedTicket;
            System.out.println("Severity Level: " + pt.getResolutionSteps());
        } else if (selectedTicket instanceof ChangeRequestTicket) {
            ChangeRequestTicket cr = (ChangeRequestTicket) selectedTicket;
            System.out.println("Movie Ticket ID: " + cr.getMovieTicketID());
        } else if (selectedTicket instanceof TechnicalDifficultyTicket) {
            TechnicalDifficultyTicket td = (TechnicalDifficultyTicket) selectedTicket;
            System.out.println("Device Type: " + td.getDeviceType());
        }
    }
}
