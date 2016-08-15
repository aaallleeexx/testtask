package com.example.alex.testtask.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alex.testtask.MainActivity;
import com.example.alex.testtask.R;
import com.example.alex.testtask.adapters.RecyclerViewCityAdapter;
import com.example.alex.testtask.data.CityInfo;
import com.example.alex.testtask.exceptions.CityNotFoundException;
import com.example.alex.testtask.view.dialogs.AddCityDialog;
import com.example.alex.testtask.web.Web;

import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerFragment extends BaseFragment implements View.OnClickListener, RecyclerViewCityAdapter.MyOnItemClickListener {
    public static final int CITY_NAME_REQUEST_CODE = 1000;

    private ArrayList<CityInfo> mCityInfo;
    private String[] mCityName = {"Kiev", "Lvov"};
    private RecyclerViewCityAdapter mRecyclerViewCityAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mFabButton.setOnClickListener(this);

        mCityInfo = new ArrayList<>(mRealm.where(CityInfo.class).findAll());

        if (mCityInfo.isEmpty()) {
            for (String name : mCityName) {
                tryGetWeather(name);
            }
        } else {
            updateData();
        }

        mRecyclerViewCityAdapter = new RecyclerViewCityAdapter(mCityInfo, getActivity());
        mRecyclerViewCityAdapter.setMyItemClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mRecyclerViewCityAdapter);
    }

    private void updateData() {
        for (final CityInfo city : mCityInfo) {
            final String name = city.getCityName();
            new AsyncTask<Void, Void, JSONObject>() {
                @Override
                protected JSONObject doInBackground(Void... voids) {
                    return Web.newInstance().getWeatherByName(name);
                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    super.onPostExecute(jsonObject);

                    if (jsonObject != null) {
                        mRealm.beginTransaction();
                        city.updateData(jsonObject);
                        mRealm.commitTransaction();
                    }
                }
            }.execute();
        }
    }

    @Override
    public void onClick(View view) {
        AddCityDialog dialog = AddCityDialog.newInstance();
        dialog.setTargetFragment(this, CITY_NAME_REQUEST_CODE);
        dialog.show(getFragmentManager(), RecyclerFragment.this.getTag());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != Activity.RESULT_OK) {
            return;
        }

        if (resultCode == CITY_NAME_REQUEST_CODE) {
            if (data.getIntExtra(AddCityDialog.ADD_CITY_DIALOG_BUTTON_KEY, DialogInterface.BUTTON_NEGATIVE)
                    == DialogInterface.BUTTON_POSITIVE) {
                tryGetWeather(data.getStringExtra(AddCityDialog.ADD_CITY_DIALOG_NAME_KEY));
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ((MainActivity) getActivity()).showFragment(CityInfoFragment.newInstance(mCityInfo.get(position)),
                CityInfoFragment.class.toString(), true);
    }

    private void tryGetWeather(final String name) {
        new AsyncTask<Void, Void, CityInfo>() {
            Exception exception;

            @Override
            protected CityInfo doInBackground(Void... voids) {
                try {
                    return new CityInfo(Web.newInstance().getWeatherByName(name));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(CityInfo cityInfo) {
                super.onPostExecute(cityInfo);

                if (exception != null) {
                    if (exception instanceof CityNotFoundException) {
                        Toast.makeText(getContext(), "City not found", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                mRealm.beginTransaction();
                mRealm.insertOrUpdate(cityInfo);
                mRealm.commitTransaction();

                mCityInfo.add(cityInfo);
                mRecyclerViewCityAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
