package steam.appjam.sopt.com.myapplication.sliding.reviewList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.application.ApplicationController;
import steam.appjam.sopt.com.myapplication.network.NetworkService;
import steam.appjam.sopt.com.myapplication.sliding.ListInScroll;
import steam.appjam.sopt.com.myapplication.sliding.PriceTime.PriceTimeItem;
import steam.appjam.sopt.com.myapplication.sliding.type.SpaceDeteilType;

public class ReviewList extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    ApplicationController api;

    public static ReviewList newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ReviewList fragment = new ReviewList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_information, container, false);

        final List<ReviewListItem> item = new ArrayList<ReviewListItem>();
        final ListInScroll listview = (ListInScroll)view.findViewById(R.id.listviewInfo);



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

                        Log.i("My2", jsonObject.getString("review_list"));


                        JSONArray getSpaceInfo = new JSONArray(jsonObject.getString("space_info"));
                        JSONArray getSdInfo = new JSONArray(jsonObject.getString("sd_info"));
                        JSONArray getReview = new JSONArray(jsonObject.getString("review_list"));

                        /**
                         * 아래와 같이 출력해서 필요한 곳에 setText()로 넣어주면됨.
                         */
                        JSONObject jObject ;

                        for(int i=0;i<getReview.length();i++){
                            jObject = getReview.getJSONObject(i);


                            String rating_star = jObject.getString("Review_rating");
                            String Member_nickname = jObject.getString("Member_nickname");
                            String Review_contents = jObject.getString("Review_contents");
                            String Review_datetime = jObject.getString("Review_datetime");
                            String SD_roomName = jObject.getString("SD_roomName");


                            item.add(new ReviewListItem(Member_nickname, Review_datetime, SD_roomName, Review_contents,rating_star));

                        }


                        ReviewListAdapter adapter = new ReviewListAdapter(getActivity().getApplicationContext(),item);
                        listview.setAdapter(adapter);

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


//
//        item.add(new ReviewListItem("이름", "날짜", "스터디룸", "내용"));
//        item.add(new ReviewListItem("이름", "날짜", "스터디룸", "내용"));
//        item.add(new ReviewListItem("이름", "날짜", "스터디룸", "내용"));


        return view;
    }
}


class ReviewListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    List<ReviewListItem> arSrc;

    //생성자
    public ReviewListAdapter(Context context, List<ReviewListItem> arItem){
        //인플레이트 준비를 합니다.
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arSrc = arItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arSrc.size();
    }

    @Override
    public ReviewListItem getItem(int position) {
        // TODO Auto-generated method stub
        return arSrc.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        // TODO Auto-generated method stub

        if(convertView == null){
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.listview_review, parent, false);

            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.room = (TextView)convertView.findViewById(R.id.room);
            holder.content = (TextView)convertView.findViewById(R.id.content);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.name.setText(arSrc.get(position).getName());
        holder.date.setText(arSrc.get(position).getDate());
        holder.room.setText(arSrc.get(position).getRoom());
        holder.content.setText(arSrc.get(position).getContent());

        String statRating =arSrc.get(position).getStar();

        ImageView item1 = (ImageView)convertView.findViewById(R.id.rating1);
        ImageView item2 = (ImageView)convertView.findViewById(R.id.rating2);
        ImageView item3 = (ImageView)convertView.findViewById(R.id.rating3);
        ImageView item4 = (ImageView)convertView.findViewById(R.id.rating4);
        ImageView item5 = (ImageView)convertView.findViewById(R.id.rating5);

        if(statRating.equals("0")){
            ;
        }
        else if(statRating.equals("1")){
            item1.setBackgroundResource(R.drawable.ic_heart_big);

        }
        else if(statRating.equals("2")){
            item1.setBackgroundResource(R.drawable.ic_heart_big);
            item2.setBackgroundResource(R.drawable.ic_heart_big);

        }
        else if(statRating.equals("3")){
            item1.setBackgroundResource(R.drawable.ic_heart_big);
            item2.setBackgroundResource(R.drawable.ic_heart_big);
            item3.setBackgroundResource(R.drawable.ic_heart_big);

        }
        else if(statRating.equals("4")){
            item1.setBackgroundResource(R.drawable.ic_heart_big);
            item2.setBackgroundResource(R.drawable.ic_heart_big);
            item3.setBackgroundResource(R.drawable.ic_heart_big);
            item4.setBackgroundResource(R.drawable.ic_heart_big);

        }else if(statRating.equals("5")){
            item1.setBackgroundResource(R.drawable.ic_heart_big);
            item2.setBackgroundResource(R.drawable.ic_heart_big);
            item3.setBackgroundResource(R.drawable.ic_heart_big);
            item4.setBackgroundResource(R.drawable.ic_heart_big);
            item5.setBackgroundResource(R.drawable.ic_heart_big);

        }



        return convertView;
    }

    private class ViewHolder{
        public TextView name;
        public TextView date;
        public TextView room;
        public TextView content;
    }

}