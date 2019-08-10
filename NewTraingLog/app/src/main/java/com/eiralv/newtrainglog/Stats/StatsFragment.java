package com.eiralv.newtrainglog.Stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class StatsFragment extends android.app.Fragment {

    private ArrayAdapter<String> adapter;
    private Spinner exerciseDropdown;
    private ArrayList<String> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);

        exerciseDropdown = view.findViewById(R.id.exerciseDropdown);

        items = new ArrayList<>();
        items.add("yes");
        items.add("no");
        items.add("boom");
        items.add("shaka");
        ArrayList<String> testList = ((MainActivity)getActivity()).dbHandler.getAllExercises();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        }
        exerciseDropdown.setAdapter(adapter);

        setupDropdownListener();

        return view;
    }

    private void setupDropdownListener() {
        exerciseDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity(), items.get(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
