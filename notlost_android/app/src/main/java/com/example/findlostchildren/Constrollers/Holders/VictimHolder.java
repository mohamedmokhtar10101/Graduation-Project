package com.example.findlostchildren.Constrollers.Holders;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findlostchildren.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VictimHolder extends RecyclerView.ViewHolder {

    public CircleImageView sourceImage;
    public TextView sourceName, postTime, victimDescription, victimName;
    public ImageView victimImage;
    public TextView detalisBtn;

    public VictimHolder(@NonNull View view) {
        super(view);
        sourceImage = view.findViewById(R.id.source_image_victim_item);
        sourceName = view.findViewById(R.id.source_name_victim_item);
        postTime = view.findViewById(R.id.post_time_victim_item);
        victimDescription = view.findViewById(R.id.victim_description_victim_item);
        victimName = view.findViewById(R.id.victim_name_victim_item);
        victimImage = view.findViewById(R.id.victim_image_victim_item);
        detalisBtn = view.findViewById(R.id.details_btn_victim_item);
    }
}
