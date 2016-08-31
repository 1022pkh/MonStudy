package steam.appjam.sopt.com.myapplication.join.view;

/**
 * Created by Min_Mac on 16. 4. 14..
 */
public interface JoinView {
    void networkError();
    void isDuplicated(String result);
    void isNameDuplicated(String result);
}
