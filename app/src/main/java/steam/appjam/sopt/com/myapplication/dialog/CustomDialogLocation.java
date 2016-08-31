package steam.appjam.sopt.com.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import steam.appjam.sopt.com.myapplication.R;

/**
 * Created by KyoungHyun on 16. 5. 1..
 */
public class CustomDialogLocation extends Dialog {

    private Button searchCurrent;
    private Button searchSend;
    private TextView item1;
    private TextView item2;
    private TextView item3;
    private TextView item4;
    private TextView item5;
    private TextView item6;
    private TextView item7;
    private TextView item8;
    private TextView item9;

    String address = "null";



    private View.OnClickListener searchCurrentEvent;
    private View.OnClickListener sendLocationEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_location);

        searchSend = (Button)findViewById(R.id.sendLocation);
        searchSend.setOnClickListener(sendLocationEvent);

        item1 = (TextView)findViewById(R.id.item1);
        item2 = (TextView)findViewById(R.id.item2);
        item3 = (TextView)findViewById(R.id.item3);
        item4 = (TextView)findViewById(R.id.item4);
        item5 = (TextView)findViewById(R.id.item5);
        item6 = (TextView)findViewById(R.id.item6);
        item7 = (TextView)findViewById(R.id.item7);
        item8 = (TextView)findViewById(R.id.item8);
        item9 = (TextView)findViewById(R.id.item9);

        item1.setOnClickListener(getAddress);
        item2.setOnClickListener(getAddress);
        item3.setOnClickListener(getAddress);
        item4.setOnClickListener(getAddress);
        item5.setOnClickListener(getAddress);
        item6.setOnClickListener(getAddress);
        item7.setOnClickListener(getAddress);
        item8.setOnClickListener(getAddress);
        item9.setOnClickListener(getAddress);

    }

    public CustomDialogLocation(Context context,View.OnClickListener CurrentEvent,View.OnClickListener BtnEvent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.searchCurrentEvent = CurrentEvent;
        this.sendLocationEvent = BtnEvent;

    }


    private View.OnClickListener getAddress = new View.OnClickListener() {
        public void onClick(View v) {

            item1.setBackgroundResource(R.color.itemBack);
            item2.setBackgroundResource(R.color.itemBack);
            item3.setBackgroundResource(R.color.itemBack);
            item4.setBackgroundResource(R.color.itemBack);
            item5.setBackgroundResource(R.color.itemBack);
            item6.setBackgroundResource(R.color.itemBack);
            item7.setBackgroundResource(R.color.itemBack);
            item8.setBackgroundResource(R.color.itemBack);
            item9.setBackgroundResource(R.color.itemBack);

            v.setBackgroundResource(R.color.blue);

            switch (v.getId()){
                case R.id.item1:
                    address = "강남";
                    break;
                case R.id.item2:
                    address = "송파";
                    break;
                case R.id.item3:
                    address = "강서";
                    break;

                case R.id.item4:
                    address = "영등포";
                    break;
                case R.id.item5:
                    address = "관악";
                    break;
                case R.id.item6:
                    address = "광진";
                    break;
                case R.id.item7:
                    address = "노원";
                    break;
                case R.id.item8:
                    address = "종로";
                    break;
                case R.id.item9:
                    address = "마포";
                    break;
                default:
                    address = "null";
                    break;
            }

        }
    };

    public String giveAddress(){

        return address;
    }
}

