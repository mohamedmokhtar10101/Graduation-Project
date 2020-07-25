package com.example.findlostchildren.Constrollers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.findlostchildren.Activities.VictimActivity;
import com.example.findlostchildren.Models.ModelNotification;
import com.example.findlostchildren.Models.VictimModel;
import com.example.findlostchildren.R;
import java.util.ArrayList;

public class AdapterNotificationListView extends ArrayAdapter {

        ArrayList<ModelNotification> mlist = new ArrayList<>();

        public AdapterNotificationListView(@NonNull Context context, int resource, @NonNull ArrayList objects) {
            super(context, resource, objects);

            mlist = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.item_listview_notification, parent,false);

            TextView childName = convertView.findViewById(R.id.poster_name_noti);
            TextView userName = convertView.findViewById(R.id.victim_name_noti);
            ImageView images = convertView.findViewById(R.id.poster_photo_noti);
            TextView phone = convertView.findViewById(R.id.phone_noti);

            childName.setText("Child Name : "+mlist.get(position).getNameChild());
            userName.setText("Someone who knows him  "+mlist.get(position).getUserName());
            Glide.with(getContext()).load(mlist.get(position).getImage()).into(images);
            phone.setText("His Phone Number  "+mlist.get(position).getPhoneNumber());



            return convertView;
        }

}
