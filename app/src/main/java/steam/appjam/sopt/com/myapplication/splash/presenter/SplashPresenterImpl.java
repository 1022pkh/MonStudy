package steam.appjam.sopt.com.myapplication.splash.presenter;


import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.login.model.User;
import steam.appjam.sopt.com.myapplication.network.NetworkService;
import steam.appjam.sopt.com.myapplication.splash.view.SplashView;

public class SplashPresenterImpl implements SplashPresenter {

    ApplicationController api;
    SplashView view;

    public SplashPresenterImpl(SplashView view) {
        this.view = view;
        api = ApplicationController.getInstance();
    }

    @Override
    public void connectServer() {
        NetworkService networkService = api.getNetworkService();
        Call<User> loginTest = networkService.getSession();
        loginTest.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                view.connectingSucceed(response.code());
            }

            @Override
            public void onFailure(Throwable t) {
                view.networkError();
            }
        });
    }

}
