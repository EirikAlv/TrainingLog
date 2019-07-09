package com.eiralv.newtrainglog.Display;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eiralv.newtrainglog.Adapter.CustomBasicListAdapapter;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.MyBottomNavigationView;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

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
        new MyBottomNavigationView(thisFragment, view).selectedTab("Display");

        Bundle bundle = getArguments();
        programName = bundle.getString("programName");

        displayDatesListview = view.findViewById(R.id.displayDatesListview);
        list = new ArrayList<>();

        readItems();
        listAdapter = new CustomBasicListAdapapter(getActivity(), list, this);
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
                ((MainActivity) getActivity()).switchScreen(thisFragment, new DisplayLogFragment(), bundle);
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
