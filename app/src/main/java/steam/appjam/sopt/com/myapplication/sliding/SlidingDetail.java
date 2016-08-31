package steam.appjam.sopt.com.myapplication.sliding;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.LoginDialog;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.board.BoardView;
import steam.appjam.sopt.com.myapplication.database.DbOpenHelper;
import steam.appjam.sopt.com.myapplication.database.ItemData;
import steam.appjam.sopt.com.myapplication.favorite.FavoriteSpace;
import steam.appjam.sopt.com.myapplication.login.view.LoginActivity;
import steam.appjam.sopt.com.myapplication.network.NetworkService;
import steam.appjam.sopt.com.myapplication.profile.view.ProfileActivity;
import steam.appjam.sopt.com.myapplication.sliding.PriceTime.PriceTime;
import steam.appjam.sopt.com.myapplication.sliding.PriceTime.PriceTimeItem;
import steam.appjam.sopt.com.myapplication.sliding.information.information;
import steam.appjam.sopt.com.myapplication.sliding.information.informationItem;
import steam.appjam.sopt.com.myapplication.sliding.notice.notice;
import steam.appjam.sopt.com.myapplication.sliding.reviewList.ReviewList;
import steam.appjam.sopt.com.myapplication.sliding.type.SpaceDeteilType;


public class SlidingDetail extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    CustomSwipeAdapter adapter1;                        //이미지 슬라이더를 위한 어댑터
    boolean check_star = false;                      //즐찾 체크
    private double hart;                                       //평점 매기는 친구 - 나중에 디비에서 가져와야 함 int로 지정해서 소수점 짤라쓰

    private ViewPager viewPager;
    private ViewPager viewPagerImage;
    private ImageView star_btn;
    private Boolean ShowBtn = false;

    private DbOpenHelper mDbOpenHelper;

    private LinearLayout mypage;
    private LinearLayout bookmark;
    private LinearLayout board;

    private LoginDialog dialog_login;

    private String name;
    private String latitude;
    private String longitude;
    private String address;
    private String userid;
    private String roomid;
    private String roomName ;
    private String image;
    private String content;
    private String tag;

    FragmentTabHost mTabHost;

    private TextView space_title;
    private TextView space_rating;
    private TextView space_address;
    private TextView space_tag;

    List<informationItem> info = new ArrayList<informationItem>();

    List<PriceTimeItem> price = new ArrayList<PriceTimeItem>();

    private double rating;
    private String telnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_room_detail);
        setContentView(R.layout.activity_room);


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


        /**
         * kh
         */


        mypage = (LinearLayout)findViewById(R.id.nav_mypage);
        bookmark = (LinearLayout)findViewById(R.id.nav_favorite);
        board = (LinearLayout)findViewById(R.id.nav_board);
        star_btn = (ImageView) findViewById(R.id.star);



        space_title = (TextView)findViewById(R.id.roomTitle);
        space_rating = (TextView)findViewById(R.id.spaceRating);
        space_address = (TextView)findViewById(R.id.spaceAddress);
        space_tag = (TextView)findViewById(R.id.inputTag);

        mypage.setOnClickListener(navEvent);
        bookmark.setOnClickListener(navEvent);
        board.setOnClickListener(navEvent);

        ApplicationController api;


        CircleImageView profile;
        TextView userNameView;

        profile = (CircleImageView)findViewById(R.id.profile_image);
        userNameView = (TextView)findViewById(R.id.userName);


        /**
         * intent로 spaceid를 받아올것임.
         */

        Intent intent = getIntent();

        String space_id = intent.getExtras().getString("space_id");

        Log.i("GetID",space_id);
        roomid = space_id;




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
            userid = api.getLoginUser().user_id;



            //즐겨찾기에 있는 것인지 파악해야함.

            // DB Create and Open
            mDbOpenHelper = new DbOpenHelper(this);
            try {
                mDbOpenHelper.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            ArrayList<ItemData>  itemDatas = mDbOpenHelper.DbSelect();

            if(itemDatas.size() == 0){
                ;
            }
            else{


                for(int i = 0 ; i < itemDatas.size() ; i++)
                {
                    String userId = itemDatas.get(i).userId;
                    String roomId = itemDatas.get(i).roomId;

                    if(roomId.equals(roomid))
                    {
                        star_btn.setBackgroundResource(R.drawable.ic_star);
                        check_star = true;
                        break;
                    }
                }

            }

        }



        api = ApplicationController.getInstance();

        NetworkService networkService = api.getNetworkService();
        Call<SpaceDeteilType> test = networkService.getRoomData(space_id);

        test.enqueue(new Callback<SpaceDeteilType>() {
            @Override
            public void onResponse(Response<SpaceDeteilType> response, Retrofit retrofit) {

                if (response.isSuccess()) {
                    //Log.i("MyTag", "확인 : " + "getDataFromServer3");
//                    view.setProfileTexts(response.body());

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(response.body());

                    Log.i("MyTag1", jsonString.toString());

                    // JSONObject를 다룰 때 JSONException을 예외처리해주어야 합니다.
                    try{
                        JSONObject jsonObject = new JSONObject(jsonString);
                        jsonObject = jsonObject.getJSONObject("data");

                        Log.i("MyTag1", jsonObject.getString("space_info"));


                        JSONArray getSpaceInfo = new JSONArray(jsonObject.getString("space_info"));
                        JSONArray getSdInfo = new JSONArray(jsonObject.getString("sd_info"));
                        JSONArray getReview = new JSONArray(jsonObject.getString("review_list"));

                        /**
                         * 아래와 같이 출력해서 필요한 곳에 setText()로 넣어주면됨.
                         */
                        JSONObject jObject = getSpaceInfo.getJSONObject(0);
//                        Log.i("MyTag1", jObject.getString("Space_notice"));

                        name = jObject.getString("Space_name");
                        latitude = jObject.getString("Space_latitude");
                        longitude = jObject.getString("Space_longitude");
                        address = jObject.getString("Space_address");
                        image = jObject.getString("Space_pic");

                        content = jObject.getString("Space_location_si")+" "+jObject.getString("Space_location_gu");
                        tag = jObject.getString("Space_tag");

                        telnum = jObject.getString("Space_cellNum");

                        space_title.setText(name);
                        space_address.setText(address);
                        space_rating.setText(jObject.getString("Space_rating_avg"));


                        String[] tagObject = new String[7];
                        tagObject[0] = "#개방된";
                        tagObject[1] ="#밀폐된";
                        tagObject[2] ="#자유로운";
                        tagObject[3] ="#조용한";
                        tagObject[4] ="#밝은";
                        tagObject[5] ="#편안한";
                        tagObject[6] ="#세련된";

                        String tagTemp[] = tag.split(",");

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

                        space_tag.setText(inputTagItem);



                        hart = Double.parseDouble(jObject.getString("Space_rating_avg"));

                        rating= Double.parseDouble(jObject.getString("Space_rating_avg"));
//평점을 시각화해주는 하트~
                        ImageView hart01 = (ImageView) findViewById(R.id.hart01);
                        ImageView hart02 = (ImageView) findViewById(R.id.hart02);
                        ImageView hart03 = (ImageView) findViewById(R.id.hart03);
                        ImageView hart04 = (ImageView) findViewById(R.id.hart04);
                        ImageView hart05 = (ImageView) findViewById(R.id.hart05);


                        if (rating < 1) {

                            if(Math.round(rating) > 0.5 )
                                hart01.setBackgroundResource(R.drawable.ic_heart_big_half);
                            else
                                hart01.setBackgroundResource(R.drawable.ic_heart_big_blank);

                        }

                        else if (rating < 2) {
                            hart01.setBackgroundResource(R.drawable.ic_heart_big);

                            if(Math.round(rating) > 1.5 )
                                hart02.setBackgroundResource(R.drawable.ic_heart_big_half);
                            else
                                hart02.setBackgroundResource(R.drawable.ic_heart_big_blank);

                            hart03.setBackgroundResource(R.drawable.ic_heart_big_blank);
                            hart04.setBackgroundResource(R.drawable.ic_heart_big_blank);
                            hart05.setBackgroundResource(R.drawable.ic_heart_big_blank);

                        }

                        else if (rating < 3) {
                            hart01.setBackgroundResource(R.drawable.ic_heart_big);
                            hart02.setBackgroundResource(R.drawable.ic_heart_big);

                            if(Math.round(rating) > 2.5 )
                                hart03.setBackgroundResource(R.drawable.ic_heart_big_half);
                            else
                                hart03.setBackgroundResource(R.drawable.ic_heart_big_blank);
                        }

                        else if (rating < 4) {
                            hart01.setBackgroundResource(R.drawable.ic_heart_big);
                            hart02.setBackgroundResource(R.drawable.ic_heart_big);
                            hart03.setBackgroundResource(R.drawable.ic_heart_big);

                            if(Math.round(rating) > 3.5 )
                                hart04.setBackgroundResource(R.drawable.ic_heart_big_half);
                            else
                                hart04.setBackgroundResource(R.drawable.ic_heart_big_blank);

                            hart05.setBackgroundResource(R.drawable.ic_heart_big_blank);
                        }


                        else if (rating < 5) {
                            hart01.setBackgroundResource(R.drawable.ic_heart_big);
                            hart02.setBackgroundResource(R.drawable.ic_heart_big);
                            hart03.setBackgroundResource(R.drawable.ic_heart_big);
                            hart04.setBackgroundResource(R.drawable.ic_heart_big);

                            if(Math.round(rating) > 4.5 )
                                hart05.setBackgroundResource(R.drawable.ic_heart_big_half);
                            else
                                hart05.setBackgroundResource(R.drawable.ic_heart_big_blank);
                        }

                        else if (rating == 5){
                            hart01.setBackgroundResource(R.drawable.ic_heart_big);
                            hart02.setBackgroundResource(R.drawable.ic_heart_big);
                            hart03.setBackgroundResource(R.drawable.ic_heart_big);
                            hart04.setBackgroundResource(R.drawable.ic_heart_big);
                            hart05.setBackgroundResource(R.drawable.ic_heart_big);
                        }

                        for(int i=0;i<getSdInfo.length();i++){
                            jObject = getSdInfo.getJSONObject(i);

                            String sdname = jObject.getString("SD_roomName");
                            String sdimg = jObject.getString("SD_pic");
                            String maxCap = "최대 인원 : " + jObject.getString("SD_maxCap") +"명 수용 가능";
                            String minTime = "최소 이용시간 : "+jObject.getString("SD_minTime") + "시간";
                            String useprice = jObject.getString("SD_price_weekday") +"/"+jObject.getString("SD_price_weekend");


                            info.add(new informationItem(sdimg,sdname, maxCap, minTime, R.drawable.img1));
                            price.add(new PriceTimeItem(sdname, useprice));
                        }





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

//        Log.i("Test",String.valueOf(test));


        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        Bundle bundle = new Bundle();
        bundle.putString("spaceid", roomid);

        mTabHost.addTab(mTabHost.newTabSpec("a").setIndicator(""), information.class, bundle);

        bundle = new Bundle();
        bundle.putString("spaceid", roomid);
        mTabHost.addTab(mTabHost.newTabSpec("b").setIndicator(""), PriceTime.class, bundle);

        bundle = new Bundle();
        bundle.putString("spaceid", roomid);
        mTabHost.addTab(mTabHost.newTabSpec("c").setIndicator(""), notice.class, bundle);

        bundle = new Bundle();
        bundle.putString("spaceid", roomid);
        mTabHost.addTab(mTabHost.newTabSpec("d").setIndicator(""), ReviewList.class, bundle);


        mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tap_red_1);
        mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tap_gray_2);
        mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tap_gray_3);
        mTabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.tap_gray_4);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if ("a".equals(tabId)) {
                    mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tap_red_1);
                    mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tap_gray_2);
                    mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tap_gray_3);
                    mTabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.tap_gray_4);
                }
                else if("b".equals(tabId))
                {
                    mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tap_gray_1);
                    mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tap_red_2);
                    mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tap_gray_3);
                    mTabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.tap_gray_4);


                }
                else if("c".equals(tabId))
                {
                    mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tap_gray_1);
                    mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tap_gray_2);
                    mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tap_red_3);
                    mTabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.tap_gray_4);
                }
                else if("d".equals(tabId))
                {
                    mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tap_gray_1);
                    mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tap_gray_2);
                    mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tap_gray_3);
                    mTabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.tap_red_4);
                }
            }
        });



        // DB Create and Open
        mDbOpenHelper = new DbOpenHelper(this);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ScrollView stickyScrollView = (ScrollView)findViewById(R.id.ssv_scroll);

        if (stickyScrollView != null) {
            stickyScrollView.smoothScrollTo(0,0);
        }




        //이미지 슬라이더를 위한 어댑터
        viewPagerImage =(ViewPager)findViewById(R.id.my_viewpager);
        adapter1 = new CustomSwipeAdapter(this);
        viewPagerImage.setAdapter(adapter1);

        star_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                ApplicationController api = new ApplicationController();
                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(SlidingDetail.this, loginEvent, loginCancelEvent);

                    loginParams = dialog_login.getWindow().getAttributes();

                    // Dialog 사이즈 조절 하기
                    loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog_login.getWindow().setAttributes(loginParams);

                    dialog_login.show();
                }
                else{
                    if (check_star == false) {
                        star_btn.setBackgroundResource(R.drawable.ic_star);
                        check_star = true;
                        // DB에 즐겨찾기 등록.
//                        String userid = "test";
//                        String roomid = "1";
//                        String roomName = "창조룸";
//                        String image = "https://scloud.pstatic.net/20160520_213/1463723615821mfXjf_JPEG/KakaoTalk_Photo_2016-05-20-14-52-59_91.jpeg?type=m1280_1280";
//                        String content = "최대 6명";
//                        String tag = "#조용함";

                        mDbOpenHelper.DbInsert(userid, roomid, name ,image,content,tag);
                        Toast.makeText(getApplicationContext(),"즐겨찾기 등록",Toast.LENGTH_SHORT).show();

                    } else {
                        star_btn.setBackgroundResource(R.drawable.ic_star_blank);
                        check_star = false;

                        ArrayList<ItemData> itemDatas = mDbOpenHelper.DbSelect();

                        if(itemDatas.size() == 0){
                            ;
                        }
                        else {

                            for(int i = 0 ; i < itemDatas.size() ; i++)
                            {
                                //해당 페이지의 스터디룸
                                String findRoomName = name;
                                int bookmarkId = itemDatas.get(i).Id;
//                            Log.i("test",itemDatas.get(i).roomName +"/"+findRoomName);
                                if(itemDatas.get(i).roomName .equals( findRoomName)){
                                    mDbOpenHelper.DbDelete(String.valueOf(bookmarkId));
//                                Log.i("test",String.valueOf(bookmarkId));
                                    break;
                                }

                            }
                        }


                        Toast.makeText(getApplicationContext(),"즐겨찾기 해제",Toast.LENGTH_SHORT).show();
                    }
                }




            }
        });

        //버튼 설정 - 지도,문의하기,후기 버튼
        Button map_btn = (Button) findViewById(R.id.mapButton);
        Button inq_btn = (Button) findViewById(R.id.inquiresButton);
        ImageButton review_btn = (ImageButton) findViewById(R.id.reviewButton);

        assert map_btn != null;
        assert inq_btn != null;
        assert review_btn != null;

        map_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);  //수정 필요
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("address",address);
                intent.putExtra("name",name);

                startActivity(intent);

                overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);

            }
        });


        inq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Inquires dialog = Inquires.newInstance(telnum);

                dialog.show(getFragmentManager(), "title");


            }
        });

        review_btn.setOnClickListener(new View.OnClickListener() {
            // 이종찬 - 후기작성버튼 누를시 로그인했는지 안했는지 판단
            @Override
            public void onClick(View v) {
                ApplicationController api = new ApplicationController();
                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(SlidingDetail.this, loginEvent, loginCancelEvent);

                    loginParams = dialog_login.getWindow().getAttributes();

                    // Dialog 사이즈 조절 하기
                    loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog_login.getWindow().setAttributes(loginParams);

                    dialog_login.show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), Review.class);
                    startActivity(intent);
                }
                //finish();
            }
        });


    }



    private View.OnClickListener navEvent = new View.OnClickListener(){
        public void onClick(View v){
            ApplicationController api = new ApplicationController();

            int id = v.getId();

            if (id == R.id.nav_mypage) {

                if(!api.getCheck()) {
                    WindowManager.LayoutParams loginParams;
                    dialog_login = new LoginDialog(SlidingDetail.this, loginEvent,loginCancelEvent);

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
                    dialog_login = new LoginDialog(SlidingDetail.this, loginEvent,loginCancelEvent);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                dialog_login = new LoginDialog(SlidingDetail.this, loginEvent,loginCancelEvent);

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
                dialog_login = new LoginDialog(SlidingDetail.this, loginEvent,loginCancelEvent);

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
