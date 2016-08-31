package steam.appjam.sopt.com.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import steam.appjam.sopt.com.myapplication.R;

/**
 * Created by KyoungHyun on 16. 5. 1..
 */
public class CustomDialogPeople extends Dialog{

    private Button minusBtn;
    private Button plusBtn;
    private Button sendBtn;
    private TextView currentPerson;

    private int getPerson = 2;
    private View.OnClickListener sendPersonEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_people);

        minusBtn = (Button)findViewById(R.id.minusPerson);
        plusBtn = (Button)findViewById(R.id.plusPerson);
        sendBtn = (Button)findViewById(R.id.sendPerson);

        currentPerson = (TextView)findViewById(R.id.currentPerson);

        minusBtn.setOnClickListener(minusPersonEvent);
        plusBtn.setOnClickListener(plusPersonEvent);

        sendBtn.setOnClickListener(sendPersonEvent);

        String temp = String.valueOf(getPerson);
        currentPerson.setText(temp);

    }

    public CustomDialogPeople(Context context,int person, View.OnClickListener BtnEvent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        getPerson = person;
        this.sendPersonEvent = BtnEvent;
    }

    private View.OnClickListener minusPersonEvent = new View.OnClickListener() {
        public void onClick(View v) {
            int person = Integer.valueOf((String) currentPerson.getText());

            if(person-1 < 1 )
                Toast.makeText(getContext().getApplicationContext(),"최소 1명입니다.",Toast.LENGTH_SHORT).show();
            else
                currentPerson.setText(String.valueOf(person-1));
        }
    };


    private View.OnClickListener plusPersonEvent = new View.OnClickListener() {
        public void onClick(View v) {
            int person = Integer.valueOf((String) currentPerson.getText());

            if(person+1 > 40 )
                Toast.makeText(getContext().getApplicationContext(),"최대 40명입니다.",Toast.LENGTH_SHORT).show();
            else
                currentPerson.setText(String.valueOf(person+1));
        }
    };


    public int getPerson(){
        int person = Integer.valueOf((String) currentPerson.getText());
        return person;
    }
}
