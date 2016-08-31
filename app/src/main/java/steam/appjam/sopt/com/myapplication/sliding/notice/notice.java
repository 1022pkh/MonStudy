package steam.appjam.sopt.com.myapplication.sliding.notice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.network.NetworkService;
import steam.appjam.sopt.com.myapplication.sliding.type.SpaceDeteilType;


public class notice extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    ApplicationController api;

    public static notice newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        notice fragment = new notice();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_notice, container, false);


        String space_id = getArguments().getString("spaceid");

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
//                        JSONObject jObject = getSpaceInfo.getJSONObject(0);
//
//                        for(int i=0;i<getSdInfo.length();i++){
//                            jObject = getSdInfo.getJSONObject(i);
//
//                            String sdname = jObject.getString("SD_roomName");
//                            String sdimg = jObject.getString("SD_pic");
//                            String maxCap = "최대 인원 : " + jObject.getString("SD_maxCap") +"명 수용 가능";
//                            String minTime = "최소 이용시간 : "+jObject.getString("SD_minTime") + "시간";
//                            String useprice = jObject.getString("SD_price_weekday") +"/"+jObject.getString("SD_price_weekend");
//
//
//                        }
                        JSONObject jObject = getSpaceInfo.getJSONObject(0);

                        TextView textView = (TextView)view.findViewById(R.id.textArea);
                        textView.setText(jObject.getString("Space_notice") + "....\n\n\n자세한 문의는 전화로 주세요");

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



        return view;
    }
}
