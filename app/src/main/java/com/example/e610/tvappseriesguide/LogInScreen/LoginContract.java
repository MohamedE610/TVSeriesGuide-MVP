package com.example.e610.tvappseriesguide.LogInScreen;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;


public interface LoginContract {

    public interface view{
        public void signInSuccessfully(int i,Intent intent);
        public void handleSignInResultSuccessfully(boolean b);
        public void checkSignInSuccessfully(boolean b);
    }

    public interface presenter{
        public void signIn();
        public void handleSignInResult(Intent intent);
        public void checkSignIn(boolean b);
    }
}
