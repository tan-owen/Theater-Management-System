public class Customer extends User {
    private String email;
    private String phoneNum;


    public Customer(String userID, String username, String password, String email, String phoneNum, String firstName, String lastName, String pronounce) {
        super(userID, username, password, firstName, lastName, pronounce);
        this.email = email;
        this.phoneNum = phoneNum;
    }

    // Getters
    public String getEmail() { return email; }
    public String getPhoneNum() { return phoneNum; }

    // Setters
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }
    

    @Override
    public String toString() {
        return String.format("UserID: %s, Username: %s, Password: %s, Email: %s, Phone: %s, First Name: %s, Last Name: %s", 
            getUserID(), getUsername(), getPassword(), email, phoneNum, getFirstName(), getLastName());
    }
}
