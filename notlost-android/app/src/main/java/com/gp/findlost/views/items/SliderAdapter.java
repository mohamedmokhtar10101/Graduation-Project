package com.gp.findlost.views.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gp.findlost.R;
import com.gp.findlost.callback.OnImageClickListner;
import com.gp.findlost.databinding.ItemImageBinding;
import com.gp.findlost.util.App;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private List<String> images;
    private OnImageClickListner listner;

    public SliderAdapter(List<String> images, OnImageClickListner listner) {
        this.images = images;
        this.listner = listner;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) App.getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemImageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_image, container, false);
        binding.setImage(images.get(position));
        binding.setImageListner(listner);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(binding.getRoot(), 0);
        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    public void clear() {
        images.clear();
    }
}
