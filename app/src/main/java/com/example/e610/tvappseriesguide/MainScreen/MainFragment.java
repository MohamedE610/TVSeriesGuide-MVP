package com.example.e610.tvappseriesguide.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.e610.tvappseriesguide.Data.SeriesRepository;
import com.example.e610.tvappseriesguide.LogInScreen.LoginActivity;
import com.example.e610.tvappseriesguide.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment implements TabLayout.OnTabSelectedListener ,Serializable ,MainContract.mainView{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    View view;


    MainPresenter mainPresenter;

    viewPagerFragment popularFragment;
    MainPresenter popularPresenter;
    SeriesRepository popularSeriesRepository;

    viewPagerFragment topFragment;
    MainPresenter topPresenter;
    SeriesRepository topSeriesRepository;

    viewPagerFragment favouriteFragment;
    MainPresenter favouritePresenter;
    SeriesRepository favouriteSeriesRepository;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        mainPresenter=new MainPresenter(this,null);
        mainPresenter.setUpGoogleApiClient(getActivity());

            popularFragment = new viewPagerFragment();
            popularFragment.setType(getActivity().getString(R.string.Popular));
            popularSeriesRepository = new SeriesRepository(this);
            popularPresenter = new MainPresenter(popularFragment, popularSeriesRepository);

            topFragment = new viewPagerFragment();
            topFragment.setType(getActivity().getString(R.string.Top));
            topSeriesRepository = new SeriesRepository(this);
            topPresenter = new MainPresenter(topFragment, topSeriesRepository);

            favouriteFragment = new viewPagerFragment();
            favouriteFragment.setType(getActivity().getString(R.string.Favourite));
            favouriteSeriesRepository = new SeriesRepository(this);
            favouritePresenter = new MainPresenter(favouriteFragment, favouriteSeriesRepository);

            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_main, container, false);
            toolbar = (Toolbar) view.findViewById(R.id.toolbar);

            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.setSupportActionBar(toolbar);

            viewPager = (ViewPager) view.findViewById(R.id.viewpager);
            setupViewPager(viewPager, 0);

            tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addOnTabSelectedListener(this);


        return view;
    }



    private void setupViewPager(ViewPager viewPager, int i) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.addFragment(popularFragment,getActivity().getString(R.string.pop_));
        adapter.addFragment(topFragment,getActivity().getString(R.string.top_));
        adapter.addFragment(favouriteFragment,getActivity().getString(R.string.Favourite));


        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Runtime.getRuntime().gc();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
       // MenuInflater inflater_ = getActivity().getMenuInflater();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         if (item.getItemId() == R.id.sign_out) {
            mainPresenter.signOut();
        }

        return true;
    }

    @Override
    public void signOutSuccessfully() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
