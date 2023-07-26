package sg.edu.np.mad.easyread;

public class User {
    private String username;

    private String email;
    private String password;
    private String creationDate;


    private  boolean notification_setting;

    private String imageUrl;

    private String userId;


    public User(String username, String email, String imageUrl, String userId) {
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.userId = userId;

    }
    public boolean isNotification_setting() {
        return notification_setting;
    }

    public void setNotification_setting(boolean notification_setting) {
        this.notification_setting = notification_setting;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String  getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
