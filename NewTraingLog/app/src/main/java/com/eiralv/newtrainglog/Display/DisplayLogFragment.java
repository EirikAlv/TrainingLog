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
import com.eiralv.newtrainglog.MyBottomNavigationView;
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
        new MyBottomNavigationView(thisFragment, view);


        Bundle bundle = getArguments();
        this.programName = bundle.getString("programName");
        this.date = bundle.getString("dato");

        logListView = view.findViewById(R.id.logListView);

        list = ((MainActivity)getActivity()).dbHandler.getExercisePerProgramDate(programName, date);

        listAdapter = new DisplayLogRowAdapter(this.getActivity(), list, date);
        logListView.setAdapter(listAdapter);

        return view;
    }


}
