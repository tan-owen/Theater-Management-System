package entity;

import java.time.LocalDateTime;

public interface Ticketable {
    String getTicketID();

    String getTicketTitle();

    String getTicketDescription();

    LocalDateTime getCreationTime();

    String getStatus();

    Customer getCustomer();

    SupportStaff getSupportStaff();

    String getPriorityLevel();

    InteractionLog getInteractionLog();

    String getTicketType();

    void setStatus(String status);

    void setPriorityLevel(String priorityLevel);

    void setSupportStaff(SupportStaff supportStaff);

    void setPriorityLevelWithLog(String newPriorityLevel, User user);
}
