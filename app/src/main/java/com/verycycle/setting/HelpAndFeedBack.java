package com.verycycle.setting;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.verycycle.R;
import com.verycycle.databinding.HelpFeedBackBinding;

public class HelpAndFeedBack extends Fragment {
    HelpFeedBackBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding  = DataBindingUtil.inflate(inflater, R.layout.help_feed_back, container, false);
        SetuUi();
        return binding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    private void SetuUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(R.color.black);
        }
      /*  MainActivity.float_action.setVisibility(View.GONE);*/
       /* binding.helpRelative.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_helpAndFeedBack_to_helpSetting);
        });*/
    }
}