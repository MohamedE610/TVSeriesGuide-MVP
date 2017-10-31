package com.example.e610.tvappseriesguide.LogInScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.e610.tvappseriesguide.MainScreen.MainActivity;
import com.example.e610.tvappseriesguide.R;
import com.example.e610.tvappseriesguide.Utils.NetworkState;
import com.google.android.gms.common.SignInButton;

public class LoginActivity extends AppCompatActivity implements LoginContract.view ,View.OnClickListener{


    private SignInButton btnSignIn;
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);

        loginPresenter=new LoginPresenter(this);

    }

    @Override
    public void signInSuccessfully(int i,Intent intent) {

        startActivityForResult(intent,i);
        /*startActivity(new Intent(this, MainActivity.class));
        finish();*/
    }

    @Override
    public void handleSignInResultSuccessfully(boolean b) {
       startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void checkSignInSuccessfully(boolean b) {

    }

    @Override
    public void onClick(View v) {
        if(NetworkState.ConnectionAvailable(this))
            loginPresenter.signIn();
        else
            Toast.makeText(LoginActivity.this,getString(R.string.NO_Internet),Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.checkSignIn(true);
    }
    private static final int RC_SIGN_IN = 007;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
            loginPresenter.handleSignInResult(data);
    }
}
