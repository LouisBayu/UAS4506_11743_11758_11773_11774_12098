package com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.AddResep;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.EditProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.SimpanResep;

import javax.annotation.Nullable;

public class Tersimpan extends Fragment {

    View view;
    MaterialButton btnSimpanResep;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {   
        view = inflater.inflate(R.layout.fragment_saved, container, false);

        btnSimpanResep = view.findViewById(R.id.btnSimpanResep);
        btnSimpanResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SimpanResep.class));
            }
        });

        return view;
    }

}