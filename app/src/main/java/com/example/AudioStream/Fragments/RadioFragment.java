package com.example.AudioStream.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.AudioStream.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadioFragment extends Fragment {

    public static RadioFragment newInstance() {
        return new RadioFragment();
    }


    public RadioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radio, container, false);
    }

}
