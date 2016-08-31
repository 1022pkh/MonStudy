package steam.appjam.sopt.com.myapplication.board;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.LoginDialog;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.board.type.BoardType;
import steam.appjam.sopt.com.myapplication.cardview.MyAdapter;
import steam.appjam.sopt.com.myapplication.cardview.MyData;
import steam.appjam.sopt.com.myapplication.favorite.FavoriteSpace;
import steam.appjam.sopt.com.myapplication.login.view.LoginActivity;
import steam.appjam.sopt.com.myapplication.main.type.MainType;
import steam.appjam.sopt.com.myapplication.network.NetworkService;
import steam.appjam.sopt.com.myapplication.profile.view.ProfileActivity;

public class BoardView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listview;
    private Button moveTop;
    private Button writeBoard;
    private EditText searchArea;

    private BoardAdapter adapter;

    private LinearLayout mypage;
    private LinearLayout bookmark;
    private LinearLayout board;

    private LoginDialog dialog_login;

    ApplicationController api;
    private Toolbar toolbar;

    private List<BoardItem> item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

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


        moveTop = (Button)findViewById(R.id.boardTop);
        writeBoard = (Button)findViewById(R.id.boardWrite);
        searchArea = (EditText)findViewById(R.id.edit_Search);
        listview = (ListView)findViewById(R.id.ListView1);



        CircleImageView profile;
        TextView userNameView;

        ApplicationController api = new ApplicationController();
        profile = (CircleImageView)findViewById(R.id.profile_image);
        userNameView = (TextView)findViewById(R.id.userName);

        api = ApplicationController.getInstance();
        //로그인 중
        if(api.getCheck()){
            CircleImageView thumnail = (CircleImageView)findViewById(R.id.profile_image);
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


            profile.setVisibility(View.VISIBLE);
//            profile.set(api.getLoginUser().img);
            userNameView.setText(api.getLoginUser().name);
        }

        moveTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.smoothScrollToPosition(0);
            }
        });

        writeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApplicationController api = new ApplicationController();
                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(BoardView.this, loginEvent,loginCancelEvent);

                    loginParams = dialog_login.getWindow().getAttributes();

                    // Dialog 사이즈 조절 하기
                    loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog_login.getWindow().setAttributes(loginParams);

                    dialog_login.show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), BoardRegister.class);
//                    startActivity(intent);
                    startActivityForResult(intent, 1);
//                    setResult(RESULT_OK, intent);
                }
            }
        });


        item  = new ArrayList<BoardItem>();



        api = ApplicationController.getInstance();
        NetworkService networkService = api.getNetworkService();

        Call<BoardType> getDB = networkService.getBoard();

        getDB.enqueue(new Callback<BoardType>() {
            @Override
            public void onResponse(Response<BoardType> response, Retrofit retrofit) {

                if (response.isSuccess()) {
//                    Log.i("MyTag", "확인 : " + "getDataFromServer3");

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(response.body());
//                    Log.i("MyTag1", jsonString.toString());

                    // JSONObject를 다룰 때 JSONException을 예외처리해주어야 합니다.
                    try{
                        JSONObject jsonObject = new JSONObject(jsonString);

                        JSONArray getBoardInfo = new JSONArray(jsonObject.getString("data"));

                        JSONObject jObject;

                        String Post_id;
                        String Post_title;
                        String Post_datetime;
                        String Post_cate_name;
                        String Post_location_city;
                        String Member_userId;
                        String Member_nickname;
                        String Reply_count;

                        for(int i = 0; i<getBoardInfo.length();i++){
                            jObject = getBoardInfo.getJSONObject(i);


                            Post_id = jObject.getString("Post_id");
                            Post_title = jObject.getString("Post_title");
                            Post_datetime = jObject.getString("Post_datetime");
                            Post_cate_name = jObject.getString("Post_cate_name");
                            Post_location_city = jObject.getString("Post_location_city");
                            Member_userId = jObject.getString("Member_userId");
                            Member_nickname = jObject.getString("Member_nickname");
                            Reply_count = jObject.getString("Reply_count");


//                            String ttt = "https://scloud.pstatic.net/20160520_213/1463723615821mfXjf_JPEG/KakaoTalk_Photo_2016-05-20-14-52-59_91.jpeg?type=m1280_1280";
                            //String name, String location, String address ,String price,String star, int img


                            //item.add(new BoardItem("안드로이드 스터디 멤버 구합니다~","댓글 수","닉네임","작성날짜","스터디 유형","지역"));
                            item.add(new BoardItem(Post_id,Post_title,Reply_count, Member_nickname ,Post_datetime, Post_cate_name, Post_location_city));

                        }

                        adapter = new BoardAdapter(getApplicationContext(),item);
                        listview.setAdapter(adapter);

                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                                // TODO Auto-generated method stub

                                Intent intent = new Intent(getApplicationContext(),BoardDetail.class);
                                intent.putExtra("boardID",item.get(position).getTv_id());
                                startActivity(intent);
                            }
                        });


                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("MyTag", e.getMessage());
                    }

                } else {
                    Log.i("MyTag", "상태 코드 : " + response.code());
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.i("MyTag", "에러 내용 : " + t.getMessage());
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        adapter.notifyDataSetChanged();

    }


    private View.OnClickListener navEvent = new View.OnClickListener(){
        public void onClick(View v){
            ApplicationController api = new ApplicationController();

            int id = v.getId();

            if (id == R.id.nav_mypage) {

                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(BoardView.this, loginEvent,loginCancelEvent);

                    loginParams = dialog_login.getWindow().getAttributes();

                    // Dialog 사이즈 조절 하기
                    loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog_login.getWindow().setAttributes(loginParams);

                    dialog_login.show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    finish();

                }

            } else if (id == R.id.nav_favorite) {
                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(BoardView.this, loginEvent,loginCancelEvent);

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
                ;
            }
        }
    };

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
                WindowManager.LayoutParams loginParams;
                dialog_login = new LoginDialog(BoardView.this, loginEvent,loginCancelEvent);

                loginParams = dialog_login.getWindow().getAttributes();

                // Dialog 사이즈 조절 하기
                loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog_login.getWindow().setAttributes(loginParams);

                dialog_login.show();
            }
            else {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_favorite) {
            if(!api.getCheck()) {
                WindowManager.LayoutParams loginParams;
                dialog_login = new LoginDialog(BoardView.this, loginEvent,loginCancelEvent);

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
            }
        }else if (id == R.id.nav_board) {
            ;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
