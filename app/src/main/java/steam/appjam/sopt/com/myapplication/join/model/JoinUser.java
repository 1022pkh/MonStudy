package steam.appjam.sopt.com.myapplication.join.model;

public class JoinUser {

    public String user_id;
    public String passwd;
    public String name;
    public String email;
    public int img;

    public JoinUser(String user_id, String passwd, String name, String email, int img) {
        this.user_id = user_id;
        this.passwd = passwd;
        this.name = name;
        this.email = email;
        this.img = img;
    }
}
