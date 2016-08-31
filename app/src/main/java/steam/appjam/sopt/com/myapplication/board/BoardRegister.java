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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.LoginDialog;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.RegisterDialog;
import steam.appjam.sopt.com.myapplication.SoftKeyboardLsnedLinearLayout;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.board.type.BoardRegisterType;
import steam.appjam.sopt.com.myapplication.board.type.Board_register;
import steam.appjam.sopt.com.myapplication.board.type.ReplyContent;
import steam.appjam.sopt.com.myapplication.board.type.Reply_register;
import steam.appjam.sopt.com.myapplication.favorite.FavoriteSpace;
import steam.appjam.sopt.com.myapplication.login.view.LoginActivity;
import steam.appjam.sopt.com.myapplication.network.NetworkService;
import steam.appjam.sopt.com.myapplication.profile.view.ProfileActivity;


public class BoardRegister extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout mypage;
    private LinearLayout bookmark;
    private LinearLayout board;

    private LoginDialog dialog_login;

    private Toolbar toolbar;

    private BackDialog dialog_back; // 종찬수정된부분

    Button btn_register; //경현이형수정했어요
    private RegisterDialog dialog_Register; //경현이형수정했어요

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_register);

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

        //종찬수정된부분
        Spinner dropdownStudy = (Spinner)findViewById(R.id.spn_studyType);
        String[] itemStudy = new String[]{"공부","취미공유","취업준비","세미나"};
        ArrayAdapter<String> adapterStudy = new ArrayAdapter<String>(this, R.layout.spinner_text_style, itemStudy);
        adapterStudy.setDropDownViewResource(R.layout.spinner_text_style);
        dropdownStudy.setAdapter(adapterStudy);

        Spinner dropdownRegion = (Spinner)findViewById(R.id.spn_region);
        String[] itemRegion = new String[]{"경기","서울","수원","영통"};
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, R.layout.spinner_text_style, itemRegion);
        adapterRegion.setDropDownViewResource(R.layout.spinner_text_style);
        dropdownRegion.setAdapter(adapterRegion);






        SoftKeyboardLsnedLinearLayout layout = (SoftKeyboardLsnedLinearLayout) findViewById(R.id.myLayout);
        final LinearLayout Linear_study = (LinearLayout)findViewById(R.id.Linear_study);
        final LinearLayout Linear_Region = (LinearLayout)findViewById(R.id.Linear_Region);
        layout.addSoftKeyboardLsner(new SoftKeyboardLsnedLinearLayout.SoftKeyboardLsner() {
            @Override
            public void onSoftKeyboardShow() {
                Linear_study.setVisibility(View.GONE);
                Linear_Region.setVisibility(View.GONE);
            }

            @Override
            public void onSoftKeyboardHide() {
                Linear_study.setVisibility(View.VISIBLE);
                Linear_Region.setVisibility(View.VISIBLE);
            }
        });

        //경현이형수정했어요
        btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationController api = new ApplicationController();
                    WindowManager.LayoutParams registerParams;
                    dialog_Register = new RegisterDialog(BoardRegister.this, registerEvent,registerCancelEvent);

                    registerParams = dialog_Register.getWindow().getAttributes();

                    // Dialog 사이즈 조절 하기
                    registerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    registerParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog_Register.getWindow().setAttributes(registerParams);

                    dialog_Register.show();
            }
        });
    }

    //경현이형수정했어요
    private View.OnClickListener registerEvent = new View.OnClickListener() {
        public void onClick(View v) {
            //다이얼로그에서 확인눌러 글등록시 구현해야할 부분

            NetworkService networkService;
            ApplicationController api;

            api = ApplicationController.getInstance();
            networkService = api.getNetworkService();

            /**
             * request.body.Post_title,
             * request.body.Post_contents,
             * request.body.User_id,
             * request.body.Post_location_id,
             * request.body.Post_category_id
             */

            TextView board_title = (TextView)findViewById(R.id.Board_title);
            TextView board_content = (TextView)findViewById(R.id.edt_content);



            Spinner spn_studyType = (Spinner)findViewById(R.id.spn_studyType);
            Spinner spn_region = (Spinner)findViewById(R.id.spn_region);





//            Log.i("tttt",spn_studyType.getId());

            String LoginUserID = api.getLoginUser().user_id;

            String title = board_title.getText().toString();
            BoardRegisterType registerBoardtem
                    = new BoardRegisterType(title,board_content.getText().toString(),LoginUserID,String.valueOf(spn_studyType.getSelectedItemPosition()+1),String.valueOf(spn_region.getSelectedItemPosition()+1));

            Call<Board_register> registerBoard = networkService.RegisterBoard(registerBoardtem);
            registerBoard.enqueue(new Callback<Board_register>() {
                @Override
                public void onResponse(Response<Board_register> response, Retrofit retrofit) {

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



            dialog_Register.dismiss();
            Toast.makeText(getApplicationContext(),"게시물 등록 완료!",Toast.LENGTH_SHORT).show();
            setResult(1);
            finish();
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
                    dialog_login = new LoginDialog(BoardRegister.this, loginEvent,loginCancelEvent);

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
                    dialog_login = new LoginDialog(BoardRegister.this, loginEvent,loginCancelEvent);

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
                ;
            }
        }
    };

    @Override
    public void onBackPressed() {
        //종찬수정된부분
        WindowManager.LayoutParams loginParams;
        dialog_back = new BackDialog(BoardRegister.this, backEvent,backCancleEvent);

        loginParams = dialog_back.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_back.getWindow().setAttributes(loginParams);
        dialog_back.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
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
                dialog_login = new LoginDialog(BoardRegister.this, loginEvent,loginCancelEvent);

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
                dialog_login = new LoginDialog(BoardRegister.this, loginEvent,loginCancelEvent);

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

    private View.OnClickListener backEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_back.dismiss();
            Intent intent = new Intent(getApplicationContext(), BoardView.class);
            startActivity(intent);
            finish();
        }

    };

    //종찬수정된부분
    private View.OnClickListener backCancleEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_back.dismiss();
        }

    };

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
