package steam.appjam.sopt.com.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JoinDialog extends Dialog {

    private Button dialog_login;

    private View.OnClickListener joinEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_dialog);

        dialog_login = (Button) findViewById(R.id.dialog_join);

        dialog_login.setOnClickListener(joinEvent);

    }

    public JoinDialog(Context context,View.OnClickListener BtnEvent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.joinEvent = BtnEvent;
    }
}
