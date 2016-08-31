package steam.appjam.sopt.com.myapplication.profile.model;


public class UserProfile {

    public String email;
    public String name;
    public int img;

    public UserProfile(String name, String email, int img) {
        this.name = name;
        this.email = email;
        this.img = img;
    }
}