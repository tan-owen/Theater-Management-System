package control;

import entity.Manager;
import entity.User;
import java.util.Scanner;

public class ManagerTicketViewHandler {

    public static void viewAllTickets(Manager manager, Scanner input) {
        StaffTicketViewHandler.viewAllTickets((User) manager, input);
    }
}
