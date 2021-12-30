package com.udinus.uas4506_11743_11758_11773_11774_12098.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.SearchData;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Activity.SearchList;
import com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter.SearchAdapter;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class Search extends Fragment {

    RecyclerView allSearch;
    List<SearchList> list = new ArrayList<>();

    private List<SearchList> listSearch = new ArrayList<>();
    SearchAdapter searchAdapter;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {   
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

//        EditText editText = view.findViewById(R.id.edittext);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                filter(s.toString());
//            }
//        });

        allSearch = view.findViewById(R.id.viewCariResep);
        allSearch.setHasFixedSize(true);

        list.addAll(SearchData.getSearchData());
        showRecyclerList();
        return view;

    }

    public void filter(String newText){
        List<SearchList> filteredList = new ArrayList<>();

        for (SearchList list : listSearch) {
            if (list.getNama().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(list);
            }
        }
        searchAdapter.filterList(filteredList);
    }

    private void showRecyclerList() {
        allSearch.setLayoutManager(new RecyclerView.LayoutManager() {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return null;
            }

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);

            }
        });
        allSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        SearchAdapter searchAdapter = new SearchAdapter(list);
        allSearch.setAdapter((searchAdapter));
    }

}