package entity;

/**
 * Manager class extending SupportStaff
 */
public class Manager extends SupportStaff {
    
    public Manager(String userID, String username, String passwordHash, String firstName, String lastName, String pronounce) {
        super(userID, username, passwordHash, firstName, lastName, pronounce);
    }

    @Override
    public String toString() {
        return String.format("UserID: %s, Username: %s, First Name: %s, Last Name: %s, Pronounce: %s", 
            getUserID(), getUsername(), getFirstName(), getLastName(), getPronounce());
    }
}
