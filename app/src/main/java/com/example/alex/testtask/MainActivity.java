package com.example.alex.testtask;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.example.alex.testtask.fragments.RecyclerFragment;
import com.example.alex.testtask.view.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton mFabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            showFragment(new RecyclerFragment(), "Recycle", true);
        }

        mFabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_add_circle_outline))
                .withButtonColor(Color.TRANSPARENT)
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 36)
                .create();
    }

    public void showFragment(Fragment frg, String tag, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.act_main_container, frg).addToBackStack(tag).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.act_main_container, frg).commit();
        }
        getSupportFragmentManager().executePendingTransactions();
    }

    public FloatingActionButton getFAB() {
        return mFabButton;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
