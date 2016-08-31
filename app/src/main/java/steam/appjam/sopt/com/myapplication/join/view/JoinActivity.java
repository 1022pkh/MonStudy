package steam.appjam.sopt.com.myapplication.join.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import steam.appjam.sopt.com.myapplication.ErrorController;
import steam.appjam.sopt.com.myapplication.R;
import steam.appjam.sopt.com.myapplication.join.presenter.JoinPresenter;
import steam.appjam.sopt.com.myapplication.join.presenter.JoinPresenterImpl;
import steam.appjam.sopt.com.myapplication.join.view.JoinCharActivity;
import steam.appjam.sopt.com.myapplication.join.view.JoinView;

/**
 * Created by Min_Mac on 16. 4. 14..
 */
public class JoinActivity extends AppCompatActivity implements JoinView {

    private Boolean nameValid = false, pwValid = false, emailValid = false;
    private Boolean idValid = false;

    JoinPresenter presenter;

    @Bind(R.id.join_edit_id)
    EditText editId;
    @Bind(R.id.join_edit_name)
    EditText editName;
    @Bind(R.id.join_edit_email)
    EditText editEmail;
    @Bind(R.id.join_edit_passwd)
    EditText editPasswd;
    @Bind(R.id.join_edit_passwd_conf)
    EditText editPasswd_Conf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);


        presenter = new JoinPresenterImpl(this);


        TextView toolbar_title = ((TextView) findViewById(R.id.main_toolbar_title));
        toolbar_title.setTypeface(Typeface.createFromAsset(getAssets(),"Quicksand_Bold.otf"));
        toolbar_title.setText("MONSTUDY");

    }

    /*
        @OnClick(R.id.join_btn_cancel)
        public void setBtn_cancel() {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    */
    @Override
    public void networkError() {
        ErrorController errorController = new ErrorController(getApplicationContext());
        errorController.notifyNetworkError();
    }




    @OnClick(R.id.join_btn_next)
    public void setBtnNext() {

        String id = editId.getText().toString();
        String name = editName.getText().toString();
        String pw = editPasswd.getText().toString();
        String email = editEmail.getText().toString();

        checkEditText();

        if (nameValid && emailValid && pwValid) {
            Intent intent = new Intent(getApplicationContext(), JoinCharActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("name", name);
            intent.putExtra("pw", pw);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void isDuplicated(String result) {
        if (result.equals("Duplicated")) {
            Toast.makeText(getApplicationContext(), getString(R.string.register_id_duplicated), Toast.LENGTH_SHORT).show();
            idValid = false;
        } else if (result.equals("Unduplicated")) {
            Toast.makeText(getApplicationContext(), getString(R.string.register_id_unduplicated), Toast.LENGTH_SHORT).show();
            idValid = true;
        }
    }

    @Override
    public void isNameDuplicated(String result) {
        if (result.equals("NameDuplicated")) {
            Toast.makeText(getApplicationContext(), getString(R.string.register_name_duplicated), Toast.LENGTH_SHORT).show();
            nameValid = false;
        } else if (result.equals("NameUnduplicated")) {
            Toast.makeText(getApplicationContext(), getString(R.string.register_name_unduplicated), Toast.LENGTH_SHORT).show();
            nameValid = true;
        }
    }

    @OnClick(R.id.join_btn_idDupl)
    public void setBtnDuplication() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editId.getWindowToken(), 0);
        final String user_id = editId.getText().toString();

        if (TextUtils.isEmpty(user_id)) editId.setError(getString(R.string.error_field_required));
        else {
            if (!isiDValid(user_id)) editId.setError(getString(R.string.error_invalid_id));
            else presenter.checkIdDuplication(user_id);
        }
    }

    @OnClick(R.id.join_btn_nameDupl)
    public void setBtnNameDuplication() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editId.getWindowToken(), 0);
        final String name = editName.getText().toString();

        if (TextUtils.isEmpty(name)) editName.setError(getString(R.string.error_field_required));
        else {
            if (!isNameValid(name)) editId.setError(getString(R.string.error_invalid_id));
            else presenter.checkNameDuplication(name);
        }
    }

    private void checkEditText() {
        String pw = editPasswd.getText().toString();
        String email = editEmail.getText().toString();

        if (TextUtils.isEmpty(email)) editEmail.setError(getString(R.string.error_field_required));
        else {
            if (!isEmailValid(email)) editEmail.setError(getString(R.string.error_invalid_email));
            else emailValid = true;
        }

        if (TextUtils.isEmpty(pw)) editPasswd.setError(getString(R.string.error_field_required));
        else {
            if (!isPasswordValid()) editPasswd.setError(getString(R.string.error_invalid_password));
            else {
                if (!isPasswordCheck())
                    editPasswd_Conf.setError(getString(R.string.error_incorrect_password));
                else pwValid = true;
            }
        }

        if (!nameValid)
            Toast.makeText(getApplicationContext(), getString(R.string.register_check_name), Toast.LENGTH_SHORT).show();
        else if (!emailValid)
            Toast.makeText(getApplicationContext(), getString(R.string.register_check_email), Toast.LENGTH_SHORT).show();
        else if (!pwValid)
            Toast.makeText(getApplicationContext(), getString(R.string.register_check_pw), Toast.LENGTH_SHORT).show();

    }
    private boolean isiDValid(String user_id) {
        return user_id.length() >= 4;
    }

    private boolean isNameValid(String name) {
        return name.length() >= 2;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid() {
        return editPasswd.getText().toString().length() >= 6;
    }

    private boolean isPasswordCheck() {
        return editPasswd.getText().toString().equals(editPasswd_Conf.getText().toString());
    }
}
