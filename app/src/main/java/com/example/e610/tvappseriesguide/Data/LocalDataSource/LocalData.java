package com.example.e610.tvappseriesguide.Data.LocalDataSource;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;

import com.example.e610.tvappseriesguide.Data.DataSource;
import com.example.e610.tvappseriesguide.Data.LocalDataSource.CotentProvider.SeriesContract;
import com.example.e610.tvappseriesguide.Models.SeriesModel;

import java.io.Serializable;
import java.util.ArrayList;

public class LocalData extends AsyncTask<Void, Void, Object> implements Serializable , LoaderManager.LoaderCallbacks<Cursor>{

    Fragment fragment;
    Context context;
    String operationType;
    String seriesID;
    SeriesModel seriesModel;


    public LocalData(Context ctx) {
    context=ctx;
    }

    public LocalData(Fragment fragment  , String op, Object obj) {
        context = fragment.getContext();
        operationType = op;

        if (operationType.equals("check") || operationType.equals("delete")) {
            seriesID = (String) obj;
        } else if (operationType.equals("save")) {
            seriesModel = (SeriesModel) obj;
        }else if(operationType.equals("Favourite")){
            this.fragment=fragment;
            //this.fragment.getLoaderManager().initLoader(610,null,this);
        }




    }

    DataSource.CallBacks callBacks;

    public void setCallBacks(DataSource.CallBacks callBacks) {

        this.callBacks = callBacks;

        if(operationType.equals("Favourite")){
            this.fragment.getLoaderManager().initLoader(610,null,this);
        }

    }


    @Override
    protected Object doInBackground(Void... params) {

        synchronized (context) {
            ArrayList<SeriesModel> models = null;
            boolean b = true;

            if (operationType.equals("Favourite")) {
                //models = getFavouriteMovies();
                 return models;
            } else if (operationType.equals("check")) {
                b = isFavorite(seriesID);
                return b;
            } else if (operationType.equals("save")) {
                b = saveInDatabase(seriesModel);
                return b;
            } else if (operationType.equals("delete")) {
                b = deleteFromDatabase(seriesID);
                return b;
            } else
                return null;
        }

    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);

        if (operationType.equals("Favourite")) {
           /* ArrayList<SeriesModel> models=( ArrayList<SeriesModel>)object;
            callBacks.localDataLoaded(models);*/
        } else if (operationType.equals("delete") ) {
            boolean b=(boolean)object;
            callBacks.modelDeleted(b);
        }
        else if (operationType.equals("check") ) {
            boolean b=(boolean)object;
            callBacks.favouriteChecked(b);
        } else if (operationType.equals("save") ) {
            boolean b=(boolean)object;
            callBacks.modelSaved(b);
        }
    }

    public ArrayList<SeriesModel> getFavouriteMovies(Cursor cursor) {
        ArrayList<SeriesModel> models = new ArrayList<>();

        /*Cursor cursor = context.getContentResolver().query(SeriesContract.SeriesEntry.CONTENT_URI, null, null, null, null);*/
        /*while (cursor.moveToNext()) {
            SeriesModel model = new SeriesModel();
            model.setId(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_ID-1));
            model.setTitle(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_TITLE-1));
            model.setPoster_ImageUrl(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_POSTER_PATH-1));
            model.setOverview(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_OVERVIEW-1));
            model.setVote_average(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_VOTE_AVERAGE-1));
            model.setRelease_Date(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_RELEASE_DATE-1));

            models.add(model);
        }*/

        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);

            SeriesModel model = new SeriesModel();
            model.setId(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_ID-1));
            model.setTitle(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_TITLE-1));
            model.setPoster_ImageUrl(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_POSTER_PATH-1));
            model.setOverview(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_OVERVIEW-1));
            model.setVote_average(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_VOTE_AVERAGE-1));
            model.setRelease_Date(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_RELEASE_DATE-1));

            models.add(model);
        }

        return models;
    }

    public ArrayList<SeriesModel> getFavouriteMovies() {
        ArrayList<SeriesModel> models = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(SeriesContract.SeriesEntry.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            SeriesModel model = new SeriesModel();
            model.setId(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_ID));
            model.setTitle(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_TITLE));
            model.setPoster_ImageUrl(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_POSTER_PATH));
            model.setOverview(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_OVERVIEW));
            model.setVote_average(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_VOTE_AVERAGE));
            model.setRelease_Date(cursor.getString(SeriesContract.SeriesEntry.COL_SERIES_RELEASE_DATE));

            models.add(model);

        }

        return models;
    }

    private boolean isFavorite(String id) {
        Cursor movieCursor = context.getContentResolver().query(
                SeriesContract.SeriesEntry.CONTENT_URI,
                new String[]{SeriesContract.SeriesEntry.COLUMN_SERIES_ID},
                SeriesContract.SeriesEntry.COLUMN_SERIES_ID + " = " + id,
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteFromDatabase(String id) {

        if (isFavorite(id)) {
            try {
                context.getContentResolver().delete(SeriesContract.SeriesEntry.CONTENT_URI,
                        SeriesContract.SeriesEntry.COLUMN_SERIES_ID + " = " + id, null);
                return true;
            } catch (Exception ex) {
                return false;
            }
        } else
            return false;

    }


    public boolean saveInDatabase(SeriesModel model) {

        if (!isFavorite(model.getId())) {
            try {

                ContentValues seriesValues = new ContentValues();
                seriesValues.put(SeriesContract.SeriesEntry.COLUMN_SERIES_ID,
                        model.getId());
                seriesValues.put(SeriesContract.SeriesEntry.COLUMN_SERIES_TITLE,
                        model.getTitle());
                seriesValues.put(SeriesContract.SeriesEntry.COLUMN_SERIES_POSTER_PATH,
                        model.getPoster_ImageUrl());
                seriesValues.put(SeriesContract.SeriesEntry.COLUMN_SERIES_OVERVIEW,
                        model.getOverview());
                seriesValues.put(SeriesContract.SeriesEntry.COLUMN_SERIES_VOTE_AVERAGE,
                        model.getVote_average());
                seriesValues.put(SeriesContract.SeriesEntry.COLUMN_SERIES_RELEASE_DATE,
                        model.getRelease_Date());

                context.getContentResolver().insert(
                        SeriesContract.SeriesEntry.CONTENT_URI,
                        seriesValues);
                return true;
            }
            catch (Exception ex){return false;}
        }
        else
            return false;

    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Activity activity=fragment.getActivity();
        return new CursorLoader(activity, SeriesContract.SeriesEntry.CONTENT_URI, SeriesContract.SeriesEntry.Series_COLUMNS,
                null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {


            ArrayList<SeriesModel> models = getFavouriteMovies(data);
            //why you put this here you can make flag or counter to avoid this
        /* if(models.size()==0)
               models=getFavouriteMovies();*/
            callBacks.localDataLoaded(models);

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

        int x;
    }
}
