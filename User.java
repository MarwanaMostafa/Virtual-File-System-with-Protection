import java.util.ArrayList;

public class User {
    private String userName = "";
    private String Password = "";
    private ArrayList<Access> capability = new ArrayList<Access>();

    public User(String name, String Pass) {
        userName = name;
        Password = Pass;
    }

    public void setCapability(ArrayList<Access> capability) {
        this.capability = capability;
    }

    public ArrayList<Access> getCapability() {
        return capability;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public String getUserName() {
        return userName;
    }

}
