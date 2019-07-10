package com.eiralv.newtrainglog.Display;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.R;

import java.time.LocalDate;
import java.util.ArrayList;

import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.vo.DateData;

public class CalendarFragment extends android.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        Bundle bundle = getArguments();
        String programName = bundle.getString("programName");
        ArrayList<LocalDate> dates = ((MainActivity) getActivity()).dbHandler.getLocalDateToList(programName);

        MCalendarView calendarView = view.findViewById(R.id.calendar);

        for (LocalDate date : dates) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                calendarView.markDate(new DateData(date.getYear(), date.getMonthValue(), date.getDayOfMonth())
                        .setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.BLUE)));
            }
        }


        return view;
    }
}

