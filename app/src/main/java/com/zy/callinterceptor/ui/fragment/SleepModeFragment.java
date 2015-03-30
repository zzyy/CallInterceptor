package com.zy.callinterceptor.ui.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zy.callinterceptor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SleepModeFragment extends PreferenceFragment {


    public SleepModeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.sleep_mode_setting);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sleep_mode, container, false);
    }


}
