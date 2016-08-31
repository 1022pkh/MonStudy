package steam.appjam.sopt.com.myapplication.profile.presenter;

import android.util.Log;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.network.NetworkService;
import steam.appjam.sopt.com.myapplication.profile.model.UserProfile;
import steam.appjam.sopt.com.myapplication.profile.view.ProfileView;


public class ProfilePresenterImpl implements ProfilePresenter {

    ApplicationController api;
    ProfileView view;
    String user_id;

    public ProfilePresenterImpl(ProfileView view) {
        this.view = view;
        api = ApplicationController.getInstance();
        user_id = api.getLoginUser().user_id;
        Log.i("MyTag", "체크 프로필 아이디" + api.getLoginUser().user_id);

        Log.i("MyTag", "체크 프로필 아이디" + user_id);
    }

    @Override
    public void getDataFromServer() {
        NetworkService networkService = api.getNetworkService();
        Call<UserProfile> userProfileCall = networkService.getUserProfile(user_id);
        Log.i("MyTag", "진행 확인 : " + "getDataFromServer2");
        userProfileCall.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Response<UserProfile> response, Retrofit retrofit) {

                if (response.isSuccess()) {
                    Log.i("MyTag", "진행 확인 : " + "getDataFromServer3");
                    UserProfile userProfile = response.body();
                    api.setProfileUser(userProfile);
                    setProfile();
                    Log.i("MyTag", "UserProfile :" + userProfile.name);
                    Log.i("MyTag", "UserProfile :" + userProfile.email);

                } else {
                    Log.i("MyTag", "상태 코드 : " + response.code());
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.i("MyTag", "에러 내용 : " + t.getMessage());
            }
        });
    }

    @Override
    public void setProfile() {
        view.setProfileTexts(api.getProfileUser());
    }

}
