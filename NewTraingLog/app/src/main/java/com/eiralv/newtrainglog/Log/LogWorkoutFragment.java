package com.eiralv.newtrainglog.Log;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.eiralv.newtrainglog.Adapter.ListAdapterItem;
import com.eiralv.newtrainglog.Adapter.LoggingListAdapter;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.MyBottomNavigationView;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class LogWorkoutFragment extends Fragment {


    private LogWorkoutFragment thisFragment = this;
    private EditText weightET;
    private EditText repsET;
    private Button addButton;
    private String tittel;
    private Spinner exerciseDropdown;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> ovelser;
    private ListView log_workout_listView;
    private ArrayList<ListAdapterItem> list;
    private LoggingListAdapter loggingListAdapter;
    private String currentExercise;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.log_workout_fragment_v2, container, false);

        //bottom navigation
        new MyBottomNavigationView(thisFragment, view).selectedTab("Log");

        Bundle bundle = getArguments();
        this.tittel = bundle.getString("programTittel");
        ovelser = ((MainActivity) getActivity()).dbHandler.getExercisesPerProgram(tittel);

        currentExercise = ovelser.get(0);

        //dropdown
        exerciseDropdown = view.findViewById(R.id.exerciseDropdown);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, ovelser);
        }
        exerciseDropdown.setAdapter(adapter);
        setupDropdownListener();

        //listview
        log_workout_listView = view.findViewById(R.id.log_workout_listView);
        list = new ArrayList<>();
        readItems(currentExercise);

        loggingListAdapter = new LoggingListAdapter(getActivity(), list, this, currentExercise, tittel);
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

                ((MainActivity) getActivity()).dbHandler.saveToLogging(new Logging(tittel, currentExercise,
                        weightET.getText().toString(), repsET.getText().toString(), ((MainActivity) getActivity()).getMesurement()));

                ListAdapterItem item = new ListAdapterItem(weightET.getText().toString(), repsET.getText().toString());
                list.add(0, item);

                weightET.setText("");
                repsET.setText("");

                loggingListAdapter.notifyDataSetChanged();
            }
        });


        return view;
    }

    private void readItems(String ovelseNavn) {
        ArrayList<ListAdapterItem> logging = ((MainActivity) getActivity()).dbHandler.getLogginPerExerciseDate(ovelseNavn, tittel);
        if (!logging.isEmpty()) {
            for (ListAdapterItem s : logging) {
                list.add(0, s);
            }
        }
    }


    private void setupDropdownListener() {
        exerciseDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity(), ovelser.get(position), Toast.LENGTH_LONG).show();
                currentExercise = ovelser.get(position);
                list.clear();
                readItems(ovelser.get(position));
                loggingListAdapter = new LoggingListAdapter(getActivity(), list, this, ovelser.get(position), tittel);
                log_workout_listView.setAdapter(loggingListAdapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
