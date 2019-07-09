package com.eiralv.newtrainglog.Display;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.eiralv.newtrainglog.Adapter.DisplayLogRowAdapter;
import com.eiralv.newtrainglog.HomeFragment;
import com.eiralv.newtrainglog.Log.ChooseProgramFragment;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.MyBottomNavigationView;
import com.eiralv.newtrainglog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static java.time.LocalDate.parse;

public class DisplayLogFragment extends android.app.Fragment {

    private ListView logListView;
    private ArrayAdapter listAdapter;
    private ArrayList<String> list;
    private String programName;
    private String date;
    private DisplayLogFragment thisFragment = this;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_log_fragment, container, false);

        //bottom navigation
        new MyBottomNavigationView(thisFragment, view).selectedTab("Display");

        Bundle bundle = getArguments();
        this.programName = bundle.getString("programName");
        this.date = bundle.getString("dato");

        //title
        String dateTitle = null;
        try {
            Date dato = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            dateTitle = new SimpleDateFormat("dd MMMM yyyy").format(dato);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView dateLogged = view.findViewById(R.id.dateLogged);
        dateLogged.setText(dateTitle);

        logListView = view.findViewById(R.id.logListView);

        list = ((MainActivity) getActivity()).dbHandler.getExercisePerProgramDate(programName, date);

        listAdapter = new DisplayLogRowAdapter(this.getActivity(), list, date);
        logListView.setAdapter(listAdapter);

        //arrow button handling
        ImageButton leftArrow = view.findViewById(R.id.leftArrow);
        ImageButton rightArrow = view.findViewById(R.id.rightArrow);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prior = ((MainActivity) getActivity()).dbHandler.getPriorLog(programName, date);
                if (prior != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("dato", prior);
                    bundle.putString("programName", programName);
                    ((MainActivity) getActivity()).switchScreen(thisFragment, new DisplayLogFragment(), bundle);
                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String next = ((MainActivity) getActivity()).dbHandler.getNextLog(programName, date);
                if (next != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("dato", next);
                    bundle.putString("programName", programName);
                    ((MainActivity) getActivity()).switchScreen(thisFragment, new DisplayLogFragment(), bundle);
                }
            }
        });

        return view;
    }


}
