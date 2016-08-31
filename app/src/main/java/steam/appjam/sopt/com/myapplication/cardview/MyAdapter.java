package steam.appjam.sopt.com.myapplication.cardview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.sliding.SlidingDetail;

/**
 * Created by parkkyounghyun on 2016. 6. 28..
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // each data item is just a string in this case

    public View v;
    private ArrayList<MyData> mDataset;

    private AQuery aq;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public ImageView mImageView;
        public String mId;
        public TextView mName;
        public TextView mLocation;
        public TextView mAddress;
        public TextView mPrice;
        public TextView mStar;

        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);

            mImageView = (ImageView)view.findViewById(R.id.image);
            mName = (TextView)view.findViewById(R.id.RoomName);
            mLocation = (TextView)view.findViewById(R.id.RoomLocation);
            mAddress = (TextView)view.findViewById(R.id.RoomAddress);
            mPrice = (TextView)view.findViewById(R.id.RoomPrice);
            mStar = (TextView)view.findViewById(R.id.RoomRating);
        }

        @Override
        public void onClick(View v) {

//            System.out.println(getPosition());
//            Log.i("test",mId);

            Intent intent = new Intent(v.getContext() , SlidingDetail.class);
            intent.putExtra("space_id",mId);
            v.getContext().startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<MyData> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_main, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mId = mDataset.get(position).id;

        holder.mName.setText(mDataset.get(position).name);
        holder.mLocation.setText(mDataset.get(position).location);
        holder.mAddress.setText(mDataset.get(position).address);
        holder.mPrice.setText(mDataset.get(position).price);
        holder.mStar.setText(mDataset.get(position).star);

//        try {
//            URL url = new URL(mDataset.get(position).img);
//            URLConnection conn = url.openConnection();
//            conn.connect();
//            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
//            Bitmap bm = BitmapFactory.decodeStream(bis);
//            bis.close();
//            holder.test.setImageBitmap(bm);
//        } catch (Exception e) {
//        }


        aq = new AQuery(v);
        aq.id(holder.mImageView).image(mDataset.get(position).img);

//        Log.i("22Tag",mDataset.get(position).img);
//        holder.mImageView.setBackground(mDataset.get(position).img);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}


