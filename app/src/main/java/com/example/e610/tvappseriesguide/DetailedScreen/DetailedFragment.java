package com.example.e610.tvappseriesguide.DetailedScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.tvappseriesguide.Adapters.SeriesAdapter;
import com.example.e610.tvappseriesguide.Data.SeriesRepository;
import com.example.e610.tvappseriesguide.Models.SeriesModel;
import com.example.e610.tvappseriesguide.Models.TrailerModel;
import com.example.e610.tvappseriesguide.R;
import com.example.e610.tvappseriesguide.Utils.NetworkState;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailedFragment extends Fragment  implements DetailedContract.view {

    DetailedContract.presenter presenter;


    final public String img_String = "http://image.tmdb.org/t/p/w185/";
    static boolean Favourite_Selected = false;
    ImageView Poster_Img;
    TextView Title;
    TextView Overview;
    TextView Release_Date;
    TextView Vote_Rating;
    TextView Trailer_Name;
    ImageView Favourite;
    ImageView button;
    Bundle series_Info;
    SeriesModel seriesModel;
    View view;
    FloatingActionButton actionButton;
    String fragmentType;

    private ArrayList<SeriesModel> series_modelArrayList;
    SeriesAdapter seriesAdapter;
    RecyclerView seriesRecyclerView;

    public DetailedFragment() {
        // Required empty public constructor
    }


    private void InitializeVariables(){
        series_Info = new Bundle();
        seriesModel = new SeriesModel();

        Poster_Img = (ImageView) view.findViewById(R.id.Poster_Image);
        Title = (TextView) view.findViewById(R.id.Title);
        Release_Date = (TextView) view.findViewById(R.id.Release_Date);
        Overview = (TextView) view.findViewById(R.id.Overview);
        Vote_Rating = (TextView) view.findViewById(R.id.Vote_Rating);
        Trailer_Name = (TextView) view.findViewById(R.id.TrailerName);
        Favourite = (ImageView) view.findViewById(R.id.Favourite);
        button = (ImageView) view.findViewById(R.id.button);
        series_Info = this.getArguments();

        actionButton = (FloatingActionButton) view.findViewById(R.id.share_fab);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType(getActivity().getString(R.string.t_p));
                String shareBody = "";
                if (seriesModel != null) {
                    shareBody = seriesModel.getTitle();
                }

                if (shareBody.equals(""))
                    shareBody = getActivity().getString(R.string.content_body);

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getActivity().getString(R.string.Subject));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getActivity().getString(R.string.Share)));
            }
        });

        Favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!seriesModel.Favourite){
                    seriesModel.Favourite=true;
                    presenter.saveModel(getActivity().getString(R.string.save),seriesModel);
                }
                else {
                    seriesModel.Favourite=false;
                    presenter.deleteModel(getActivity().getString(R.string.delete),seriesModel.getId());
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_detailed, container, false);

        InitializeVariables();
        seriesModel=series_Info.getParcelable(getActivity().getString(R.string.model));
        fragmentType=series_Info.getString(getActivity().getString(R.string.type));
        setMovieDetails();

        return view;
    }

    public void setMovieDetails() {
        Picasso.with(view.getContext()).load(img_String + seriesModel.getPoster_ImageUrl())
                .placeholder(R.drawable.loadingicon).error(R.drawable.error).into(Poster_Img);
        Title.setText(seriesModel.getTitle());
        Overview.setText(seriesModel.getOverview());
        Release_Date.setText(seriesModel.getRelease_Date());
        Vote_Rating.setText(seriesModel.getVote_average() + "/10");
    }

    public void setTrailerDetails() {
        Trailer_Name.setText(seriesModel.getTrailerName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getActivity().getString(R.string.youtubeURL) + seriesModel.getTrailerKey())));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(NetworkState.ConnectionAvailable(getContext())){
           presenter.getTrailerData(getActivity().getString(R.string.TrailerModel), seriesModel.getId());
           presenter.loadData(getActivity().getString(R.string.Recommendations),seriesModel.getId());
        }
        else
            Toast.makeText(getContext(), R.string.NOInternetTo,Toast.LENGTH_LONG).show();

        presenter.checkFavourite(getActivity().getString(R.string.check),seriesModel.getId());

    }

    @Override
    public void displayData(String json) {


        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            seriesRecyclerView = (RecyclerView) view.findViewById(R.id.RecommendationsRecyclerView);
            seriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            series_modelArrayList = SeriesModel.ParsingSeriesData(json);
            series_modelArrayList.remove(seriesModel);
            seriesAdapter = new SeriesAdapter(series_modelArrayList, getContext());
            seriesAdapter.setClickListener(new SeriesAdapter.RecyclerViewClickListener() {
                @Override
                public void ItemClicked(View v, int position) {
                    DetailedFragment detailedFragment = new DetailedFragment();
                    SeriesRepository seriesRepository = new SeriesRepository(detailedFragment);
                    DetailedPresenter detailedPresenter = new DetailedPresenter(detailedFragment, seriesRepository);

                    Bundle bundle = new Bundle();
                    SeriesModel model = series_modelArrayList.get(position);
                    ;
                    bundle.putParcelable("model", model);
                    bundle.putString("type", fragmentType);
                    detailedFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.detailed_fragment, detailedFragment);
                }
            });

            seriesRecyclerView.setAdapter(seriesAdapter);


            if (json == null)
                Toast.makeText(getContext(), R.string.NoInternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setTrailerData(String data) {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            TrailerModel trailer = new TrailerModel();
            trailer = TrailerModel.ParsingTrailerData(data);
            seriesModel.setTrailer(trailer);
            setTrailerDetails();
        }
    }

    @Override
    public void setPresenter(DetailedContract.presenter presenter)
    {
     this.presenter=presenter;
    }

    @Override
    public void favouriteChecked(boolean b) {

        if(b){
            Favourite.setImageResource(R.drawable.staron);
            seriesModel.Favourite=true;
        }
        else{

            Favourite.setImageResource(R.drawable.staroff);
            seriesModel.Favourite=false;
        }

    }

    @Override
    public void modelSaved(boolean b) {

        if(b) {
            Toast.makeText(getContext(), R.string.Saved, Toast.LENGTH_SHORT).show();
            Favourite.setImageResource(R.drawable.staron);
        }
        else
            Toast.makeText(getContext(), R.string.Failed,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void modelDeleted(boolean b) {

        if(b) {
            Toast.makeText(getContext(), R.string.deleteSuc, Toast.LENGTH_SHORT).show();
            Favourite.setImageResource(R.drawable.staroff);
        }
        else
            Toast.makeText(getContext(), R.string.deleteFa,Toast.LENGTH_SHORT).show();
    }


}
