package entity;

/**
 * Base User class with password hashing support
 */
public class User {
    private String userID;
    private String username;
    private String firstName;
    private String lastName;
    private String passwordHash;  // Stores hash:salt combined
    private String pronounce;
    
    public User(String userID, String username, String passwordHash, String firstName, String lastName, String pronounce) {
        this.userID = userID;
        this.username = username;
        this.passwordHash = passwordHash;  // Should be in format "hash:salt"
        this.firstName = firstName;
        this.lastName = lastName;
        this.pronounce = pronounce;
    }

    // Getters
    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPronounce() { return pronounce; }

    // Setters
    public void setUserID(String userID) { this.userID = userID; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPronounce(String pronounce) { this.pronounce = pronounce; }

    @Override
    public String toString() {
        return String.format("UserID: %s, Username: %s, First Name: %s, Last Name: %s", 
            userID, username, firstName, lastName);
    }
}
