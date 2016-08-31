package steam.appjam.sopt.com.myapplication.join.presenter;

import steam.appjam.sopt.com.myapplication.join.model.JoinUser;

/**
 * Created by Min_Mac on 16. 4. 13..
 */
public interface JoinPresenter {

    void registerToServer(JoinUser user);

    void checkIdDuplication(String user_id);

    void checkNameDuplication(String name);
}

