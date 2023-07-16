package sg.edu.np.mad.easyread;

public class Users {
    private String Username;

    private String Email;
    private String Password;

    public Users(String username, String email) {
        Username = username;
        Email = email;

    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
