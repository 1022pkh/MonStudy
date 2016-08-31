package steam.appjam.sopt.com.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginDialog extends Dialog {

    private Button dialog_login;
    private Button dialog_login_cancel;

    private View.OnClickListener loginEvent;
    private View.OnClickListener loginCancelEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog);

        dialog_login = (Button) findViewById(R.id.dialog_login);
        dialog_login_cancel = (Button)findViewById(R.id.dialog_login_cancel);

        dialog_login.setOnClickListener(loginEvent);
        dialog_login_cancel.setOnClickListener(loginCancelEvent);

    }

    public LoginDialog(Context context,View.OnClickListener CurrentEvent,View.OnClickListener BtnEvent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.loginEvent = CurrentEvent;
        this.loginCancelEvent = BtnEvent;
    }
}
