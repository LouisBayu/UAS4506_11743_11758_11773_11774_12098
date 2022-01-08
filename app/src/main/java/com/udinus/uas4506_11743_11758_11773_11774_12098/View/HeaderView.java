package com.udinus.uas4506_11743_11758_11773_11774_12098.View;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HeaderView extends LinearLayout {

    @Bind(R.id.namaResep)
    TextView nama;

    @Bind(R.id.author)
    TextView author;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        this.nama = findViewById(R.id.namaResep);
        this.author = findViewById(R.id.author);
    }

    public void bindTo(String nama, String author){
        this.nama.setText(nama);
        this.author.setText("Oleh : "+author);
    }

    public void setTextSize(float size){
        nama.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

}
