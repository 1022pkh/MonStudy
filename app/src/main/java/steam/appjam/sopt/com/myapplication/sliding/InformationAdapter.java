package steam.appjam.sopt.com.myapplication.sliding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.sliding.information.informationItem;

/**
 * Created by parkkyounghyun on 2016. 7. 7..
 */


class InformationAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    List<informationItem> arSrc;

    //생성자
    public InformationAdapter(Context context, List<informationItem> arItem){
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
    public informationItem getItem(int position) {
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

            convertView = mInflater.inflate(R.layout.listview_room_detail_information, parent, false);

            holder.iv_imageView = (ImageView)convertView.findViewById(R.id.iv_imageview);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_number = (TextView)convertView.findViewById(R.id.tv_number);
            holder.tv_option = (TextView)convertView.findViewById(R.id.tv_option);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.iv_imageView.setImageResource(arSrc.get(position).getTempimage());
        holder.tv_name.setText(arSrc.get(position).getTv_name());
        holder.tv_number.setText(arSrc.get(position).getTv_number());
        holder.tv_option.setText(arSrc.get(position).getTv_option());

        return convertView;
    }

    private class ViewHolder{
        public ImageView iv_imageView;
        public TextView tv_name;
        public TextView tv_number;
        public TextView tv_option;
    }

}
