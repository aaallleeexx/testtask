package com.example.alex.testtask.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.testtask.R;
import com.example.alex.testtask.data.CityInfo;
import com.example.alex.testtask.data.ForecastData;
import com.example.alex.testtask.utils.CustomUtils;
import com.example.alex.testtask.web.Web;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class CityInfoFragment extends BaseFragment {
    private static final String ENTER_ARGS_KEY = "city_info_fragment_enter_args_key";
    private RecyclerView mRecyclerView;
    private ArrayList<ForecastData> list = new ArrayList<>();
    private LinearLayout mLlContainer;

    public static CityInfoFragment newInstance(CityInfo cityInfo) {
        Bundle args = new Bundle();
        args.putSerializable(ENTER_ARGS_KEY, cityInfo);

        CityInfoFragment fragment = new CityInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.city_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLlContainer = (LinearLayout) view.findViewById(R.id.forecast_date_ll_container);
        TextView cityName = (TextView) view.findViewById(R.id.cityName);
        ImageView weatherIcon = (ImageView) view.findViewById(R.id.cityIcon);
        TextView temp = (TextView) view.findViewById(R.id.cityTempreture);
        TextView citySky = (TextView) view.findViewById(R.id.citySky);
        TextView time = (TextView) view.findViewById(R.id.cityGetTime);
        TextView wind = (TextView) view.findViewById(R.id.cityWind);
        TextView cloudiness = (TextView) view.findViewById(R.id.citySkyCloudness);
        TextView pressure = (TextView) view.findViewById(R.id.cityPressure);
        TextView humidity = (TextView) view.findViewById(R.id.cityHumidity);
        TextView sunrise = (TextView) view.findViewById(R.id.citySunrise);
        TextView sunset = (TextView) view.findViewById(R.id.citySunset);
        TextView coord = (TextView) view.findViewById(R.id.cityGeo);

        if (getArguments() != null) {
            CityInfo mCityInfo = (CityInfo) getArguments().getSerializable(ENTER_ARGS_KEY);
            tryToGetForecastData(mCityInfo.getCityName());

            Picasso.with(getActivity()).load(mCityInfo.getWeatherIconURL()).into(weatherIcon);
            cityName.setText(mCityInfo.getCityName());
            temp.setText(mCityInfo.getFormattedTemp());
            citySky.setText(mCityInfo.getCityMainWeather());
            time.setText(CustomUtils.getDate(mCityInfo.getDate()));
            wind.setText(mCityInfo.getFormattedWindSpeed() + ", " + CustomUtils.formatBearing(mCityInfo.getCityWindDeg()));
            cloudiness.setText(mCityInfo.getCityDescr());
            pressure.setText(mCityInfo.getFormattedPressure());
            humidity.setText(mCityInfo.getFormattedHuidity());
            sunrise.setText(CustomUtils.getMMMHmmDate(mCityInfo.getCitySunrise()));
            sunset.setText(CustomUtils.getMMMHmmDate(mCityInfo.getCitySunset()));
            coord.setText(mCityInfo.getFormattedPos());
        }

        mFabButton.setVisibility(View.GONE);
    }

    private void fillForecastDataContainer(ArrayList<ForecastData> list) {
        for (ForecastData data : list) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.forecast_item, null);

            ((TextView) v.findViewById(R.id.forecast_date)).setText(data.getFormattedDate());
            ((TextView) v.findViewById(R.id.forecast_temp_max)).setText(data.getFormattedMaxTemp());
            ((TextView) v.findViewById(R.id.forecast_temp_min)).setText(data.getFormattedMinTemp());
            Picasso.with(getContext()).load(data.getWeatherIcon())
                    .into(((ImageView) v.findViewById(R.id.forecast_icon)));

            mLlContainer.addView(v);
        }
    }

    private void tryToGetForecastData(final String cityName) {
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                return Web.newInstance().getForecastData(cityName);
            }

            @Override
            protected void onPostExecute(JSONObject object) {
                super.onPostExecute(object);
                if (object != null) {
                    list = ForecastData.newInstance().getForecastDataList(object);
                    fillForecastDataContainer(list);
                }
            }
        }.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFabButton.setVisibility(View.VISIBLE);
    }
}
