package com.example.e610.tvappseriesguide.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.example.e610.tvappseriesguide.Data.LocalDataSource.LocalData;
import com.example.e610.tvappseriesguide.Models.SeriesModel;
import com.example.e610.tvappseriesguide.R;

import java.util.ArrayList;


public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    ArrayList<SeriesModel> models = new ArrayList<>();

    Context context;
    LocalData localData;
    public ListRemoteViewsFactory(Context ctx) {
        context = ctx;
        localData=new LocalData(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        models = localData.getFavouriteMovies();

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.series_row_item_widget);
        views.setTextViewText(R.id.series_name_widget, models.get(position).getTitle());
        views.setTextViewText(R.id.series_rating_widget, models.get(position).getVote_average());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
