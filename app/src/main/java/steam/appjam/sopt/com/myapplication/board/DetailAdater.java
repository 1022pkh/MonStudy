package steam.appjam.sopt.com.myapplication.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import steam.appjam.sopt.com.myapplication.R;

/**
 * Created by parkkyounghyun on 2016. 7. 2..
 */
public class DetailAdater extends BaseAdapter {


    LayoutInflater mInflater;
    List<BoardDetailItem> arSrc;

    //생성자
    public DetailAdater(Context context, List<BoardDetailItem> arItem){
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
    public BoardDetailItem getItem(int position) {
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

            convertView = mInflater.inflate(R.layout.listview_reply, parent, false);

            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
            holder.tv_register_time = (TextView)convertView.findViewById(R.id.tv_register_time);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv_name.setText(arSrc.get(position).getTv_name());
        holder.tv_content.setText(arSrc.get(position).getTv_content());
        holder.tv_register_time.setText(arSrc.get(position).getTv_register_time());


        return convertView;
    }

    private class ViewHolder{
        public TextView tv_name;
        public TextView tv_content;
        public TextView tv_register_time;

    }

}
