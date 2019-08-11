package com.eiralv.newtrainglog.Display;

import android.app.FragmentManager;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
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

import sun.bob.mcalendarview.MCalendarView;

import static java.time.LocalDate.parse;

public class DisplayLogFragment extends android.app.Fragment implements MainActivity.OnBackPressedListener {

    private ListView logListView;
    private ArrayAdapter listAdapter;
    private ArrayList<String> list;
    private String programName;
    private String date;
    private DisplayLogFragment thisFragment = this;
    View cView;
    private boolean fromCalendar;
    private String prior;
    private String next;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        cView = inflater.inflate(R.layout.display_log_fragment, container, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);

        //bottom navigation
        new MyBottomNavigationView(thisFragment, cView).selectedTab("Display");

        Bundle bundle = getArguments();
        this.programName = bundle.getString("programName");
        this.date = bundle.getString("dato");
        this.fromCalendar = bundle.getBoolean("calendar");

        //TEEEST
        ((MainActivity) getActivity()).dbHandler.deleteExercisesIfNotUsed(programName);

        //title
        String dateTitle = null;
        try {
            Date dato = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            dateTitle = new SimpleDateFormat("dd MMM yyyy").format(dato);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView dateLogged = cView.findViewById(R.id.dateLogged);
        dateLogged.setText(dateTitle);

        logListView = cView.findViewById(R.id.logListView);

        list = ((MainActivity) getActivity()).dbHandler.getExercisePerProgramDate(programName, date);

        listAdapter = new DisplayLogRowAdapter(this.getActivity(), list, date, programName);
        logListView.setAdapter(listAdapter);

        //arrow button handling
        ImageButton leftArrow = cView.findViewById(R.id.leftArrow);
        ImageButton rightArrow = cView.findViewById(R.id.rightArrow);
        prior = ((MainActivity) getActivity()).dbHandler.getPriorLog(programName, date);
        next = ((MainActivity) getActivity()).dbHandler.getNextLog(programName, date);
        if(prior == null) {
            leftArrow.setImageResource(0);
        }
        if(next == null) {
            rightArrow.setImageResource(0);
        }

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prior = ((MainActivity) getActivity()).dbHandler.getPriorLog(programName, date);
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
                //String next = ((MainActivity) getActivity()).dbHandler.getNextLog(programName, date);
                if (next != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("dato", next);
                    bundle.putString("programName", programName);
                    ((MainActivity) getActivity()).switchScreen(thisFragment, new DisplayLogFragment(), bundle);
                }
            }
        });

        //calendar button handling
        ImageButton calendarButton = cView.findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("programName", programName);
                //((MainActivity) getActivity()).popupFragment(new CalendarFragment(), bundle);
                ((MainActivity) getActivity()).removeOnBackPressedListener();
                ((MainActivity) getActivity()).switchScreen(thisFragment, new CalendarFragment(), bundle);
            }
        });

        return cView;
    }

    @Override
    public void doBack() {
        if(fromCalendar){
            FragmentManager manager = getActivity().getFragmentManager();
            manager.popBackStack();
            manager.popBackStack();
        }else{
            ((MainActivity) getActivity()).removeOnBackPressedListener();
            getActivity().onBackPressed();
        }
    }
}
