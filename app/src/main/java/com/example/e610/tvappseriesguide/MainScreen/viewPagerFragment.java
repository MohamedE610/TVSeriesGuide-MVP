package com.example.e610.tvappseriesguide.MainScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.tvappseriesguide.Adapters.SeriesAdapter;
import com.example.e610.tvappseriesguide.Data.SeriesRepository;
import com.example.e610.tvappseriesguide.DetailedScreen.DetailedActivity;
import com.example.e610.tvappseriesguide.DetailedScreen.DetailedFragment;
import com.example.e610.tvappseriesguide.DetailedScreen.DetailedPresenter;
import com.example.e610.tvappseriesguide.Models.SeriesModel;
import com.example.e610.tvappseriesguide.R;
import com.example.e610.tvappseriesguide.Utils.NetworkState;

import java.io.Serializable;
import java.util.ArrayList;


public class viewPagerFragment extends Fragment implements MainContract.View , SeriesAdapter.RecyclerViewClickListener ,Serializable{


    SeriesAdapter seriesAdapter;
    ArrayList<SeriesModel> seriesModels;
    RecyclerView recyclerView;
    View view;
    ProgressBar progressBar;

    String fragmentType="";
    TextView textView;

    public viewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        textView = (TextView) view.findViewById(R.id.main_title);

        if(fragmentType.equals(getString(R.string.Popular)))
            //textView.setText("Most Popular Series");
            textView.setText(R.string.msp);
        else if(fragmentType.equals(getString(R.string.Top)))
                //textView.setText("Top Rated Series");
                textView.setText(R.string.trs);
        else if(fragmentType.equals(getString(R.string.Favourite)))
            //textView.setText("Favourite Series");
            textView.setText(R.string.fs);


        if(NetworkState.ConnectionAvailable(getContext())) {
            if (!fragmentType.equals(getString(R.string.Favourite)) && mainPresenter != null)
                mainPresenter.LoadRemoteData(fragmentType);
        }
        else
            Toast.makeText(getContext(), R.string.NO_Internet,Toast.LENGTH_LONG).show();

        return view;
    }


    @Override
    public void DisplayRemoteData(String jsonData) {

        progressBar.setVisibility(View.GONE);
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            seriesModels = SeriesModel.ParsingSeriesData(jsonData);
            seriesAdapter = new SeriesAdapter(seriesModels, getContext());
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            seriesAdapter.setClickListener(this);
            recyclerView.setAdapter(seriesAdapter);

            //Tablet check
            Bundle bundle = new Bundle();
            SeriesModel model = new SeriesModel();
            if (seriesModels.size() > 0)
                model = seriesModels.get(0);
            bundle.putParcelable(getString(R.string.model), model);
            bundle.putString(getString(R.string.type), fragmentType);
            tabletCheck(bundle);
        }

    }

    @Override
    public void DisplayLocalData(ArrayList<SeriesModel> models) {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            progressBar.setVisibility(View.GONE);

            seriesModels = models;
            seriesAdapter = new SeriesAdapter(models, getContext());
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            seriesAdapter.setClickListener(this);
            recyclerView.setAdapter(seriesAdapter);

            //Tablet check
            Bundle bundle = new Bundle();
            SeriesModel model = new SeriesModel();
            if (seriesModels.size() > 0)
                model = seriesModels.get(0);

            bundle.putParcelable("model", model);
            bundle.putString("type", fragmentType);
            tabletCheck(bundle);
        }
    }

    MainContract.presenter mainPresenter;

    @Override
    public void setPresenter(MainContract.presenter presenter) {

        mainPresenter = presenter;

    }

    @Override
    public void setType(String Type) {
        fragmentType=Type;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(fragmentType.equals(getActivity().getString(R.string.Favourite))&&mainPresenter!=null)
            mainPresenter.LoadLocalData(fragmentType);
    }


    @Override
    public void ItemClicked(View v, int position) {

        Bundle bundle=new Bundle();
        SeriesModel model=seriesModels.get(position);
        bundle.putParcelable(getActivity().getString(R.string.model),model);
        bundle.putString(getActivity().getString(R.string.type),fragmentType);

        //Tablet check
        if(!tabletCheck(bundle)) {
            Intent intent = new Intent(getActivity(), DetailedActivity.class);
            intent.putExtra(getActivity().getString(R.string.bundle), bundle);
            startActivity(intent);
            //Toast.makeText(getContext(), seriesModels.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        }

    }

    DetailedFragment detailedFragment;
    SeriesRepository seriesRepository;
    DetailedPresenter detailedPresenter;
    boolean tabletCheck(Bundle bundle){

        //java.lang.IllegalStateException: Fragment not attached to Activity view pager
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
        //java.lang.IllegalStateException: Fragment not attached to Activity view pager

            if (getResources().getBoolean(R.bool.Tablet)) {
                detailedFragment = new DetailedFragment();
                seriesRepository = new SeriesRepository(detailedFragment);
                detailedPresenter = new DetailedPresenter(detailedFragment, seriesRepository);
                detailedFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.detailed_fragment, detailedFragment).commit();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getActivity().getString(R.string.fragmentType),fragmentType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}