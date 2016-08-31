package steam.appjam.sopt.com.myapplication.profile.component;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.board.BoardDetail;
import steam.appjam.sopt.com.myapplication.board.BoardItem;
import steam.appjam.sopt.com.myapplication.board.BoardRegister;
import steam.appjam.sopt.com.myapplication.favorite.FavoriteSpace;
import steam.appjam.sopt.com.myapplication.login.view.LoginActivity;
import steam.appjam.sopt.com.myapplication.profile.view.ProfileActivity;

public class ProfileModel extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listview;
    private Button moveTop;
    private Button writeBoard;
    private EditText searchArea;

    private ProfileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        moveTop = (Button)findViewById(R.id.boardTop);
        writeBoard = (Button)findViewById(R.id.boardWrite);
        searchArea = (EditText)findViewById(R.id.edit_Search);
        listview = (ListView)findViewById(R.id.ListView1);

        moveTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.smoothScrollToPosition(0);
            }
        });

        writeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BoardRegister.class);
                startActivity(intent);
            }
        });


        List<BoardItem> item = new ArrayList<BoardItem>();

//        item.add(new BoardItem("안드로이드 스터디 멤버 구합니다~","댓글 수","닉네임","작성날짜","스터디 유형","지역"));
//        item.add(new BoardItem("자바 스터디 멤버 구합니다~","댓글 수","닉네임","작성날짜","스터디 유형","지역"));
//        item.add(new BoardItem("C언어 스터디 멤버 구합니다~","댓글 수","닉네임","작성날짜","스터디 유형","지역"));
//        item.add(new BoardItem("웹 스터디 멤버 구합니다~","댓글 수","닉네임","작성날짜","스터디 유형","지역"));
//        item.add(new BoardItem("디자인 스터디 멤버 구합니다~","댓글 수","닉네임","작성날짜","스터디 유형","지역"));
//        item.add(new BoardItem("기획 스터디 멤버 구합니다~","댓글 수","닉네임","작성날짜","스터디 유형","지역"));
//        item.add(new BoardItem("꿀잠 스터디 멤버 구합니다~","댓글 수","닉네임","작성날짜","스터디 유형","지역"));
//        item.add(new BoardItem("놀기 스터디 멤버 구합니다~","댓글 수","닉네임","작성날짜","스터디 유형","지역"));
//        item.add(new BoardItem("스터디 멤버 구합니다~","댓글 수","닉네임","작성날짜","스터디 유형","지역"));


        adapter = new ProfileAdapter(getApplicationContext(),item);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                // TODO Auto-generated method stub

                Intent intent = new Intent(getApplicationContext(),BoardDetail.class);
                startActivity(intent);
            }
        });

        // Capture Text in EditText
        searchArea.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchArea.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        ApplicationController api = new ApplicationController();

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
        }else if (id == R.id.nav_board) {
            ;
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
