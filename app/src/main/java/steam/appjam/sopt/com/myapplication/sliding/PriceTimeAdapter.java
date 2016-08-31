package steam.appjam.sopt.com.myapplication.sliding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.sliding.PriceTime.PriceTimeItem;

/**
 * Created by parkkyounghyun on 2016. 7. 7..
 */

class PriceTimeAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    List<PriceTimeItem> arSrc;

    //생성자
    public PriceTimeAdapter(Context context, List<PriceTimeItem> arItem){
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
    public PriceTimeItem getItem(int position) {
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

            convertView = mInflater.inflate(R.layout.activity_price_time_row, parent, false);

            holder.type = (TextView)convertView.findViewById(R.id.type);
            holder.price = (TextView)convertView.findViewById(R.id.price);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.type.setText(arSrc.get(position).getType());
        holder.price.setText(arSrc.get(position).getPrice());

        return convertView;
    }

    private class ViewHolder{
        public TextView type;
        public TextView price;
    }

}
