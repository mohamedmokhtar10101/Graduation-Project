package com.gp.findlost.views.basefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.gp.findlost.R;
import com.gp.findlost.callback.OnNavigationUpdateListner;
import com.gp.findlost.databinding.FragmentBaseBinding;

public abstract class BaseFragment extends Fragment {
    private FragmentBaseBinding binding;

    private int layout;
    private boolean showToolbar;
    private boolean showNavigation;
    private boolean showBack;
    private static OnNavigationUpdateListner listner;
    protected MutableLiveData<String> toolbarName = new MutableLiveData<>();

    public static void setListner(OnNavigationUpdateListner listner) {
        BaseFragment.listner = listner;
    }

    public BaseFragment(int layout, boolean showToolbar, boolean showBack, boolean showNavigation) {
        this.layout = layout;
        this.showToolbar = showToolbar;
        this.showBack = showBack;
        this.showNavigation = showNavigation;
    }

    protected abstract void doOnCreate(View view);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base, container, false);
        listner.onUpdateNavigation(showNavigation);
        LayoutInflater.from(getContext()).inflate(layout, binding.baseFrameLayout, true);
        View view = binding.baseFrameLayout.getChildAt(0);
        doOnCreate(view);

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        binding.setShowToolbar(showToolbar);
        binding.toolbar.setShowBack(showBack);
        binding.toolbar.toolbarBackBtn.setOnClickListener(v -> {
            if (layout == R.layout.fragment_add_item || layout == R.layout.fragment_requests){
                Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.actionMyPosts);
            } else {
                Navigation.findNavController(getActivity(), R.id.navHostFragment).popBackStack();
            }
        });
        toolbarName.observe(getViewLifecycleOwner(), text -> binding.toolbar.setToolbarName(text));
    }
}