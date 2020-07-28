package com.gp.findlost.views.image;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.gp.findlost.R;
import com.gp.findlost.databinding.FragmentImageBinding;
import com.gp.findlost.views.basefragment.BaseFragment;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageFragment extends BaseFragment {

    public ImageFragment() {
        super(R.layout.fragment_image, false, false, false);
    }

    private FragmentImageBinding binding;

    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);
        String imageUrl = ImageFragmentArgs.fromBundle(getArguments()).getImage();
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .into(binding.image);
    }
}