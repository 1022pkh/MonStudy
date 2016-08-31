package steam.appjam.sopt.com.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.afterFilter.FilterAdapter;
import steam.appjam.sopt.com.myapplication.afterFilter.ListItem;
import steam.appjam.sopt.com.myapplication.afterFilter.type.FilterType;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.board.BoardView;
import steam.appjam.sopt.com.myapplication.cardview.MyAdapter;
import steam.appjam.sopt.com.myapplication.cardview.MyData;
import steam.appjam.sopt.com.myapplication.database.DbOpenHelper;
import steam.appjam.sopt.com.myapplication.dialog.CustomDialogKind;
import steam.appjam.sopt.com.myapplication.dialog.CustomDialogLocation;
import steam.appjam.sopt.com.myapplication.dialog.CustomDialogPeople;
import steam.appjam.sopt.com.myapplication.favorite.FavoriteSpace;
import steam.appjam.sopt.com.myapplication.gps.GpsInfo;
import steam.appjam.sopt.com.myapplication.login.view.LoginActivity;
import steam.appjam.sopt.com.myapplication.main.type.MainType;
import steam.appjam.sopt.com.myapplication.network.NetworkService;
import steam.appjam.sopt.com.myapplication.profile.view.ProfileActivity;
import steam.appjam.sopt.com.myapplication.sliding.SlidingDetail;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyData> myDataset;

    private ListView listview;
    private SearchView searchView;
    private MenuItem searchItem;

    private LinearLayout locationBtn;
    private LinearLayout kindBtn;
    private LinearLayout peopleBtn;
    private Toolbar toolbar;
    private CheckBox scale;
    private CheckBox price;
    private CheckBox hour;

    private CircleImageView profile;
    private TextView userNameView;
    private CustomDialogLocation dialog_location;
    private CustomDialogKind dialog_kind;
    private CustomDialogPeople dialog_people;

    private String myLocation;
    LocationManager mLocationManager;
    private GpsInfo gps;


    private String chooseAddress = "null";
    private ArrayList<String> checkItem;
    private int checkPerson = 0;

    private int checkArray = 1;
    private Boolean fullHourChk = false;


    //Back 키 두번 클릭 여부 확인
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    private Boolean LoginCheck = false;

    private DbOpenHelper mDbOpenHelper;
    ApplicationController api;
    private LoginDialog dialog_login;
    private TextView kindText;

    private LinearLayout mypage;
    private LinearLayout bookmark;
    private LinearLayout board;
    private List<ListItem> item;
    private   FilterAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#F6D03F"));
        }



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        TextView toolbar_title = ((TextView) findViewById(R.id.main_toolbar_title));
        toolbar_title.setTypeface(Typeface.createFromAsset(getAssets(),"Quicksand_Bold.otf"));
        toolbar_title.setText("MONSTUDY");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        /**
         * kh 2016 6 28
         */



        mypage = (LinearLayout)findViewById(R.id.nav_mypage);
        bookmark = (LinearLayout)findViewById(R.id.nav_favorite);
        board = (LinearLayout)findViewById(R.id.nav_board);


        profile = (CircleImageView)findViewById(R.id.profile_image);
        userNameView = (TextView)findViewById(R.id.userName);

        locationBtn = (LinearLayout) findViewById(R.id.LocationArea);
        kindBtn = (LinearLayout) findViewById(R.id.KindArea);
        peopleBtn = (LinearLayout) findViewById(R.id.PeopleArea);


        mypage.setOnClickListener(navEvent);
        bookmark.setOnClickListener(navEvent);
        board.setOnClickListener(navEvent);

        // DB Create and Open
        mDbOpenHelper = new DbOpenHelper(this);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                WindowManager.LayoutParams params;

                switch (view.getId()) {
                    case R.id.LocationArea:

                        dialog_location = new CustomDialogLocation(MainActivity.this, SearchCurrent,getLocationEvent);

                        params = dialog_location.getWindow().getAttributes();

                        // Dialog 사이즈 조절 하기
                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
                        params.height = WindowManager.LayoutParams.MATCH_PARENT;
                        dialog_location.getWindow().setAttributes(params);

                        dialog_location.show();

                        break;

                    case R.id.KindArea:
                        dialog_kind = new CustomDialogKind(MainActivity.this,checkItem ,getKindEvent);

                        params = dialog_kind.getWindow().getAttributes();

                        // Dialog 사이즈 조절 하기
                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
                        params.height = WindowManager.LayoutParams.MATCH_PARENT;
                        dialog_kind.getWindow().setAttributes(params);
                        dialog_kind.show();
                        break;

                    case R.id.PeopleArea:
                        dialog_people = new CustomDialogPeople(MainActivity.this, checkPerson ,getPeopleEvent);

                        params = dialog_people.getWindow().getAttributes();

                        // Dialog 사이즈 조절 하기
                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
                        params.height = WindowManager.LayoutParams.MATCH_PARENT;
                        dialog_people.getWindow().setAttributes(params);
                        dialog_people.show();

                        break;
                }
            }
        };


        locationBtn.setOnClickListener(onClickListener);
        kindBtn.setOnClickListener(onClickListener);
        peopleBtn.setOnClickListener(onClickListener);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();

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


        NetworkService networkService = api.getNetworkService();

        Call<MainType> getDB = networkService.getMainData();

        getDB.enqueue(new Callback<MainType>() {
            @Override
            public void onResponse(Response<MainType> response, Retrofit retrofit) {

                if (response.isSuccess()) {
//                    Log.i("MyTag", "확인 : " + "getDataFromServer3");

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(response.body());
//                    Log.i("MyTag1", jsonString.toString());

                    // JSONObject를 다룰 때 JSONException을 예외처리해주어야 합니다.
                    try{
                        JSONObject jsonObject = new JSONObject(jsonString);
//                        jsonObject = jsonObject.getJSONObject("data");

                        JSONArray getMainInfo = new JSONArray(jsonObject.getString("data"));

//                        Log.i("MyTag1", jsonObject.toString());
                        JSONObject jObject;

                        String sp_id;
                        String name;
                        String m_address;
                        String location;
                        String min_price;
                        String rating;
                        String image;

                        for(int i = 0; i<getMainInfo.length();i++){
                            jObject = getMainInfo.getJSONObject(i);

//                            Log.i("MyTag1", jObject.getString("Space_name"));
                            //        //String name, String location, String address ,String price,String star, int img

                            sp_id = jObject.getString("Space_id");
                            name = jObject.getString("Space_name");
                            m_address = jObject.getString("Space_location_si");
                            location  = jObject.getString("Space_location_si") +jObject.getString("Space_location_gu");
                            min_price = jObject.getString("Min_price") +"원 ~ ";
                            rating = jObject.getString("Review_rating_avg");
                            image  =  jObject.getString("Space_pic") ;

//                            Log.i("MyTag",jObject.getString("Review_rating_avg"));
                            Log.i("test",image);

//                            String ttt = "https://scloud.pstatic.net/20160520_213/1463723615821mfXjf_JPEG/KakaoTalk_Photo_2016-05-20-14-52-59_91.jpeg?type=m1280_1280";
                            //String name, String location, String address ,String price,String star, int img


                            myDataset.add(new MyData(sp_id,name, m_address ,location, min_price, rating, image));

                        }


                        mAdapter = new MyAdapter(myDataset);
                        mRecyclerView.setAdapter(mAdapter);


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




        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy < 0) {
                    // Recycle view scrolling up...

                } else if (dy > 0) {
                    // Recycle view scrolling down...

//                    int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
//                    int totalItemCount = mLayoutManager.getItemCount();

                    int scrollOffset = mRecyclerView.computeVerticalScrollOffset();
                    int scrollExtend = mRecyclerView.computeVerticalScrollExtent();
                    int scrollRange = mRecyclerView.computeVerticalScrollRange();

//                    Log.i("test","scrollOffset "+String.valueOf(scrollOffset));
//                    Log.i("test","scrollExtend "+String.valueOf(scrollExtend));
//                    Log.i("test","scrollRange "+String.valueOf(scrollRange));

                    if (scrollOffset + scrollExtend == scrollRange || scrollOffset + scrollExtend - 1 == scrollRange) {

//                    if (lastVisibleItem >= totalItemCount - 10) {

//                        myDataset.add(new MyData("#추가1", "경기도 수원시 영통구", "1500원 / 1시간", "평점 4.5", R.drawable.img1));
//                        myDataset.add(new MyData("#추가2", "경기도 수원시 영통구", "2500원 / 1시간", "평점 4.5", R.drawable.img2));
//                        myDataset.add(new MyData("#추가3", "경기도 수원시 영통구", "3500원 / 1시간", "평점 4.5", R.drawable.img3));
//
//                        mAdapter.notifyDataSetChanged();

                    }


                }
            }
        });

    }


    private void ChangeMainArea(){
        LinearLayout inflatedLayout = (LinearLayout)findViewById(R.id.mainArea);
        inflatedLayout.removeAllViews();

        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflated_Layout.xml로 구성한 레이아웃을 inflatedLayout 영역으로 확장
        inflater.inflate(R.layout.activity_main_filter, inflatedLayout);

        item = new ArrayList<ListItem>();
        listview = (ListView)findViewById(R.id.ListView1);

        scale = (CheckBox)findViewById(R.id.scaleChk);
        price = (CheckBox)findViewById(R.id.priceChk);
        hour = (CheckBox)findViewById(R.id.hourChk);



        NetworkService networkService = api.getNetworkService();

        //@GET("/search/{si}/{gu}/{scate}/{cap}/{order}")
        String si;
        String gu;


        //지역 선택되어 있는지
        if(chooseAddress == "null")
        {
            si = "*";
            gu = "*";
        }
        else{

            si = "서울시";
            gu = chooseAddress + "구";


        }

        //업종 선택되어 있는지
//        String[] scate = new String[10];
        String scate = "";

        if(checkItem != null){


            for (int i = 0; i<checkItem.size();i++) {

                if(checkItem.get(i).equals("all")){
                    scate = "*";
                    break;
                }
                else{
                    /**
                     * ChkName.add("회의실");
                     ChkName.add("세미나실");
                     ChkName.add("다목적실");
                     ChkName.add("스터디");
                     ChkName.add("연습");
                     ChkName.add("카페");

                     */
                    if(checkItem.get(i).equals("회의실"))
                        scate += "1" ;
                    else if(checkItem.get(i).equals("세미나실"))
                        scate += "2" ;
                    else if(checkItem.get(i).equals("다목적실"))
                        scate += "3" ;
                    else if(checkItem.get(i).equals("스터디"))
                        scate += "4" ;
                    else if(checkItem.get(i).equals("연습"))
                        scate += "5" ;
                    else if(checkItem.get(i).equals("카페"))
                        scate += "6" ;

                    if(i == checkItem.size()-1)
                        ;
                    else
                        scate += ",";
                }

            }



        }
        else
            scate = "*";

//
//        Log.i("Tag",gu[0]);
        Log.i("Tag",scate);

        //인원 선택되어 있는지
        String cap;
        if(checkPerson == 0)
            cap = "*";
        else
            cap = String.valueOf(checkPerson);

        //검색조건
        //checkArray = 1;
        //private Boolean fullHourChk = false;
        //
        String order = "1";
        if(checkArray == 1)
            order = "1";

        if(checkArray == 2)
            order = "2";

        if(checkArray == 1 && fullHourChk == true)
            order = "3";

        if(checkArray == 2 && fullHourChk == true)
            order = "4";


//        Log.i("get",si+"/"+gu+"/"+scate+"/"+cap+"/"+order);

        Call<FilterType> getFilter = networkService.getSearchRoom(si,gu,scate,cap,order);

        getFilter.enqueue(new Callback<FilterType>() {
            @Override
            public void onResponse(Response<FilterType> response, Retrofit retrofit) {

                if (response.isSuccess()) {
//                    Log.i("MyTag", "확인 : " + "getDataFromServer3");

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(response.body());
//                    Log.i("MyTag1", jsonString.toString());

                    // JSONObject를 다룰 때 JSONException을 예외처리해주어야 합니다.
                    try{
                        JSONObject jsonObject = new JSONObject(jsonString);
//                        jsonObject = jsonObject.getJSONObject("data");

                        JSONArray getFilterInfo = new JSONArray(jsonObject.getString("data"));

                        Log.i("MyTag1", jsonObject.toString());
                        JSONObject jObject;

                        String Space_id;
                        String Min_price;
                        String Space_name;
                        String Space_pic;
                        String Space_openhour;
                        String Space_closehour;
                        String Review_rating_avg;
                        String time;

                        for(int i = 0; i<getFilterInfo.length();i++){
                            jObject = getFilterInfo.getJSONObject(i);

                            Space_id = jObject.getString("Space_id");
                            Space_name = jObject.getString("Space_name");
                            Min_price = jObject.getString("Min_price") +"원 ~ ";
                            Review_rating_avg = jObject.getString("Review_rating_avg");
                            Space_pic  =  jObject.getString("Space_pic") ;
                            Space_openhour =  jObject.getString("Space_openhour") ;
                            Space_closehour =  jObject.getString("Space_closehour") ;

                            time = Space_openhour +":00~"+Space_closehour+":00";

                            item.add(new ListItem(Space_id,Space_pic, Space_name, Min_price, time, Review_rating_avg));
                            Log.i("MyTag","in");
                        }

                        if(getFilterInfo.length()==0)
                            Toast.makeText(getApplicationContext(),"죄송합니다. 검색결과가 없습니다.",Toast.LENGTH_SHORT).show();

                        else{

                            Log.i("MyTag",String.valueOf(item.size()));

                            adapter = new FilterAdapter(getApplicationContext(),item);
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    Intent intent = new Intent(getApplicationContext() , SlidingDetail.class);
                                    intent.putExtra("space_id",String.valueOf(item.get(position).getId()));

                                    startActivity(intent);

                                }
                            });


                            listview.setAdapter(adapter);


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




//        String image = "https://scloud.pstatic.net/20160520_213/1463723615821mfXjf_JPEG/KakaoTalk_Photo_2016-05-20-14-52-59_91.jpeg?type=m1280_1280";
        String image = "http://52.78.8.18:5000/image/6.png";
//String id,String image, String name, String price, String time, String grade
//        item.add(new ListItem("1",image, "힐스터디", "1300원~", "09:00~20:00", "4.4"));
//        item.add(new ListItem("2",image,"쉐어원 라운지","1000원~","09:00~20:00","2.3"));
//        item.add(new ListItem("3",image,"히어로스터디","500원~","09:00~20:00","3.3"));









        if(checkArray == 1)
            scale.setChecked(true);
        else
            price.setChecked(true);

        if(fullHourChk)
            hour.setChecked(true);
        else
            hour.setChecked(false);


        scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scale.isChecked()){
                    checkArray = 1;
                    price.setChecked(false);
                    ChangeMainArea();
                }
                else{
                    checkArray = 2;
                    price.setChecked(true);
                    ChangeMainArea();
                }
            }
        });

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(price.isChecked()){
                    checkArray = 2;
                    scale.setChecked(false);
                    ChangeMainArea();
                }
                else{
                    checkArray = 1;
                    scale.setChecked(true);
                    ChangeMainArea();
                }
            }
        });

        hour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    fullHourChk = true;
                    ChangeMainArea();
                }
                else{
                    fullHourChk = false;
                    ChangeMainArea();
                }
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
                    dialog_login = new LoginDialog(MainActivity.this, loginEvent,loginCancelEvent);

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
                    dialog_login = new LoginDialog(MainActivity.this, loginEvent,loginCancelEvent);

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


    private View.OnClickListener SearchCurrent = new View.OnClickListener() {
        public void onClick(View v) {

            gps = new GpsInfo(MainActivity.this);
            String myPosition = null;

            // GPS 사용유무 가져오기
            if (gps.isGetLocation()) {
                Location myLocation = getLastKnownLocation();
//            Log.i("current","Latitude : "+String.valueOf(myLocation.getLatitude()));
//            Log.i("current","Longitude : "+String.valueOf(myLocation.getLongitude()));

                if(myLocation == null)
                    return ;
                double currentLatitude = myLocation.getLatitude();
                double currentLongitude = myLocation.getLongitude();


                Geocoder gc = new Geocoder(getApplicationContext(), Locale.KOREAN);

                try{
                    List<Address> addresses = gc.getFromLocation(currentLatitude,currentLongitude,1);
                    StringBuilder sb = new StringBuilder();

                    if(addresses.size() > 0){
                        Address address = addresses.get(0);

                        String addressTemp = address.getAddressLine(0);

                        String[] myLocationAddress = addressTemp.split(" ");
                        //현재 도로명주소로 주고 있음...
                        myPosition = myLocationAddress[1]+" "+myLocationAddress[2]+" "+myLocationAddress[3];
//                        Log.i("current",myPosition);


//                        LinearLayout parent = (LinearLayout)findViewById(R.id.LocationArea);
                        locationBtn.removeAllViews();


                        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        // Inflated_Layout.xml로 구성한 레이아웃을 inflatedLayout 영역으로 확장
                        inflater.inflate(R.layout.main_search_location, locationBtn);


                        Toast.makeText(getApplicationContext(),"현재 위치 : "+myPosition,Toast.LENGTH_SHORT).show();

                        dialog_location.dismiss();
                        ChangeMainArea();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"GPS 신호가 약합니다. 직접 선택해주세요! ",Toast.LENGTH_SHORT).show();

                }

            } else {
                // GPS 를 사용할수 없으므로
                gps.showSettingsAlert();
            }

            myLocation = myPosition;


        }


    };

    private View.OnClickListener getLocationEvent = new View.OnClickListener() {
        public void onClick(View v) {

//            LinearLayout parent = (LinearLayout)findViewById(R.id.LocationArea);
            chooseAddress = dialog_location.giveAddress();

            if(chooseAddress == "null") {
                Toast.makeText(getApplicationContext(),"지역을 선택해주세요.",Toast.LENGTH_SHORT).show();
            }
            else {

                locationBtn.removeAllViews();


                locationBtn.setGravity(Gravity.CENTER);
                LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Inflated_Layout.xml로 구성한 레이아웃을 inflatedLayout 영역으로 확장
                inflater.inflate(R.layout.main_search_location, locationBtn);



//            Log.i("tttt",test);


                TextView locationText = (TextView) findViewById(R.id.ChooseLocation);
                String address = "서울시\n" + chooseAddress;

                locationText.setText(address);


                dialog_location.dismiss();
                ChangeMainArea();
//                Toast.makeText(getApplicationContext(),"선택한 위치 : 서울시 " +chooseAddress,Toast.LENGTH_SHORT).show();


            }
        }

    };

    private View.OnClickListener getKindEvent = new View.OnClickListener() {
        public void onClick(View v) {

            checkItem = dialog_kind.CheckKind();

            if(checkItem != null){
                kindBtn.removeAllViews();


                kindBtn.setGravity(Gravity.CENTER);
                LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Inflated_Layout.xml로 구성한 레이아웃을 inflatedLayout 영역으로 확장
                inflater.inflate(R.layout.main_search_kind, kindBtn);

                kindText = (TextView)findViewById(R.id.ChooseKind);
//            String address = "세미나실\n스터디룸\n다목적실";

//            locationText.setText(address);

                if(checkItem.size() > 3) {
                    Toast.makeText(getApplicationContext(), "최대 3개의 업종을 선택할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    return ;
                }

//            Log.i("test","gg");

                String temp = "";

                for (int i = 0; i<checkItem.size();i++) {
                    temp += checkItem.get(i) ;
                    if(i != checkItem.size()-1)
                    {
                        temp +="\n";
                    }
                }


                if(temp.equals("all"))
                    temp = "전체";

                if(temp == "" || temp == null)
                    return ;
                else
                    kindText.setText(temp);


                dialog_kind.dismiss();
                ChangeMainArea();
//                Toast.makeText(getApplicationContext(),"선택된 업종 : "+temp,Toast.LENGTH_SHORT).show();

            }
            else{
//                if(checkItem.size() == 0) {
                Toast.makeText(getApplicationContext(), "1개 이상의 업종을 선택해주세요.", Toast.LENGTH_SHORT).show();
//                    return ;
//                }
            }


        }

    };

    private View.OnClickListener getPeopleEvent = new View.OnClickListener() {
        public void onClick(View v) {

            checkPerson = dialog_people.getPerson();
            if(checkPerson == 0 ){
                Toast.makeText(getApplicationContext(),"최소 1명입니다."+checkPerson,Toast.LENGTH_SHORT).show();
                return ;
            }
            else{

                peopleBtn.removeAllViews();


                peopleBtn.setGravity(Gravity.CENTER);
                LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Inflated_Layout.xml로 구성한 레이아웃을 inflatedLayout 영역으로 확장
                inflater.inflate(R.layout.main_search_people, peopleBtn);


                TextView peopleText = (TextView)findViewById(R.id.ChoosePeople);



                peopleText.setText(String.valueOf(checkPerson)+"명");

//                Toast.makeText(getApplicationContext(),"선택된 인원 : "+checkPerson+"명",Toast.LENGTH_SHORT).show();

                dialog_people.dismiss();
                ChangeMainArea();
            }


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


    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }



    @Override
    public void onBackPressed() {
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            /**
             * Back키 두번 연속 클릭 시 앱 종료
             */

            if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
                super.onBackPressed();
            }
            else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(),"뒤로 가기 키을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
            }
        }
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
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//
//            Toast.makeText(getApplicationContext(),"Searching",Toast.LENGTH_SHORT).show();
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
                dialog_login = new LoginDialog(MainActivity.this, loginEvent,loginCancelEvent);

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
                dialog_login = new LoginDialog(MainActivity.this, loginEvent,loginCancelEvent);

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


    public void sendSucceed() {
        Toast.makeText(this, "전송완료!", Toast.LENGTH_LONG).show();

    }

    public void sendFailure() {
        Toast.makeText(this, "전송실패!", Toast.LENGTH_LONG).show();
    }


    public void networkError() {
        ErrorController errorController = new ErrorController(this.getApplicationContext());
        errorController.notifyNetworkError();
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
