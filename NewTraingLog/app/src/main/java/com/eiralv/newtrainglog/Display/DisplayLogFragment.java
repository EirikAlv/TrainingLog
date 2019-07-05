package com.eiralv.newtrainglog.Display;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eiralv.newtrainglog.Adapter.DisplayLogRowAdapter;
import com.eiralv.newtrainglog.HomeFragment;
import com.eiralv.newtrainglog.Log.ChooseProgramFragment;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class DisplayLogFragment extends android.app.Fragment {

    private ListView logListView;
    private ArrayAdapter listAdapter;
    private ArrayList<String> list;
    private String programName;
    private String date;
    private DisplayLogFragment thisFragment = this;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_log_fragment, container, false);

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
        this.programName = bundle.getString("programName");
        this.date = bundle.getString("dato");

        logListView = (ListView) view.findViewById(R.id.logListView);
        list = new ArrayList<>();

        readItems();
        listAdapter = new DisplayLogRowAdapter(this.getActivity(), list);
        //listAdapter = new CustomListAdapter(getActivity(), list, this);
        logListView.setAdapter(listAdapter);

        return view;
    }

    public void readItems() {
        ArrayList<String> logger = ((MainActivity)getActivity()).dbHandler.loggingToList(programName, date);
        for (String s : logger) {
            list.add(s);
        }
    }

}
