package com.eiralv.newtrainglog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.eiralv.newtrainglog.Display.DisplayChooseProgramFragment;
import com.eiralv.newtrainglog.Log.ChooseProgramFragment;


public class HomeFragment extends android.app.Fragment {

    private Button createProgramButton;
    private Button logWorkoutButton;
    private Button displayLogMenuButton;
    private Button displayDatabaseButton;
    private String kg = "kg";
    private String lb = "lb";

    private Switch mesure_switch;
    final HomeFragment thisFragment = this;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout_fragment, container, false);

        hideKeyboardFrom(getActivity(), view);
        //handle create program button
        createProgramButton = (Button) view.findViewById(R.id.createProgramButton);
        createProgramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).switchScreen(thisFragment, new CreateProgramFragment() , null);


            }
        });



        //handle log button
        logWorkoutButton = (Button) view.findViewById(R.id.logWorkoutButton);
        logWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).switchScreen(thisFragment, new ChooseProgramFragment(), null);
            }
        });


        //handle display button
        displayLogMenuButton = (Button) view.findViewById(R.id.displayLogMenuButton);
        displayLogMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).switchScreen(thisFragment, new DisplayChooseProgramFragment(), null);
            }
        });

        /*
        //handle display databaseButton
        displayDatabaseButton = (Button) view.findViewById(R.id.displayDatabaseButton);
        displayDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).switchScreen(thisFragment, new AboutFragment(), null);            }
        });
        */

        //switch handling
        mesure_switch = view.findViewById(R.id.mesure_switch);
        mesure_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mesure_switch.isChecked()) {
                    ((MainActivity)getActivity()).dbHandler.setMesure(lb);
                    ((MainActivity)getActivity()).setMesurement(lb);
                }else {
                    ((MainActivity)getActivity()).dbHandler.setMesure(kg);
                    ((MainActivity)getActivity()).setMesurement(kg);
                }
            }
        });

        //set switch if either kg og lb according to what mainactivity mesure says
        String mesurement = ((MainActivity)getActivity()).getMesurement();
        if(mesurement.equals(kg)) {
            mesure_switch.setChecked(false);
        }else if (mesurement.equals(lb)) {
            mesure_switch.setChecked(true);
        }

        return view;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
