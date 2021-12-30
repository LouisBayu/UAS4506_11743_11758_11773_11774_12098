package com.udinus.uas4506_11743_11758_11773_11774_12098.Activity;

import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.ArrayList;

public class SearchData {

    private static String[] nama = {
            "Ayam Mentega",
            "Tongseng Daging Sapi",
            "Pie Susu",
            "Es Kuwut",
            "Cumi Asam Manis",
            "Mujair Bakar Kecap",
            "Wedang Ronde",
            "Seblak Ceker",
            "Sayur Lodeh",
            "Garang Asem Ayam"
    };

    private static String[] detail = {
            "Olahan Daging",
            "Olahan Daging",
            "Kue",
            "Minuman",
            "Seafood",
            "Olahan Daging",
            "Minuman",
            "Jajanan",
            "Olahan Sayur",
            "Olahan Daging"
    };

    private static int[] fotoMakanan = {
            R.drawable.ayammentega,
            R.drawable.tongsengsapi,
            R.drawable.piesusu,
            R.drawable.eskuwut,
            R.drawable.cumiasmas,
            R.drawable.mujair,
            R.drawable.ronde,
            R.drawable.seblak,
            R.drawable.lodeh,
            R.drawable.garangasem
    };

    public static ArrayList<SearchList> getSearchData() {
        ArrayList<SearchList> list = new ArrayList<>();
        for (int position = 0; position <nama.length; position++) {
            SearchList searchList = new SearchList();
            searchList.setNama(nama[position]);
            searchList.setDetail(detail[position]);
            searchList.setFoto(fotoMakanan[position]);
            list.add(searchList);
        }
        return list;
    }

}
