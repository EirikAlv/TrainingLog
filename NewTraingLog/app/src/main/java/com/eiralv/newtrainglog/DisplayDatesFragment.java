package com.eiralv.newtrainglog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class DisplayDatesFragment extends android.app.Fragment  {


    private ListView displayDatesListview;
    private ArrayAdapter listAdapter;
    private ArrayList<String> list;
    private String programName;
    private DisplayDatesFragment thisFragment = this;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_dates_fragment, container, false);

        //bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home_item:
                        ((MainActivity)getActivity()).switchScreen(thisFragment, new HomeFragment(), null);
                        break;
                    case R.id.log_item:
                        ((MainActivity)getActivity()).switchScreen(thisFragment, new ChooseProgramFragment(), null);
                        break;
                    case R.id.display_item:
                        ((MainActivity)getActivity()).switchScreen(thisFragment, new DisplayChooseProgramFragment(), null);
                        break;
                }
                return true;
            }
        });
        bottomNavigationView.getMenu().findItem(R.id.display_item).setChecked(true);

        Bundle bundle = getArguments();
        programName = bundle.getString("programName");

        displayDatesListview = (ListView) view.findViewById(R.id.displayDatesListview);
        list = new ArrayList<>();

        readItems();
        listAdapter = new CustomBasicListAdapapter(getActivity(), list, this);
        //listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        displayDatesListview.setAdapter(listAdapter);

        setupListViewListener();

        return view;
    }

    private void setupListViewListener() {
        displayDatesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("dato", list.get(position));
                bundle.putString("programName", programName);
                ((MainActivity)getActivity()).switchScreen(thisFragment, new DisplayLogFragment(), bundle);
            }
        });
    }



    public void readItems() {
        ArrayList<String> logger = ((MainActivity)getActivity()).dbHandler.datesToList(programName);
        for (String s : logger) {
            list.add(s);
        }

    }
}
