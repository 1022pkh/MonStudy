package steam.appjam.sopt.com.myapplication.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import steam.appjam.sopt.com.myapplication.R;

/**
 * Created by parkkyounghyun on 2016. 7. 2..
 */
public class BoardAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    List<BoardItem> arSrc;

    ArrayList<BoardItem> arraylist;


    //생성자
    public BoardAdapter(Context context, List<BoardItem> arItem){
        //인플레이트 준비를 합니다.
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /**
         * 여기서 arSrc은 검색한 데이터가 일치하는 항목만 넣어주는 객체입니다. (B)
         * 처음에는 입력한 데이터가 없기때문에, 일단 모든 데이터를 넣어줍니다
         * arraylist 경우에는 데이터 전체를 가지고있는 객체입니다. (A)
         */

        arSrc = arItem;

        this.arraylist = new ArrayList<BoardItem>();
        this.arraylist.addAll(arSrc);


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arSrc.size();
    }

    @Override
    public BoardItem getItem(int position) {
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

            convertView = mInflater.inflate(R.layout.listview_board, parent, false);

            holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
            holder.tv_type = (TextView)convertView.findViewById(R.id.tv_type);
            holder.tv_region = (TextView)convertView.findViewById(R.id.tv_region);


            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv_title.setText(arSrc.get(position).getTv_title());
        holder.tv_name.setText(arSrc.get(position).getTv_name());
        holder.tv_date.setText(arSrc.get(position).getTv_date());
        holder.tv_type.setText(arSrc.get(position).getTv_type());
        holder.tv_region.setText(arSrc.get(position).getTv_region());


        return convertView;
    }

    private class ViewHolder{
        public TextView tv_title;
        public TextView tv_number;
        public TextView tv_name;
        public TextView tv_date;
        public TextView tv_type;
        public TextView tv_region;
    }


    // Filter Class
    public void filter(String charText) {
        /**
         * 검색창에 입력한 데이터를 가져오는 것이죠
         * 밑에 toLowerCase()의 경우에는 소문자로 바꿔주는 함수죠
         * 대소문자 구분없이 검색하도록 넣어준 것입니다
         * 만약 대소문자 구분하여 검색하려면 toLowerCase()부분을 제거해주시면 됩니다!
         */
        charText = charText.toLowerCase(Locale.getDefault());

        //먼저 arSrc객체를 비워줍니다.
        arSrc.clear();

        //입력한 데이터가 없을 경우에는 모든 데이터항목을 출력해줍니다.
        if (charText.length() == 0) {
            arSrc.addAll(arraylist);
        }
        //입력한 데이터가 있을 경우에는 일치하는 항목들만 찾아 출력해줍니다.
        else
        {
            for (BoardItem wp : arraylist)
            {
                if (wp.getTv_title().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arSrc.add(wp);

                }
            }
        }

        notifyDataSetChanged();
    }

}
