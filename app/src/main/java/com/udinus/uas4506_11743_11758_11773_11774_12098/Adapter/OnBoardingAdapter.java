package com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udinus.uas4506_11743_11758_11773_11774_12098.Model.OnBoardingItem;
import com.udinus.uas4506_11743_11758_11773_11774_12098.R;

import java.util.List;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>{

    private List<OnBoardingItem> onboardingItemList;

    public OnBoardingAdapter(List<OnBoardingItem> onboardingItemList) {
        this.onboardingItemList = onboardingItemList;
    }

    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnBoardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slides_onboard_layout, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {
        holder.setOnBoardingData(onboardingItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItemList.size();
    }

    class OnBoardingViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitle;
        private TextView textDesc;
        private ImageView imageOnBoarding;

        public OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.titleOnBoarding);
            textDesc = itemView.findViewById(R.id.descOnBoarding);
            imageOnBoarding = itemView.findViewById(R.id.imageOnBoarding);
        }

        void setOnBoardingData(OnBoardingItem onboardingItem){
            textTitle.setText(onboardingItem.getTitle());
            textDesc.setText(onboardingItem.getDesc());
            imageOnBoarding.setImageResource(onboardingItem.getImage());
        }
    }
}
