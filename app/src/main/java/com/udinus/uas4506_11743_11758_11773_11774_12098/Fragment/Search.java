package com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import javax.annotation.Nullable;

public class Search extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {   
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}