package com.example.e610.tvappseriesguide.DetailedScreen;

import com.example.e610.tvappseriesguide.Models.SeriesModel;

/**
 * Created by E610 on 10/11/2017.
 */
public interface DetailedContract {

    public  interface view {
        public void displayData(String json);
        public void setTrailerData(String data);
        public void setPresenter(DetailedContract.presenter presenter);

        public void favouriteChecked(boolean b);
        public void modelSaved(boolean b);
        public void modelDeleted(boolean b);
    }

    public interface presenter{
        public void loadData( String info,String info2);
        public void getTrailerData(String info1,String info2);
        public void checkFavourite( String Info ,String id);
        public void saveModel( String Info, SeriesModel model);
        public void deleteModel( String Info, String id);

    }
}
