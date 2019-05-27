package nam.com.rausach.models;

import java.util.Date;

public class User {
    private String username;
    private String fullName;
    private Date sessionExpiryDate;

    public User(String username, String fullName, Date sessionExpiryDate) {
        this.username = username;
        this.fullName = fullName;
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }
}
