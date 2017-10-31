package com.example.e610.tvappseriesguide.MainScreen;

import android.app.Activity;

import com.example.e610.tvappseriesguide.Models.SeriesModel;

import java.io.Serializable;
import java.util.ArrayList;


public interface MainContract {

    public interface View extends Serializable{

        public void DisplayRemoteData(String jsonData);
        public void DisplayLocalData(ArrayList<SeriesModel> models);
        public void setPresenter(MainContract.presenter presenter);
        public void setType(String Type);
    }

    public interface mainView extends Serializable{
        public void signOutSuccessfully();
    }
    public interface presenter extends Serializable {

        public void LoadRemoteData(String info);
        public void LoadLocalData(String info);
        public void signOut();
        public void setUpGoogleApiClient(Activity activity);

    }

}
