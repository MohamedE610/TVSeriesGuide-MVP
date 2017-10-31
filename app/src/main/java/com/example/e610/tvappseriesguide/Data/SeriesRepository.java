package com.example.e610.tvappseriesguide.Data;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.e610.tvappseriesguide.Data.LocalDataSource.LocalData;
import com.example.e610.tvappseriesguide.Data.RemoteDataSource.RemoteData;
import com.example.e610.tvappseriesguide.Models.SeriesModel;


public class SeriesRepository implements DataSource {


    RemoteData remoteData;
    LocalData localData;

    Fragment fragment ;

    public SeriesRepository(Fragment f){
        fragment=f;
    }


    @Override
    public void getRemoteData(CallBacks callBacks, String info1, String Info2) {

        remoteData =new RemoteData(info1,Info2);
        remoteData.setNetworkOperations(callBacks);
        remoteData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void getLocalData(CallBacks callBacks, String info) {

        LocalData localData=new LocalData(fragment,info,"");
        localData.setCallBacks(callBacks);
        localData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void checkFavourite(CallBacks callBacks, String Info, String id) {

        LocalData localData=new LocalData(fragment,Info,id);
        localData.setCallBacks(callBacks);
        localData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void saveModel(CallBacks callBacks, String Info, SeriesModel model) {

        LocalData localData=new LocalData(fragment,Info,model);
        localData.setCallBacks(callBacks);
        localData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void deleteModel(CallBacks callBacks, String Info, String id) {

        LocalData localData=new LocalData(fragment,Info,id);
        localData.setCallBacks(callBacks);
        localData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
