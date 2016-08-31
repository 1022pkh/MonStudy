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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.*;
import steam.appjam.sopt.com.myapplication.RegisterDialog;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.board.type.BoardDetailType;
import steam.appjam.sopt.com.myapplication.board.type.ReplyContent;
import steam.appjam.sopt.com.myapplication.board.type.ReplyType;
import steam.appjam.sopt.com.myapplication.board.type.Reply_register;
import steam.appjam.sopt.com.myapplication.cardview.MyData;
import steam.appjam.sopt.com.myapplication.favorite.FavoriteSpace;
import steam.appjam.sopt.com.myapplication.login.model.User;
import steam.appjam.sopt.com.myapplication.login.view.LoginActivity;
import steam.appjam.sopt.com.myapplication.network.NetworkService;
import steam.appjam.sopt.com.myapplication.profile.view.ProfileActivity;

public class BoardDetail extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout mypage;
    private LinearLayout bookmark;
    private LinearLayout board;

    private LoginDialog dialog_login;

    private ScrollView contentScrollView ;
    Button btn_register;
    EditText edit_register;

    ApplicationController api;
    private Toolbar toolbar;

    private TextView title;
    private TextView content;
    private TextView nick;
    private TextView date;
    private TextView type;
    private TextView region;
    private ImageView thumnail;
    private EditText replyContent;


    private String LoginUserID;
    private ListView listviewReply;

    private List<BoardDetailItem> item;
    private NetworkService networkService;

    private DetailAdater reply_adater;

    private RegisterDialog dialog_Register; //경현이형수정했어요



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);
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



        contentScrollView = (ScrollView)findViewById(R.id.contentScrollView);

        mypage = (LinearLayout)findViewById(R.id.nav_mypage);
        bookmark = (LinearLayout)findViewById(R.id.nav_favorite);
        board = (LinearLayout)findViewById(R.id.nav_board);

        thumnail = (ImageView)findViewById(R.id.userThumnail);

        title = (TextView)findViewById(R.id.BoardTitle);
        content = (TextView)findViewById(R.id.BoardContent);
        nick = (TextView)findViewById(R.id.tv_name);
        date = (TextView)findViewById(R.id.tv_date);
        type = (TextView)findViewById(R.id.tv_type);
        region = (TextView)findViewById(R.id.tv_region);
        replyContent = (EditText)findViewById(R.id.edit_register);

        mypage.setOnClickListener(navEvent);
        bookmark.setOnClickListener(navEvent);
        board.setOnClickListener(navEvent);



        contentScrollView.scrollTo(0,0);
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

            LoginUserID = api.getLoginUser().user_id;

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



        Intent intent = getIntent();
        String board_id = intent.getExtras().getString("boardID");


        api = ApplicationController.getInstance();
        networkService = api.getNetworkService();

        Call<BoardDetailType> getDB = networkService.getBoardDetail(board_id);

        getDB.enqueue(new Callback<BoardDetailType>() {
            @Override
            public void onResponse(Response<BoardDetailType> response, Retrofit retrofit) {

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

//                        title = (TextView)findViewById(R.id.BoardTitle);
//                        content = (TextView)findViewById(R.id.BoardContent);
//                        nick = (TextView)findViewById(R.id.tv_name);
//                        date = (TextView)findViewById(R.id.tv_date);
//                        type = (TextView)findViewById(R.id.tv_type);
//                        region = (TextView)findViewById(R.id.tv_region);

                        jObject = getBoardInfo.getJSONObject(0);


                        title.setText("제목 : " +jObject.getString("Post_title"));
                        content.setText("내용 : " +jObject.getString("Post_contents"));
                        nick.setText(jObject.getString("Member_nickname"));
                        date.setText(jObject.getString("Post_datetime"));
                        type.setText(jObject.getString("Post_cate_name"));
                        region.setText(jObject.getString("Post_location_city"));


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


        /**
         * 댓글 가져오기
         *
         */


        listviewReply = (ListView)findViewById(R.id.ListView1);
        item = new ArrayList<BoardDetailItem>();


        Call<ReplyType> getReply = networkService.getReply(board_id);

        getReply.enqueue(new Callback<ReplyType>() {
            @Override
            public void onResponse(Response<ReplyType> response, Retrofit retrofit) {

                if (response.isSuccess()) {
//                    Log.i("MyTag", "확인 : " + "getDataFromServer3");

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(response.body());
                    Log.i("MyTag1", jsonString.toString());

                    // JSONObject를 다룰 때 JSONException을 예외처리해주어야 합니다.
                    try{
                        JSONObject jsonObject = new JSONObject(jsonString);

                        JSONArray getReplyInfo = new JSONArray(jsonObject.getString("data"));

                        JSONObject jObject;
                        String reply_id;
                        String reply_contents;
                        String reply_datetime;
                        String Member_nickname;

                        for(int i = 0; i<getReplyInfo.length();i++){
                            jObject = getReplyInfo.getJSONObject(i);

//                            Log.i("MyTag1", jObject.getString("Space_name"));
                            //        //String name, String location, String address ,String price,String star, int img

                            reply_id = jObject.getString("reply_id");
                            reply_contents = jObject.getString("reply_contents");
                            reply_datetime = jObject.getString("reply_datetime");
                            Member_nickname  = jObject.getString("Member_nickname");

                            //item.add(new BoardDetailItem("닉네임1","내용1","날짜"));
                            item.add(new BoardDetailItem(Member_nickname,reply_contents, reply_datetime));

                        }

                        reply_adater = new DetailAdater(getApplicationContext(),item);
                        listviewReply.setAdapter(reply_adater);

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









        //댓글달기 눌렀을 경우 로그인 안되있으면 로그인 다이얼로그 띄워주기
        btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationController api = new ApplicationController();


                if(replyContent.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),"내용을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(BoardDetail.this, loginEvent,loginCancelEvent);

                    loginParams = dialog_login.getWindow().getAttributes();

                    // Dialog 사이즈 조절 하기
                    loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog_login.getWindow().setAttributes(loginParams);

                    dialog_login.show();
                }
                else {
                    // 로그인 되있으면 댓글 등록되게 하는 부분!



                    WindowManager.LayoutParams registerParams;
                    dialog_Register = new steam.appjam.sopt.com.myapplication.RegisterDialog(BoardDetail.this, registerEvent,registerCancelEvent);

                    registerParams = dialog_Register.getWindow().getAttributes();

                    // Dialog 사이즈 조절 하기
                    registerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    registerParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog_Register.getWindow().setAttributes(registerParams);

                    dialog_Register.show();


                }
            }
        });
    }

    //경현이형수정했어요
    private View.OnClickListener registerEvent = new View.OnClickListener() {
        public void onClick(View v) {
            //다이얼로그에서 확인눌러 글등록시 구현해야할 부분

            Log.i("MyTag222","in");

            Intent intent = getIntent();
            String post_id = intent.getExtras().getString("boardID");

            // 현재 시간을 msec으로 구한다.
            long now = System.currentTimeMillis();
            // 현재 시간을 저장 한다.
            Date date = new Date(now);
            // 시간 포맷으로 만든다.
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strNow = sdfNow.format(date);



            ReplyContent registerReplyItem = new ReplyContent(post_id,replyContent.getText().toString(),LoginUserID);

            item.add(new BoardDetailItem(LoginUserID,replyContent.getText().toString(), strNow));
            reply_adater.notifyDataSetChanged();

            Call<Reply_register> registerReply = networkService.registerReply(registerReplyItem);
            registerReply.enqueue(new Callback<Reply_register>() {
                @Override
                public void onResponse(Response<Reply_register> response, Retrofit retrofit) {

//                    Log.i("MyTag222","Ttt");
                    if(response.isSuccess()) {
                        dialog_Register.dismiss();
                    }
                    else {
                        Log.i("MyTag", ""+ response.code());
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }

            });

            Toast.makeText(getApplicationContext(),"등록 완료!",Toast.LENGTH_SHORT).show();
            replyContent.setText("");
            dialog_Register.dismiss();


        }

    };

    private View.OnClickListener registerCancelEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_Register.dismiss();
        }

    };

    private View.OnClickListener navEvent = new View.OnClickListener(){
        public void onClick(View v){
            ApplicationController api = new ApplicationController();

            int id = v.getId();

            if (id == R.id.nav_mypage) {

                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(BoardDetail.this, loginEvent,loginCancelEvent);

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
                    dialog_login = new LoginDialog(BoardDetail.this, loginEvent,loginCancelEvent);

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

            } else if (id == R.id.nav_board) {
                Intent intent = new Intent(getApplicationContext(), BoardView.class);
                startActivity(intent);
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
//        getMenuInflater().inflate(R.menu.ma, menu);
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
                dialog_login = new LoginDialog(BoardDetail.this, loginEvent,loginCancelEvent);

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
                dialog_login = new LoginDialog(BoardDetail.this, loginEvent,loginCancelEvent);

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
            Intent intent = new Intent(getApplicationContext(), BoardView.class);
            startActivity(intent);
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

