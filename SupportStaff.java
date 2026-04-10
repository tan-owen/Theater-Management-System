public class SupportStaff extends User {        
    public SupportStaff(String userID, String username, String password, String firstName, String lastName, String pronounce) {
        super(userID, username, password, firstName, lastName, pronounce);
    }

    @Override
    public String toString() {
        return String.format("UserID: %s, Username: %s, Password: %s, First Name: %s, Last Name: %s, Pronounce: %s", 
            getUserID(), getUsername(), getPassword(), getFirstName(), getLastName(), getPronounce());
    }
}
