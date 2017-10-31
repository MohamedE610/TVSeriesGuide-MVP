package com.example.e610.tvappseriesguide.Data;

import android.content.Context;

import com.example.e610.tvappseriesguide.Models.SeriesModel;

import java.io.Serializable;
import java.util.ArrayList;

public interface DataSource extends Serializable {

    public interface CallBacks{

        public void remoteDataLoaded(String jsonData);
        public void localDataLoaded(ArrayList<SeriesModel> models);
        public void favouriteChecked(boolean b);
        public void modelSaved(boolean b);
        public void modelDeleted(boolean b);
    }

    public void getRemoteData(CallBacks callBacks,String info1,String Info2);
    public void getLocalData(CallBacks callBacks, String Info);
    public void checkFavourite(CallBacks callBacks, String Info ,String id);
    public void saveModel(CallBacks callBacks, String Info, SeriesModel model);
    public void deleteModel(CallBacks callBacks, String Info, String id);



}
