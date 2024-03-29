package com.eiralv.newtrainglog.Log;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.eiralv.newtrainglog.Adapter.ListDeleteAdapter;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.MyBottomNavigationView;
import com.eiralv.newtrainglog.Ovelse;
import com.eiralv.newtrainglog.ProgOvelseReg;
import com.eiralv.newtrainglog.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class EditExerciseFragment extends android.app.Fragment {

    private TextView choose_exercise_title;
    private ListView exercise_item_ListView;
    private ArrayAdapter listAdapter;
    private ArrayList<String> list;
    private String tittel;
    private ImageButton add_exercise_button;
    private ImageButton saveEditButton;
    private EditExerciseFragment thisFragment = this;

    private Ovelse ovelse;
    ProgOvelseReg progOvelseReg;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_exercise_fragment, container, false);

        //bottom navigation
        new MyBottomNavigationView(thisFragment, view).selectedTab("Log");

        final Bundle bundle = getArguments();
        this.tittel = bundle.getString("programTittel");
        choose_exercise_title = view.findViewById(R.id.choose_exercise_title);
        choose_exercise_title.setText(tittel);

        //handling listview, and setting adapter
        exercise_item_ListView = view.findViewById(R.id.exercise_item_ListView);
        list = new ArrayList<>();
        //read file
        readItems();

        listAdapter = new ListDeleteAdapter(getActivity(), list, this);
        exercise_item_ListView.setAdapter(listAdapter);

        //setupListViewListener();

        //Add exercise button handling
        add_exercise_button = view.findViewById(R.id.add_exercise_button);
        add_exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(getActivity());
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setMessage("Exercise name:");
                alertDialogBuilder.setView(input);
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ovelse = new Ovelse(input.getText().toString());
                        progOvelseReg = new ProgOvelseReg(tittel, input.getText().toString());
                        //list.clear();
                        //readItems();
                        list.add(ovelse.getOvelseNavn());
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.alertButtons));
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.alertButtons));

            }
        });
        //Save exercise button handling
        saveEditButton = view.findViewById(R.id.saveEditButton);
        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ovelse != null) {
                    ((MainActivity) getActivity()).dbHandler.addExercise(ovelse);
                    ((MainActivity) getActivity()).dbHandler.addProgOvelseReg(progOvelseReg);
                }
                ((MainActivity) getActivity()).switchScreen(thisFragment, new ChooseProgramFragment(), bundle);
            }
        });
        return view;
    }

    /*
        public void setupListViewListener() {
            exercise_item_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putString("exerciseTittel", list.get(position));
                    bundle.putString("programTittel", tittel);
                    ((MainActivity)getActivity()).switchScreen(thisFragment, new LogWorkout(), bundle);
                }
            });
        }
    */
    public void readItems() {
        ArrayList<String> ovelser = ((MainActivity) getActivity()).dbHandler.getExercisesPerProgram(tittel);
        for (String s : ovelser) {
            list.add(s);
        }
    }

    public void deleteExercise(String text) {
        ((MainActivity) getActivity()).dbHandler.deleteExerciseFromProgram(tittel, text);
    }
}
