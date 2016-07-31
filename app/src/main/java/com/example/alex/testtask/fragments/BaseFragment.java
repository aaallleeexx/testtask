package com.example.alex.testtask.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.alex.testtask.MainActivity;
import com.example.alex.testtask.view.FloatingActionButton;

public class BaseFragment extends Fragment {
    protected FloatingActionButton mFabButton;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFabButton = ((MainActivity) getActivity()).getFAB();
    }
}
