package com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.AddResep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import javax.annotation.Nullable;

public class Add extends Fragment {

    View view;
    MaterialButton btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add, container, false);



        btn = view.findViewById(R.id.buttonAddResep);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddResep.class);
                startActivity(i);
            }
        });

        return view;
    }
}