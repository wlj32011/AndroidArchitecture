package com.wanglj.androidarchitecture.module.login;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.wanglj.androidarchitecture.ArchitectureApplication;
import com.wanglj.androidarchitecture.R;
import com.wanglj.androidarchitecture.data.remote.ApiManager;
import com.wanglj.androidarchitecture.data.remote.model.User;
import com.wanglj.androidarchitecture.module.base.BaseActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class LoginActivity extends BaseActivity implements LoginView{

    @Inject
    LoginPresenter loginPresenter;

    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.password) EditText password;

    @Bind(R.id.login)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        username.setText(loginPresenter.getUserNameFromLocal());
        password.setText(loginPresenter.getPasswordFromLocal());



        RxTextView.textChanges(username).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                loginPresenter.checkInput(charSequence.toString(),password.getText().toString());
            }
        });

        RxTextView.textChanges(password).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                loginPresenter.checkInput(username.getText().toString(),charSequence.toString());
            }
        });

    }

    @OnClick(R.id.login) void onLoginClick() {
        loginPresenter.login(username.getText().toString(),password.getText().toString());
    }


    @Override
    public void setupActivityComponent() {
        ArchitectureApplication.get(this).getAppComponent().plus(new LoginModule(this)).inject(this);
    }


    @Override
    public void canLogin(boolean canLogin) {
        if(canLogin){
            login.setEnabled(true);
            login.setBackgroundColor(Color.GREEN);
        }else{
            login.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public void showUser(User user) {
        loginPresenter.saveLoginInfo(user.getUsername(),user.getPassword());
        Log.e("showUser",user.toString());
    }


    @Override
    public void showLoading() {
        showProgressDialog("加载中...");
    }

    @Override
    public void hideLoading() {
        dissmissProgressDialog();
    }

}
