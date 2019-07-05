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
import android.widget.ListView;
import android.widget.TextView;

import com.eiralv.newtrainglog.Display.DisplayChooseProgramFragment;
import com.eiralv.newtrainglog.HomeFragment;
import com.eiralv.newtrainglog.Adapter.ListAdapterItem;
import com.eiralv.newtrainglog.Adapter.LoggingListAdapter;
import com.eiralv.newtrainglog.MainActivity;
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
                //hideKeyboardFrom(getActivity(), view);
                ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);

                return true;
            }
        });
        bottomNavigationView.getMenu().findItem(R.id.log_item).setChecked(true);


        Bundle bundle = getArguments();
        this.exerciseTittel = bundle.getString("exerciseTittel");
        this.programTittel = bundle.getString("programTittel");
        tittelTV = view.findViewById(R.id.tittelTV);
        tittelTV.setText(exerciseTittel);


        //handling listview
        log_workout_listView = (ListView) view.findViewById(R.id.log_workout_listView);
        list = new ArrayList<>();
        readItems();

        loggingListAdapter = new LoggingListAdapter(getActivity(), list, this);
        log_workout_listView.setAdapter(loggingListAdapter);

        //EDIT TEXT HANDLING
        weightET = (EditText) view.findViewById(R.id.weightET);
        repsET = (EditText) view.findViewById(R.id.repsET);


        //((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        weightET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    repsET.requestFocus();
                    return true;
                }
                return false;
            }
        });
        repsET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    addButton.callOnClick();
                    weightET.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //ADD BUTTON HANDLING
        addButton = (Button) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Logging logging = new Logging(programTittel, tittelTV.getText().toString(),
                        weightET.getText().toString(), repsET.getText().toString(),((MainActivity)getActivity()).getMesurement());
                ((MainActivity)getActivity()).dbHandler.saveToLogging(logging);

                ListAdapterItem item = new ListAdapterItem(weightET.getText().toString() + " " +
                        ((MainActivity)getActivity()).getMesurement(), repsET.getText().toString() + " reps");
                list.add(0, item);

                weightET.setText("");
                repsET.setText("");

                loggingListAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }
    public void deleteLogLine(ListAdapterItem line) {
        ((MainActivity)getActivity()).dbHandler.deleteLogLine(programTittel, exerciseTittel, line);
    }
    private void readItems() {
        ArrayList<ListAdapterItem> logging = ((MainActivity)getActivity()).dbHandler.getLogginPerExerciseDate(exerciseTittel);
        if(!logging.isEmpty()){
            for (ListAdapterItem s : logging) {
                list.add(0, s);
            }
        }
    }



}