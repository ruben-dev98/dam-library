package com.example.libreryviewer;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class MyPreferenceFragments extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
