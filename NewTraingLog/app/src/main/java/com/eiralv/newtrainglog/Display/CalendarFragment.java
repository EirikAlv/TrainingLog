package com.eiralv.newtrainglog.Display;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eiralv.newtrainglog.R;

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

        MCalendarView calendarView = view.findViewById(R.id.calendar);
        calendarView.markDate(new DateData(2019, 07, 03).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.BLUE)));

        calendarView.setMarkedStyle(MarkStyle.DOT, Color.GREEN);

        calendarView.markDate(2019, 07, 19);

        return view;
    }
}

