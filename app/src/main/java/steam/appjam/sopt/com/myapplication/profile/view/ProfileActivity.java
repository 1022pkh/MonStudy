package steam.appjam.sopt.com.myapplication.profile.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import steam.appjam.sopt.com.myapplication.ErrorController;
import steam.appjam.sopt.com.myapplication.LoginDialog;
import steam.appjam.sopt.com.myapplication.MainActivity;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.board.BoardView;
import steam.appjam.sopt.com.myapplication.favorite.FavoriteSpace;
import steam.appjam.sopt.com.myapplication.login.view.LoginActivity;
import steam.appjam.sopt.com.myapplication.profile.model.UserProfile;
import steam.appjam.sopt.com.myapplication.profile.presenter.ProfilePresenter;
import steam.appjam.sopt.com.myapplication.profile.presenter.ProfilePresenterImpl;


public class ProfileActivity extends AppCompatActivity implements ProfileView {

    TextView profile_name;
    TextView profile_email;
    int img;
    String imgname;
    ImageView ImageView;

    private LinearLayout mypage;
    private LinearLayout bookmark;
    private LinearLayout board;

    ApplicationController api = ApplicationController.getInstance();
    ProfilePresenter presenter;

    private LoginDialog dialog_login;
    private Toolbar toolbar;

    private CircleImageView imgimgimg;
    private TextView userNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        TextView toolbar_title = ((TextView) findViewById(R.id.main_toolbar_title));
        toolbar_title.setTypeface(Typeface.createFromAsset(getAssets(), "Quicksand_Bold.otf"));
        toolbar_title.setText("MONSTUDY");



        mypage = (LinearLayout)findViewById(R.id.nav_mypage);
        bookmark = (LinearLayout)findViewById(R.id.nav_favorite);
        board = (LinearLayout)findViewById(R.id.nav_board);

        mypage.setOnClickListener(navEvent);
        bookmark.setOnClickListener(navEvent);
        board.setOnClickListener(navEvent);

        ImageView = (ImageView)findViewById(R.id.profile_image);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        profile_name = (TextView)findViewById(R.id.profile_name);
        profile_email = (TextView)findViewById(R.id.profile_email);

        api = ApplicationController.getInstance();
        presenter = new ProfilePresenterImpl(this);
        presenter.getDataFromServer();


        imgimgimg = (CircleImageView)findViewById(R.id.thumnails);

        userNameView = (TextView)findViewById(R.id.userName);

        //로그인 중
        if(api.getCheck()){
            CircleImageView thumnail = (CircleImageView)findViewById(R.id.thumnails);
            thumnail.setVisibility(View.INVISIBLE);

//            Log.i("MyTag","@2222"+api.getLoginUser().img);

            if(api.getLoginUser().img == 1)
                thumnail.setImageResource(R.drawable.ic_character_hobby_big);
            else  if(api.getLoginUser().img == 2){
                thumnail.setImageResource(R.drawable.ic_character_ready_big);
            }
            else{
                thumnail.setImageResource(R.drawable.ic_character_teach_big);
            }


            imgimgimg.setVisibility(View.VISIBLE);
//            profile.set(api.getLoginUser().img);
            userNameView.setText(api.getLoginUser().name);
        }


    }



    private View.OnClickListener loginEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_login.dismiss();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

    };

    private View.OnClickListener loginCancelEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_login.dismiss();
        }

    };


    private View.OnClickListener navEvent = new View.OnClickListener(){
        public void onClick(View v){
            ApplicationController api = new ApplicationController();

            int id = v.getId();

            if (id == R.id.nav_mypage) {

                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(ProfileActivity.this, loginEvent,loginCancelEvent);

                    loginParams = dialog_login.getWindow().getAttributes();

                    // Dialog 사이즈 조절 하기
                    loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog_login.getWindow().setAttributes(loginParams);
                    dialog_login.show();
                }
                else{
                    ;
                }

            } else if (id == R.id.nav_favorite) {
                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(ProfileActivity.this, loginEvent,loginCancelEvent);

                    loginParams = dialog_login.getWindow().getAttributes();

                    // Dialog 사이즈 조절 하기
                    loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog_login.getWindow().setAttributes(loginParams);

                    dialog_login.show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), FavoriteSpace.class);
                    startActivity(intent);
                    finish();
                }
            } else if (id == R.id.nav_board) {
                Intent intent = new Intent(getApplicationContext(), BoardView.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        ProfilePresenter presenter = new ProfilePresenterImpl(this);
        presenter.getDataFromServer();
    }

    @Override
    public void setProfileTexts(UserProfile userProfile) {

        profile_name.setText(api.getLoginUser().name);
        profile_email.setText(api.getLoginUser().email);
        img = api.getLoginUser().img;
        switch (img)
        {
            case 1:
                imgname = "@drawable/ic_character_hobby_big";
                break;
            case 2:
                imgname = "@drawable/ic_character_ready_big";
                break;
            case 3:
            default:
                imgname = "@drawable/ic_character_teach_big";
                break;

        }
        String packName = this.getPackageName();
        int resID = getResources().getIdentifier(imgname, "drawable", packName);
        ImageView.setImageResource(resID);
    }

    @Override
    public void networkError() {
        ErrorController errorController = new ErrorController(getApplicationContext());
        errorController.notifyNetworkError();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        ApplicationController api = new ApplicationController();

        int id = item.getItemId();

        if (id == R.id.nav_mypage) {

            if(!api.getCheck()) {
                Dialogcheck();
            }
            else {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
            }

        } else if (id == R.id.nav_favorite) {
            Intent intent = new Intent(getApplicationContext(), FavoriteSpace.class);
            startActivity(intent);

        } else if (id == R.id.nav_board) {
            Intent intent = new Intent(getApplicationContext(), BoardView.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void Dialogcheck() {
        AlertDialog.Builder buider = new AlertDialog.Builder(this); //AlertDialog.Builder 객체 생성
        buider.setTitle("로그인을 해주세요"); //Dialog 제목
        buider.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            //Dialog에 "Complite"라는 타이틀의 버튼을 설정
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //멤버 정보의 입력을 완료하고 TextView에 추가 하도록 하는 작업 수행

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });
        buider.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            //Dialog에 "Cancel"이라는 타이틀의 버튼을 설정

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //멤버 정보의 입력을 취소하고 Dialog를 종료하는 작업
                //취소하였기에 특별한 작업은 없고 '취소'했다는 메세지만 Toast로 출력
                Toast.makeText(getApplicationContext(), "멤버 추가를 취소합니다", Toast.LENGTH_SHORT).show();
            }
        });

        //설정한 값으로 AlertDialog 객체 생성
        AlertDialog dialog = buider.create();

        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정

        //Dialog 보이기
        dialog.show();
    }
}