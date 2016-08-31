package steam.appjam.sopt.com.myapplication.login.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import steam.appjam.sopt.com.myapplication.ErrorController;
import steam.appjam.sopt.com.myapplication.MainActivity;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.join.view.JoinActivity;
import steam.appjam.sopt.com.myapplication.login.model.Authentication;
import steam.appjam.sopt.com.myapplication.login.presenter.LoginPresenter;
import steam.appjam.sopt.com.myapplication.login.presenter.LoginPresenterImpl;


public class LoginActivity extends AppCompatActivity implements LoginView {

    @Bind(R.id.login_edit_id)
    EditText login_edit_id;
    @Bind(R.id.login_edit_passwd)
    EditText login_edit_passwd;

    TextView login_text_id;
    CharSequence cs;

    Button login_btn_join;
    ApplicationController api;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        login_text_id = (TextView)findViewById(R.id.userName);
        login_btn_join = (Button)findViewById(R.id.login_btn_join);

        login_btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
                finish();

            }
        });

        TextView toolbar_title = ((TextView) findViewById(R.id.main_toolbar_title));
        toolbar_title.setTypeface(Typeface.createFromAsset(getAssets(),"Quicksand_Bold.otf"));
        toolbar_title.setText("MONSTUDY");

//        login_text_id.setText("aaaaaaaaa");
        /*
        //로그인 버튼
        login_btn_login = (Button)findViewById(R.id.login_btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //회원가입 버튼
        btn_join = (Button)findViewById(R.id.login_btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
        */

    }



    @OnClick(R.id.login_btn_login)
    public void setBtn_login() {
        sendData();
    }

    public void sendData() {

        String user_id = login_edit_id.getText().toString();
        String passwd = login_edit_passwd.getText().toString();

        Authentication authentication = null;
        authentication = new Authentication(user_id, passwd);
        LoginPresenter presenter = new LoginPresenterImpl(authentication, this);
        presenter.loginToServer();
    }

    @Override
    public void loginSucceed(String user_id) {
        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
        ApplicationController api = new ApplicationController();
        boolean chkboolean = true;
        api.setCheck(chkboolean);
        Log.i("MyTag", "상태 확인 :" + chkboolean);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void loginFailed() {
        Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void networkError() {
        ErrorController errorController = new ErrorController(getApplicationContext());
        errorController.notifyNetworkError();
    }


}
