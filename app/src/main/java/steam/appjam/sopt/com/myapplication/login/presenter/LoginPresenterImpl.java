package steam.appjam.sopt.com.myapplication.login.presenter;


import android.util.Log;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.login.model.Authentication;
import steam.appjam.sopt.com.myapplication.login.model.User;
import steam.appjam.sopt.com.myapplication.login.model.UserInfo;
import steam.appjam.sopt.com.myapplication.login.view.LoginView;
import steam.appjam.sopt.com.myapplication.network.NetworkService;

public class LoginPresenterImpl implements LoginPresenter {

    ApplicationController api;
    Authentication authentication;
    LoginView view;
    NetworkService networkService;

    public LoginPresenterImpl(Authentication authentication, LoginView view) {
        this.authentication = authentication;
        this.view = view;
        api = ApplicationController.getInstance();
        networkService = api.getNetworkService();
    }

    @Override
    public void loginToServer() {
        Call<User> login = networkService.login(authentication);
        login.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    User user = response.body();

                    UserInfo userInfo = new UserInfo();
                    userInfo.user_id = user.Member_userId;
                    userInfo.name = user.Member_nickname;
                    userInfo.email = user.Member_email;
                    userInfo.img = user.Member_picture;

                    api.setLoginUser(userInfo);

                    Log.i("MyTag", "진행 체크1 :" + userInfo.user_id);

                    Log.i("MyTag", "진행 체크4 :" + authentication.user_id);

                    view.loginSucceed(authentication.user_id);

                }
                else{
                    view.loginFailed();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                view.networkError();
            }
        });
    }



}
