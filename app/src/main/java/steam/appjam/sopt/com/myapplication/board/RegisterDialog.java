package steam.appjam.sopt.com.myapplication.board;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import steam.appjam.sopt.com.myapplication.R;

public class RegisterDialog extends Dialog {

    private Button dialog_register;
    private Button dialog_dialog_register_cancel;

    private View.OnClickListener registerEvent;
    private View.OnClickListener registerCancelEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_dialog);

        dialog_register = (Button) findViewById(R.id.dialog_register);
        dialog_dialog_register_cancel = (Button)findViewById(R.id.dialog_register_cancel);

        dialog_register.setOnClickListener(registerEvent);
        dialog_dialog_register_cancel.setOnClickListener(registerCancelEvent);

    }

    public RegisterDialog(Context context,View.OnClickListener CurrentEvent,View.OnClickListener BtnEvent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.registerEvent = CurrentEvent;
        this.registerCancelEvent = BtnEvent;
    }
}
