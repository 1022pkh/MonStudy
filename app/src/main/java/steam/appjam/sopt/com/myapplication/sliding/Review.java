package steam.appjam.sopt.com.myapplication.sliding;

import android.app.Activity;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.MainActivity;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.RegisterDialog;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.board.BackDialog;
import steam.appjam.sopt.com.myapplication.board.BoardView;
import steam.appjam.sopt.com.myapplication.board.type.Reply_register;
import steam.appjam.sopt.com.myapplication.favorite.FavoriteSpace;
import steam.appjam.sopt.com.myapplication.login.view.LoginActivity;
import steam.appjam.sopt.com.myapplication.profile.view.ProfileActivity;


/**
 * Created by 즤 on 2016-06-29.
 */
public class Review extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    String[] Data = null;
    ArrayAdapter<String> adapter = null;
    Spinner spinner = null;

    private LinearLayout mypage;
    private LinearLayout bookmark;
    private LinearLayout board;

    private BackDialog dialog_back;
    private RegisterDialog dialog_Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_review);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        TextView toolbar_title = ((TextView) findViewById(R.id.main_toolbar_title));
        toolbar_title.setTypeface(Typeface.createFromAsset(getAssets(), "Quicksand_Bold.otf"));
        toolbar_title.setText("MONSTUDY");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        mypage = (LinearLayout)findViewById(R.id.nav_mypage);
        bookmark = (LinearLayout)findViewById(R.id.nav_favorite);
        board = (LinearLayout)findViewById(R.id.nav_board);


        mypage.setOnClickListener(navEvent);
        bookmark.setOnClickListener(navEvent);
        board.setOnClickListener(navEvent);


        final ImageView hart01 = (ImageView) findViewById(R.id.first_hart);
        final ImageView hart02 = (ImageView) findViewById(R.id.second_hart);
        final ImageView hart03 = (ImageView) findViewById(R.id.third_hart);
        final ImageView hart04 = (ImageView) findViewById(R.id.fourth_hart);
        final ImageView hart05 = (ImageView) findViewById(R.id.fifth_hart);

        hart01.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hart01.setBackgroundResource(R.drawable.ic_heart_big);
                hart02.setBackgroundResource(R.drawable.ic_heart_big_blank);
                hart03.setBackgroundResource(R.drawable.ic_heart_big_blank);
                hart04.setBackgroundResource(R.drawable.ic_heart_big_blank);
                hart05.setBackgroundResource(R.drawable.ic_heart_big_blank);
            }
        });

        hart02.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hart01.setBackgroundResource(R.drawable.ic_heart_big);
                hart02.setBackgroundResource(R.drawable.ic_heart_big);
                hart03.setBackgroundResource(R.drawable.ic_heart_big_blank);
                hart04.setBackgroundResource(R.drawable.ic_heart_big_blank);
                hart05.setBackgroundResource(R.drawable.ic_heart_big_blank);
            }
        });

        hart03.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hart01.setBackgroundResource(R.drawable.ic_heart_big);
                hart02.setBackgroundResource(R.drawable.ic_heart_big);
                hart03.setBackgroundResource(R.drawable.ic_heart_big);
                hart04.setBackgroundResource(R.drawable.ic_heart_big_blank);
                hart05.setBackgroundResource(R.drawable.ic_heart_big_blank);
            }
        });

        hart04.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hart01.setBackgroundResource(R.drawable.ic_heart_big);
                hart02.setBackgroundResource(R.drawable.ic_heart_big);
                hart03.setBackgroundResource(R.drawable.ic_heart_big);
                hart04.setBackgroundResource(R.drawable.ic_heart_big);
                hart05.setBackgroundResource(R.drawable.ic_heart_big_blank);
            }
        });

        hart05.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hart01.setBackgroundResource(R.drawable.ic_heart_big);
                hart02.setBackgroundResource(R.drawable.ic_heart_big);
                hart03.setBackgroundResource(R.drawable.ic_heart_big);
                hart04.setBackgroundResource(R.drawable.ic_heart_big);
                hart05.setBackgroundResource(R.drawable.ic_heart_big);
            }
        });




        Button review_btn = (Button) findViewById(R.id.reviewReportButton);

        assert review_btn != null;

        review_btn.setOnClickListener(new View.OnClickListener() {
            // 이종찬 - 후기등록버튼 누를 시 등록하시겠습니까? 1번쨰수정
            @Override
            public void onClick(View v) {
                WindowManager.LayoutParams registerParams;
                dialog_Register = new steam.appjam.sopt.com.myapplication.RegisterDialog(Review.this, registerEvent,registerCancelEvent);

                registerParams = dialog_Register.getWindow().getAttributes();

                // Dialog 사이즈 조절 하기
                registerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                registerParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog_Register.getWindow().setAttributes(registerParams);

                dialog_Register.show();
            }
        });

        spinner = (Spinner)findViewById(R.id.spinner);

        String [] Data = getResources().getStringArray(R.array.studyRoomName);      //Spinner에 필요한 데이터 준비
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Data);    //어뎁터에 생성 및 준비된 데이터를 설정
        spinner.setAdapter(adapter);                                //스피너에 어뎁터를 설정
        //simple_dropdown_item_1line: 안드로이드에서 편의를 위해 제공하는 스피너 전용 레이아웃 리소르
        //기본 리소스는 android.R.layout으로 시작함ut으로 시작함
    }

    // 이종찬 - 후기등록버튼 누를 시 등록하시겠습니까? 2번쨰수정
    // 이부분 네트워크라 잘모르겠어서 내용비워뒀어요. 이부분 비슷한 곳 구현되있는 곳은 BoardDetail.java에 있어요

    private View.OnClickListener registerEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_Register.dismiss();
            finish();
        }
    };

    // 이종찬 - 후기등록버튼 누를 시 등록하시겠습니까? 3번쨰수정
    private View.OnClickListener registerCancelEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_Register.dismiss();
        }
    };


    public void reviewBack (View view)
    {
        finish();
    }

    @Override
    public void onBackPressed() {
        //이종찬 - 뒤로가기 눌렀을때 다이얼로그 1번째수정사항
        WindowManager.LayoutParams loginParams;
        dialog_back = new BackDialog(Review.this, backEvent,backCancleEvent);

        loginParams = dialog_back.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_back.getWindow().setAttributes(loginParams);
        dialog_back.show();
    }

    //이종찬 - 뒤로가기 눌렀을때 다이얼로그 2번째수정사항
    private View.OnClickListener backEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_back.dismiss();
            finish();
        }
    };

    //이종찬 - 뒤로가기 눌렀을때 다이얼로그 3번째수정사항
    private View.OnClickListener backCancleEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_back.dismiss();
        }

    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//
//            Toast.makeText(getApplicationContext(),"Searching",Toast.LENGTH_SHORT).show();
//
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener navEvent = new View.OnClickListener(){
        public void onClick(View v){
            ApplicationController api = new ApplicationController();

            int id = v.getId();

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
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
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
            finish();
        } else if (id == R.id.nav_board) {
            Intent intent = new Intent(getApplicationContext(), BoardView.class);
            startActivity(intent);
            finish();
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
