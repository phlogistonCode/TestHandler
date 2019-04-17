package com.phlogiston.testhandler;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initViews();

        setSupportActionBar(toolbar);

        Fragment fragmentHotelsList = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragmentHotelsList == null) {
            fragmentHotelsList = new ModelsListFragment();
            getSupportFragmentManager().
                    beginTransaction().add(R.id.container, fragmentHotelsList)
                    .commit();
        }
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}