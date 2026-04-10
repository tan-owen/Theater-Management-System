public class User {
    private String userID;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String pronounce;
    
    public User(String userID, String username, String password, String firstName, String lastName, String pronounce) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pronounce = pronounce;
    }

    // Getters
    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPronounce() { return pronounce; }

    // Setters
    public void setUserID(String userID) { this.userID = userID; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPronounce(String pronounce) { this.pronounce = pronounce; }

    @Override
    public String toString() {
        return String.format("UserID: %s, Username: %s, First Name: %s, Last Name: %s", 
            userID, username, firstName, lastName);
    }

}