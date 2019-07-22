package com.eiralv.newtrainglog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eiralv.newtrainglog.Adapter.CustomBasicListAdapapter;
import com.eiralv.newtrainglog.Adapter.ListDeleteAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CreateProgramFragment extends android.app.Fragment {

    private ArrayList<String> exercises;
    private ArrayAdapter<String> exerciseAdapter;
    private ArrayAdapter<String> simpleAdapter;
    private ListView logLV;
    private TextView createProgramTitle;
    private Button submitButton;
    private EditText setET;
    private Button saveButton;
    private Boolean nothingAdded = true;
    private String programName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_program_fragment, container, false);

        //title handling
        createProgramTitle = view.findViewById(R.id.createProgramTitle);
        createProgramTitle.setText(programName);

        //handling listview and setting custom adapter
        logLV = view.findViewById(R.id.logLV);
        exercises = new ArrayList<>();
        exerciseAdapter = new ListDeleteAdapter(getActivity(), exercises, this);
        simpleAdapter = new CustomBasicListAdapapter(getActivity(), exercises, this);
        //logLV.setAdapter(exerciseAdapter);


        //add a dialog box when user opens fragment
        //create a edittex for the dialogbox
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        //setting colors for edittext input
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            input.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColor)));
        }
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(input, R.drawable.cursor);
        } catch (Exception e) {

        }

        //dialog window for input
        final AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                .setMessage("Choose a name for your program")
                .setPositiveButton("save", null) //Set to null. We override the onclick
                .setNegativeButton("cancel", null).setView(input)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                final Button saveButton = (dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                saveButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (!input.getText().toString().equals("")) {
                            ArrayList<String> programList = ((MainActivity) getActivity()).dbHandler.programToList();
                            Boolean funnet = false;
                            for (String s : programList) {
                                if (s.equals(input.getText().toString())) {
                                    funnet = true;
                                    break;
                                }
                            }
                            if (funnet) {
                                Toast.makeText(getActivity(), "program allready exists", Toast.LENGTH_LONG).show();
                            } else {
                                programName = input.getText().toString();
                                createProgramTitle.setText(programName);
                                dialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getActivity(), "cannot save empty name", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                Button cancelButton = (dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        getActivity().onBackPressed();
                    }
                });
                input.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                            saveButton.callOnClick();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
        dialog.show();

        //checking if nothing is added to list and adds an explanation to list for user to easily understand
        if (nothingAdded) {
            logLV.setAdapter(simpleAdapter);
            exercises.add("Add all the exercises for you custom program to this list");
            exercises.add(".....");
            exercises.add(".....");
            logLV.setClickable(false);
            logLV.setEnabled(false);
        }

        //"add" button handling
        setET = view.findViewById(R.id.setET);
        submitButton = view.findViewById(R.id.submitButton);
        setET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    submitButton.callOnClick();
                    return true;
                }
                return false;
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exercise = setET.getText().toString();
                boolean emptyString = exercise.equals("");
                if (emptyString){
                    Toast.makeText(getActivity(), "Exercise name is empty", Toast.LENGTH_LONG).show();
                }
                if (nothingAdded && !emptyString) {
                    exercises.clear();
                    logLV.setAdapter(exerciseAdapter);
                    nothingAdded = false;
                    logLV.setClickable(true);
                    logLV.setEnabled(true);
                }
                boolean allerede = false;
                for (String s : exercises) {
                    if (s.equals(exercise)) {
                        allerede = true;
                        Toast.makeText(getActivity(), "Exercise already added to list", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                if (!allerede && !emptyString) {
                    exerciseAdapter.add(exercise);
                }
                setET.setText("");
            }
        });

        //"addprogram" button handling
        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nothingAdded) {
                    Toast.makeText(getActivity(), "Nothing added to the list", Toast.LENGTH_LONG).show();
                } else {
                    Program program = new Program(programName);
                    ((MainActivity) getActivity()).dbHandler.addProgram(program);
                    for (String exercise : exercises) {
                        Ovelse ovelse = new Ovelse(exercise);
                        ProgOvelseReg progOvelseReg = new ProgOvelseReg(programName, exercise);
                        ((MainActivity) getActivity()).dbHandler.addExercise(ovelse);
                        ((MainActivity) getActivity()).dbHandler.addProgOvelseReg(progOvelseReg);
                    }
                    exercises.clear();
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }

    public Boolean getNothingAdded() {
        return nothingAdded;
    }
}
