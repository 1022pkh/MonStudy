package steam.appjam.sopt.com.myapplication.profile.component;

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
import steam.appjam.sopt.com.myapplication.board.BoardItem;

public class ProfileAdapter extends BaseAdapter {


    LayoutInflater mInflater;
    List<BoardItem> arSrc;

    ArrayList<BoardItem> arraylist;


    //생성자
    public ProfileAdapter(Context context, List<BoardItem> arItem){
        //인플레이트 준비를 합니다.
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            holder.tv_number = (TextView)convertView.findViewById(R.id.tv_number);
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
        holder.tv_number.setText(arSrc.get(position).getTv_number());
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
        charText = charText.toLowerCase(Locale.getDefault());
        arSrc.clear();
        if (charText.length() == 0) {
            arSrc.addAll(arraylist);
        }
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
