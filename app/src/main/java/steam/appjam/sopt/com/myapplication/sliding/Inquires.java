package steam.appjam.sopt.com.myapplication.sliding;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import steam.appjam.sopt.com.myapplication.R;


/**
 * Created by 즤 on 2016-06-29.
 */

public class Inquires extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Dialog getDialog;
    private static String callnum;

    static Inquires newInstance(String telName) {
        Inquires f = new Inquires();

        callnum = telName;
        // Supply num input as an argument.
        Bundle args = new Bundle();
        //args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.inquires_layout, container, false);
        getDialog = this.getDialog();
        getDialog.requestWindowFeature(STYLE_NO_TITLE);
        // View tv = v.findViewById(R.id.text);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경 투명하게 만듦

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();


        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER | Gravity.CENTER;
        getDialog().getWindow().setAttributes(params);

        ImageButton btn1 = (ImageButton) v.findViewById(R.id.callButton);
        ImageButton btn2 = (ImageButton) v.findViewById(R.id.websiteButton);

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+callnum));
                startActivity(intent);

                //finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.naver.com"));
                startActivity(intent);

                //finish();
                }
            });

        return v;
    }

}
