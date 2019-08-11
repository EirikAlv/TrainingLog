package com.eiralv.newtrainglog.Log;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.eiralv.newtrainglog.Display.DisplayChooseProgramFragment;
import com.eiralv.newtrainglog.HomeFragment;
import com.eiralv.newtrainglog.Adapter.ListAdapterItem;
import com.eiralv.newtrainglog.Adapter.LoggingListAdapter;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.MyBottomNavigationView;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class LogWorkout extends Fragment {

    private EditText weightET;
    private EditText repsET;
    private Button addButton;
    private ListView log_workout_listView;
    private TextView tittelTV;
    private LoggingListAdapter loggingListAdapter;
    private ArrayList<ListAdapterItem> list;
    private LogWorkout thisFragment = this;
    private String exerciseTittel;
    private String programTittel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.log_workout_fragment, container, false);

        //bottom navigation
        new MyBottomNavigationView(thisFragment, view).selectedTab("Log");

        Bundle bundle = getArguments();
        this.exerciseTittel = bundle.getString("exerciseTittel");
        this.programTittel = bundle.getString("programTittel");
        tittelTV = view.findViewById(R.id.tittelTV);
        tittelTV.setText(exerciseTittel);

        //handling listview
        log_workout_listView = view.findViewById(R.id.log_workout_listView);
        list = new ArrayList<>();
        readItems();

        loggingListAdapter = new LoggingListAdapter(getActivity(), list, this, exerciseTittel, programTittel);
        log_workout_listView.setAdapter(loggingListAdapter);

        //EDIT TEXT HANDLING
        weightET = view.findViewById(R.id.weightET);
        repsET = view.findViewById(R.id.repsET);


        //((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        weightET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    repsET.requestFocus();
                    return true;
                }
                return false;
            }
        });
        repsET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    addButton.callOnClick();
                    weightET.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //ADD BUTTON HANDLING
        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (weightET.getText().toString().equals("")) {
                    weightET.setText("0");
                }
                if (repsET.getText().toString().equals("")) {
                    repsET.setText("0");
                }

                ((MainActivity) getActivity()).dbHandler.saveToLogging(new Logging(programTittel, tittelTV.getText().toString(),
                        weightET.getText().toString(), repsET.getText().toString(), ((MainActivity) getActivity()).getMesurement()));

                ListAdapterItem item = new ListAdapterItem(weightET.getText().toString(), repsET.getText().toString());
                list.add(0, item);

                weightET.setText("");
                repsET.setText("");

                loggingListAdapter.notifyDataSetChanged();
            }
        });

        //BACK BUTTON HANDLING
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        //saveForTesting();

        return view;
    }

    public void deleteLogLine(ListAdapterItem line) {
        ((MainActivity) getActivity()).dbHandler.deleteLogLine(programTittel, exerciseTittel, line);
    }

    private void readItems() {
        ArrayList<ListAdapterItem> logging = ((MainActivity) getActivity()).dbHandler.getLogginPerExerciseDate(exerciseTittel, programTittel);
        if (!logging.isEmpty()) {
            for (ListAdapterItem s : logging) {
                list.add(0, s);
            }
        }
    }

    //method to add logs for different dates for testing purposes
    /*
    private void saveForTesting() {
        Logging log1 = new Logging(programTittel, tittelTV.getText().toString(), "35", "4", "kg");
        Logging log2 = new Logging(programTittel, tittelTV.getText().toString(), "50", "66", "kg");
        Logging log3 = new Logging(programTittel, tittelTV.getText().toString(), "222", "78", "kg");
        Logging log4 = new Logging(programTittel, tittelTV.getText().toString(), "745", "32", "kg");

        log1.setDato("2019-08-01");
        log2.setDato("2019-08-02");
        log3.setDato("2019-08-03");
        log4.setDato("2019-08-05");

        ((MainActivity) getActivity()).dbHandler.saveToLogging(log1);
        ((MainActivity) getActivity()).dbHandler.saveToLogging(log2);
        ((MainActivity) getActivity()).dbHandler.saveToLogging(log3);
        ((MainActivity) getActivity()).dbHandler.saveToLogging(log4);

    }
    */

}
