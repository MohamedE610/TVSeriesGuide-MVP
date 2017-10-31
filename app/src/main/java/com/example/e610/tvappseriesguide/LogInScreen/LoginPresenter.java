package com.example.e610.tvappseriesguide.LogInScreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.e610.tvappseriesguide.MainScreen.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;


public class LoginPresenter implements LoginContract.presenter, GoogleApiClient.OnConnectionFailedListener {



    LoginActivity loginActivity;
    private GoogleApiClient mGoogleApiClient;

    public LoginPresenter(LoginActivity activity){
        loginActivity=activity;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(loginActivity)
                .enableAutoManage(loginActivity,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }
    private static final int RC_SIGN_IN = 007;
    @Override
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        loginActivity.signInSuccessfully(RC_SIGN_IN,signInIntent);
    }

    @Override
    public void handleSignInResult(Intent intent) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            loginActivity.handleSignInResultSuccessfully(true);

        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    public void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            loginActivity.handleSignInResultSuccessfully(true);

        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    @Override
    public void checkSignIn(boolean b) {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
