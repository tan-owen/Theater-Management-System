public class User {
    private String userID;
    private String username;
    private String password;
    private String email;
    private String phoneNum;
    
    public User(String userID, String username, String password, String email, String phoneNum) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    // Getters
    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhoneNum() { return phoneNum; }
}