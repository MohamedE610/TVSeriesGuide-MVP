package com.example.e610.tvappseriesguide.DetailedScreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.e610.tvappseriesguide.Data.SeriesRepository;
import com.example.e610.tvappseriesguide.R;

public class DetailedActivity extends AppCompatActivity {

    DetailedFragment detailedFragment;
    SeriesRepository seriesRepository;
    DetailedPresenter detailedPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        detailedFragment=new DetailedFragment();
        seriesRepository =new SeriesRepository(detailedFragment);
        detailedPresenter=new DetailedPresenter(detailedFragment, seriesRepository);

        Bundle bundle=getIntent().getBundleExtra(getString(R.string.bundle));
        detailedFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.detailed_fragment,detailedFragment).commit();

    }
}
