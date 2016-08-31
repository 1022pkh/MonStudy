package steam.appjam.sopt.com.myapplication.join.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
import steam.appjam.sopt.com.myapplication.ErrorController;
import steam.appjam.sopt.com.myapplication.JoinDialog;
import steam.appjam.sopt.com.myapplication.MainActivity;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.join.component.CharacterAdapter;
import steam.appjam.sopt.com.myapplication.join.model.JoinUser;
import steam.appjam.sopt.com.myapplication.join.presenter.JoinPresenter;
import steam.appjam.sopt.com.myapplication.join.presenter.JoinPresenterImpl;

public class JoinCharActivity extends AppCompatActivity implements JoinCharView {

    JoinPresenter presenter;
    Intent intent;

    ViewPager pager;
    Toast toast;
    Handler handler;

    private int position;
    private int img;
    Button join_btn_join;

    private JoinDialog dialog_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_char);

        ButterKnife.bind(this);

        presenter = new JoinPresenterImpl(this);

        pager = (ViewPager) findViewById(R.id.vp_character);

        //ViewPager에 설정할 Adapter 객체 생성
        //ListView에서 사용하는 Adapter와 같은 역할.
        //다만. ViewPager로 스크롤 될 수 있도록 되어 있다는 것이 다름
        //PagerAdapter를 상속받은 Characterdapter 객체 생성
        //Characterdapter LayoutInflater 객체 전달
        CharacterAdapter adapter = new CharacterAdapter(getLayoutInflater());

        //ViewPager에 Adapter 설정
        pager.setAdapter(adapter);

        intent = getIntent();

        join_btn_join = (Button)findViewById(R.id.join_btn_join);
        join_btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 최종 회원가입 누르는 부분.. 네트워크 구현해야해요

                WindowManager.LayoutParams loginParams;
                dialog_join = new JoinDialog(JoinCharActivity.this, joinEvent);

                loginParams = dialog_join.getWindow().getAttributes();

                // Dialog 사이즈 조절 하기
                loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog_join.getWindow().setAttributes(loginParams);

                dialog_join.show();

                setBtnJoin();
            }
        });
    }

    private View.OnClickListener joinEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_join.dismiss();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

    };

    //onClick속성이 지정된 View를 클릭했을때 자동으로 호출되는 메소드
    public void mOnClick(View v) {

        switch (v.getId()) {
            case R.id.btn_previous://이전버튼 클릭
                position = pager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴
                if (position == 0) {
                    img = 2;
                    pager.setCurrentItem(2, true);
                } else if(position == 1){
                    img = 0;
                    pager.setCurrentItem(0, true);
                } else if(position == 2) {
                    img = 1;
                    pager.setCurrentItem(1, true);
                }
                break;

            case R.id.btn_next://다음버튼 클릭
                position = pager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴

                if (position == 2) {
                    img = 1;
                    pager.setCurrentItem(0, true);
                } else if(position ==1 ){
                    img = 2;
                    pager.setCurrentItem(position +1, true);

                } else if(position == 0 ) {
                    img = 1;
                    pager.setCurrentItem(position +1, true);
                }
                break;
        }

    }

    @Override
    public void networkError() {
        ErrorController errorController = new ErrorController(getApplicationContext());
        errorController.notifyNetworkError();
    }

    @Override
    public void registerSucceed() {
        Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void setBtnJoin() {

        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String pw = intent.getStringExtra("pw");
        String email = intent.getStringExtra("email");

        JoinUser user = new JoinUser(id, pw, name, email, img);

        presenter.registerToServer(user);


    }

}