package steam.appjam.sopt.com.myapplication.login.view;

public interface LoginView {
    void loginSucceed(String user_id);
    void loginFailed();
    void networkError();
}
