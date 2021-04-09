package com.verycycle.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.verycycle.R;
import com.verycycle.databinding.HelpSeetingBinding;


public class HelpSetting extends Fragment {
    HelpSeetingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.help_seeting, container, false);
        SetuUi();
        return binding.getRoot();
    }

    private void SetuUi() {


    }
}