package com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.EditProfil;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.MainActivity;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.WelcomebackLogin;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import javax.annotation.Nullable;

public class Profil extends Fragment {

    MaterialButton btnEditProfil;
    TextView logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil,container,false);
        btnEditProfil = view.findViewById(R.id.edtProfile);
        logout = view.findViewById(R.id.logout);
        btnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfil.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WelcomebackLogin.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        return view;
    }
}