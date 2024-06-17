package com.example.libreryviewer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityPreferencias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, new MyPreferenceFragments())
                .commit();
    }
}
