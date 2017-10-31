package com.example.e610.tvappseriesguide.MainScreen;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.e610.tvappseriesguide.Data.DataSource;
import com.example.e610.tvappseriesguide.Data.SeriesRepository;
import com.example.e610.tvappseriesguide.Models.SeriesModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;

public class MainPresenter implements MainContract.presenter  , GoogleApiClient.OnConnectionFailedListener {

    MainFragment mainFragment;
    viewPagerFragment viewPagerFragment;
    SeriesRepository mainSeriesRepository;

    public MainPresenter(Fragment fragment, SeriesRepository seriesRepository){
        if(seriesRepository !=null) {
            viewPagerFragment = (viewPagerFragment) fragment;
            viewPagerFragment.setPresenter(this);
        }
        else
            mainFragment=(MainFragment)fragment;
        mainSeriesRepository = seriesRepository;

    }
    @Override
    public void LoadRemoteData(String info) {
        mainSeriesRepository.getRemoteData(new DataSource.CallBacks() {
            @Override
            public void remoteDataLoaded(String jsonData) {
                viewPagerFragment.DisplayRemoteData(jsonData);
            }

            @Override
            public void localDataLoaded(ArrayList<SeriesModel> models) {

            }

            @Override
            public void favouriteChecked(boolean b) {

            }

            @Override
            public void modelSaved(boolean b) {

            }

            @Override
            public void modelDeleted(boolean b) {

            }


        },info,"");
    }

    @Override
    public void LoadLocalData(String info) {

        mainSeriesRepository.getLocalData(new DataSource.CallBacks() {
            @Override
            public void remoteDataLoaded(String jsonData) {

            }

            @Override
            public void localDataLoaded(ArrayList<SeriesModel> models) {
               viewPagerFragment.DisplayLocalData(models);
            }

            @Override
            public void favouriteChecked(boolean b) {

            }

            @Override
            public void modelSaved(boolean b) {

            }

            @Override
            public void modelDeleted(boolean b) {

            }
        },info);
    }

    private GoogleApiClient mGoogleApiClient;
    @Override
    public void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });

        mainFragment.signOutSuccessfully();
    }

    @Override
    public void setUpGoogleApiClient(Activity activity) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage((FragmentActivity)activity,this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
