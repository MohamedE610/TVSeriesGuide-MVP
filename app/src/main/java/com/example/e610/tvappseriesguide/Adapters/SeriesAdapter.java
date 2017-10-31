package com.example.e610.tvappseriesguide.Adapters;

import android.content.Context;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.e610.tvappseriesguide.Models.SeriesModel;
import com.example.e610.tvappseriesguide.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MyViewHolder> {
    ArrayList<SeriesModel> series_models;
    Context context;
    int LastPosition = -1;
    RecyclerViewClickListener recyclerViewClickListener;

    public SeriesAdapter() {
    }

    public SeriesAdapter(ArrayList<SeriesModel> series_models, Context context) {
        this.series_models = new ArrayList<>();
        this.series_models = series_models;
        this.context = context;
    }

    public void setClickListener(RecyclerViewClickListener clickListener) {
        this.recyclerViewClickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(context).load(holder.imgString + series_models.get(position).getPoster_ImageUrl())
                .placeholder(R.drawable.asd).error(R.drawable.asd).into(holder.PosterImg);
         holder.textView.setText(series_models.get(position).getTitle());
        setAnimation(holder.PosterContainer, position);
    }

    @Override
    public int getItemCount() {
        return series_models.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final public String imgString = context.getString(R.string.imgStringUrl);
        ImageView PosterImg;
        TextView textView;
        CardView PosterContainer;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            PosterImg = (ImageView) itemView.findViewById(R.id.img);
            textView = (TextView) itemView.findViewById(R.id.text);
            PosterContainer = (CardView) itemView.findViewById(R.id.card);
        }

        @Override
        public void onClick(View view) {
            if (recyclerViewClickListener != null)
                recyclerViewClickListener.ItemClicked(view, getAdapterPosition());
        }

        public void clearAnimation() {
            PosterContainer.clearAnimation();
        }
    }

    public interface RecyclerViewClickListener {
        public void ItemClicked(View v, int position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > LastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            LastPosition = position;
        }
    }

    private ArrayList<SeriesModel> models;

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }


    public ArrayList<SeriesModel> getMovies() {
        return models;
    }
}