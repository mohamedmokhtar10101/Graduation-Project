package com.gp.findlost.views.splash;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.fragment.NavHostFragment;

import com.gp.findlost.R;
import com.gp.findlost.databinding.FragmentSplashBinding;
import com.gp.findlost.util.App;
import com.gp.findlost.util.Constants;
import com.gp.findlost.views.basefragment.BaseFragment;

public class SplashFragment extends BaseFragment {
    private FragmentSplashBinding binding;

    public SplashFragment() {
        super(R.layout.fragment_splash, false, false, false);
    }


    @Override
    protected void doOnCreate(View view) {
        binding = DataBindingUtil.bind(view);
    }


    @Override
    public void onResume() {
        super.onResume();
        move();
    }

    private void move() {
        new Handler().postDelayed(() -> {
            Animation RightSwipe = AnimationUtils.loadAnimation(App.getContext(), R.anim.left_to_right);
            binding.parentSplash.startAnimation(RightSwipe);
        }, 1000);

        new Handler().postDelayed(() ->
                        NavHostFragment.findNavController(SplashFragment.this).navigate(R.id.splashToChildren)
                , Constants.SPLASH_DISPLAY_LENGTH);
    }

}