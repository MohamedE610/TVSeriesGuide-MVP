package com.example.e610.tvappseriesguide.DetailedScreen;

import com.example.e610.tvappseriesguide.Data.DataSource;
import com.example.e610.tvappseriesguide.Data.SeriesRepository;
import com.example.e610.tvappseriesguide.Models.SeriesModel;

import java.util.ArrayList;

public class DetailedPresenter implements DetailedContract.presenter {

    DetailedFragment detailedFragment;
    SeriesRepository seriesRepository;


    public DetailedPresenter(DetailedFragment fragment, SeriesRepository r){

        detailedFragment=fragment;
        seriesRepository =r;
        detailedFragment.setPresenter(this);
    }

    @Override
    public void loadData(String info ,String info2) {

        seriesRepository.getRemoteData(new DataSource.CallBacks() {
            @Override
            public void remoteDataLoaded(String jsonData) {
                detailedFragment.displayData(jsonData);
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

        },info,info2);
    }

    @Override
    public void getTrailerData(String info1, String info2) {
        seriesRepository.getRemoteData(new DataSource.CallBacks() {
            @Override
            public void remoteDataLoaded(String jsonData) {
                detailedFragment.setTrailerData(jsonData);
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

        },info1,info2);
    }

    @Override
    public void checkFavourite(String Info, String id) {
        seriesRepository.checkFavourite(new DataSource.CallBacks() {
            @Override
            public void remoteDataLoaded(String jsonData) {

            }

            @Override
            public void localDataLoaded(ArrayList<SeriesModel> models) {

            }

            @Override
            public void favouriteChecked(boolean b) {
                detailedFragment.favouriteChecked(b);
            }

            @Override
            public void modelSaved(boolean b) {

            }

            @Override
            public void modelDeleted(boolean b) {

            }
        },Info,id);
    }

    @Override
    public void saveModel(String Info, SeriesModel model) {

        seriesRepository.saveModel(new DataSource.CallBacks() {
            @Override
            public void remoteDataLoaded(String jsonData) {

            }

            @Override
            public void localDataLoaded(ArrayList<SeriesModel> models) {

            }

            @Override
            public void favouriteChecked(boolean b) {

            }

            @Override
            public void modelSaved(boolean b) {
               detailedFragment.modelSaved(b);
            }

            @Override
            public void modelDeleted(boolean b) {

            }
        },Info,model);

    }

    @Override
    public void deleteModel(String Info, String id) {

        seriesRepository.deleteModel(new DataSource.CallBacks() {
            @Override
            public void remoteDataLoaded(String jsonData) {

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
               detailedFragment.modelDeleted(b);
            }
        },Info,id);

    }
}

