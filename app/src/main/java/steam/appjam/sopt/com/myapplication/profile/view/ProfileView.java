package steam.appjam.sopt.com.myapplication.profile.view;

import android.support.design.widget.NavigationView;

import steam.appjam.sopt.com.myapplication.profile.model.UserProfile;

public interface ProfileView extends NavigationView.OnNavigationItemSelectedListener {
    void setProfileTexts(UserProfile userProfile);
    void networkError();
}
