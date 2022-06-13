package com.surya.miniproject.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surya.miniproject.R;
import com.surya.miniproject.adapters.DevelopersListAdapter;

public class DeveloperFragment extends Fragment {

    private DevelopersBottomSheet developersBottomSheet;

    public interface DevelopersBottomSheet {
        View getDevelopersBottomSheetView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_developer, container, false);

        // method to initialise the UI Elements
        RecyclerView recyclerView = view.findViewById(R.id.developers_rec_view);

        // setting up the adapter for the recycler view
        DevelopersListAdapter adapter = new DevelopersListAdapter(getContext(), developersBottomSheet.getDevelopersBottomSheetView());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

        developersBottomSheet = (DevelopersBottomSheet) activity;
    }
}