package com.eiralv.newtrainglog.Display;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.R;

import java.time.LocalDate;
import java.util.ArrayList;

import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.vo.DateData;
import sun.bob.mcalendarview.vo.MarkedDates;

public class CalendarFragment extends android.app.Fragment {

    private CalendarFragment thisFragment = this;
    private String programName;
    private MCalendarView calendarView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        Bundle bundle = getArguments();
        programName = bundle.getString("programName");
        ArrayList<LocalDate> dates = ((MainActivity) getActivity()).dbHandler.getLocalDateToList(programName);

        calendarView = view.findViewById(R.id.calendar);
        calendarView.getMarkedDates().getAll().clear();
        //calendarView.setDateCell(R.layout.date_cell);
        //TextView t = view.findViewById(R.id.id_cell_text);




        for (LocalDate date : dates) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                calendarView.markDate(new DateData(date.getYear(), date.getMonthValue(), date.getDayOfMonth())
                        .setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.BLUE)));
            }
        }

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                String month = date.getMonth() < 9 ? "0" + date.getMonth() : date.getMonth() + "";
                String day = date.getDay() < 9 ? "0" + date.getDay() : date.getDay() + "";
                String dato = date.getYear() + "-" + month + "-" + day;
                Bundle bundle = new Bundle();
                bundle.putString("dato", dato);
                bundle.putString("programName", programName);
                bundle.putBoolean("calendar", true);
                MarkedDates md = calendarView.getMarkedDates();
                ArrayList<DateData> liste = md.getAll();
                if(liste.indexOf(date) >= 0) {
                    ((MainActivity) getActivity()).switchScreen(thisFragment, new DisplayLogFragment(), bundle);
                }
            }
        });

        return view;
    }
}

