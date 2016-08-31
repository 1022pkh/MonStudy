package steam.appjam.sopt.com.myapplication.favorite;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import steam.appjam.sopt.com.myapplication.LoginDialog;
import steam.appjam.sopt.com.myapplication.MainActivity;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.board.BoardView;
import steam.appjam.sopt.com.myapplication.database.DbOpenHelper;
import steam.appjam.sopt.com.myapplication.database.ItemData;
import steam.appjam.sopt.com.myapplication.login.view.LoginActivity;
import steam.appjam.sopt.com.myapplication.profile.view.ProfileActivity;
import steam.appjam.sopt.com.myapplication.sliding.SlidingDetail;

/**
 * Created by 즤 on 2016-07-02.
 */

public class FavoriteSpace extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private CustomFavoriteAdater adapter;
    private DbOpenHelper mDbOpenHelper;

    private ArrayList<ItemData> itemDatas = null;
    private List<FavoriteSpaceData> item = null;

    private LinearLayout mypage;
    private LinearLayout bookmark;
    private LinearLayout board;

    private LoginDialog dialog_login;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

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

        // DB Create and Open
        mDbOpenHelper = new DbOpenHelper(this);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        itemDatas = mDbOpenHelper.DbSelect();

        if(itemDatas.size() == 0){
            Toast.makeText(getApplicationContext(),"즐겨찾기 항목이 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else{

            item = new ArrayList<FavoriteSpaceData>();
            listView = (ListView) findViewById(R.id.listView);

            for(int i = 0 ; i < itemDatas.size() ; i++)
            {
                String bookmarkId = String.valueOf(itemDatas.get(i).Id);
                String userId = itemDatas.get(i).userId;
                String roomId = itemDatas.get(i).roomId;
                String imgurl = itemDatas.get(i).image;
                String roomName = itemDatas.get(i).roomName;
                String info1 = itemDatas.get(i).content;
                String info2 = itemDatas.get(i).tag;



                String[] tagObject = new String[7];
                tagObject[0] = "#개방된";
                tagObject[1] ="#밀폐된";
                tagObject[2] ="#자유로운";
                tagObject[3] ="#조용한";
                tagObject[4] ="#밝은";
                tagObject[5] ="#편안한";
                tagObject[6] ="#세련된";

                String tagTemp[] = info2.split(",");

                String inputTagItem = "";
                for(int k=0; k<tagTemp.length;k++){

                    if(tagTemp[k].equals("1")){
                        inputTagItem += tagObject[1];
                    }
                    else if(tagTemp[k].equals("2")){
                        inputTagItem += tagObject[2];
                    }
                    else if(tagTemp[k].equals("3")){
                        inputTagItem += tagObject[3];
                    }
                    else if(tagTemp[k].equals("4")){
                        inputTagItem += tagObject[4];
                    }
                    else if(tagTemp[k].equals("5")){
                        inputTagItem += tagObject[5];
                    }
                    else if(tagTemp[k].equals("6")){
                        inputTagItem += tagObject[6];
                    }

                    if(k == tagTemp.length-1)
                        ;
                    else
                        inputTagItem += "  ";
                }




                item.add(new FavoriteSpaceData(bookmarkId,userId,roomId,imgurl,roomName,info1,inputTagItem));
            }

            adapter = new CustomFavoriteAdater(getApplicationContext(), item);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext() , SlidingDetail.class);
                    intent.putExtra("space_id",String.valueOf(item.get(position).roomId));

                    startActivity(intent);
                    finish();
                }
            });

            listView.setAdapter(adapter);
        }


    }

    private View.OnClickListener navEvent = new View.OnClickListener(){
        public void onClick(View v){
            ApplicationController api = new ApplicationController();

            int id = v.getId();

            if (id == R.id.nav_mypage) {

                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(FavoriteSpace.this,loginEvent,loginCancelEvent);

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
                Intent intent = new Intent(getApplicationContext(), FavoriteSpace.class);
                startActivity(intent);

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

        ApplicationController api = new ApplicationController();
        int id = item.getItemId();

        if (id == R.id.nav_mypage) {
            if(!api.getCheck()) {
                WindowManager.LayoutParams loginParams;
                dialog_login = new LoginDialog(FavoriteSpace.this, loginEvent,loginCancelEvent);

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
            ;

        } else if (id == R.id.nav_board) {
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
