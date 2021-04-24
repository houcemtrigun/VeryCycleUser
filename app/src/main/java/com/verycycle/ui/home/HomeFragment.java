package com.verycycle.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.verycycle.ChoosingTypeOfride;
import com.verycycle.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;
    private CardView card_view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        SetuUI();

        return root;
    }

    private void SetuUI() {
        card_view = root.findViewById(R.id.card_view);

        card_view.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ChoosingTypeOfride.class));
        });

    }
}