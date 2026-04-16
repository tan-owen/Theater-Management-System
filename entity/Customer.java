package entity;

/**
 * Customer class extending User with email and phone number
 */
public class Customer extends User {
    private String email;
    private String phoneNum;

    public Customer(String userID, String username, String passwordHash, String email, String phoneNum, String firstName, String lastName, String pronounce) {
        super(userID, username, passwordHash, firstName, lastName, pronounce);
        this.email = email;
        this.phoneNum = phoneNum;
    }

    // Getters
    public String getEmail() { return email; }
    public String getPhoneNum() { return phoneNum; }

    // Setters
    public void setEmail(String email) { 
        if (utility.ConsoleUtil.isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }

    @Override
    public String toString() {
        return String.format("UserID: %s, Username: %s, Email: %s, Phone: %s, First Name: %s, Last Name: %s", 
            getUserID(), getUsername(), email, phoneNum, getFirstName(), getLastName());
    }
}
