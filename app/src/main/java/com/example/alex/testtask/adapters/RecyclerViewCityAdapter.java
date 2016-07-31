package com.example.alex.testtask.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.testtask.data.CityInfo;
import com.example.alex.testtask.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewCityAdapter extends RecyclerView.Adapter {
    private List<CityInfo> mCityInfo;
    private Context mContext;
    MyOnItemClickListener mMyOnItemClickListener;

    public RecyclerViewCityAdapter(List<CityInfo> mCityInfo, Context context) {
        this.mCityInfo = mCityInfo;
        this.mContext = context;
    }

    public void setNewList(List<CityInfo> list) {
        this.mCityInfo = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).tvCityName.setText(mCityInfo.get(position).getCityName());
        ((ViewHolder) holder).tvTemperature.setText((mCityInfo.get(position).getFormattedTemp()));
        Picasso.with(mContext).load(mCityInfo.get(position).getWeatherIconURL())
                .into(((ViewHolder) holder).ivIcon);
    }

    @Override
    public int getItemCount() {
        return mCityInfo.size();
    }

    public void setMyItemClickListener(final MyOnItemClickListener listener) {
        this.mMyOnItemClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvCityName;
        public TextView tvTemperature;
        public ImageView ivIcon;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvCityName = (TextView) itemView.findViewById(R.id.recycler_tv_city_name);
            tvTemperature = (TextView) itemView.findViewById(R.id.recycler_tv_temperature);
            ivIcon = (ImageView) itemView.findViewById(R.id.recycler_weather_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mMyOnItemClickListener != null) {
                mMyOnItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface MyOnItemClickListener {
        void onItemClick(View view , int position);
    }
}
