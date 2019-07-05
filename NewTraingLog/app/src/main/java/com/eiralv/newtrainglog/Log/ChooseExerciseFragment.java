package com.eiralv.newtrainglog.Log;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.eiralv.newtrainglog.Adapter.CustomListAdapter;
import com.eiralv.newtrainglog.Display.DisplayChooseProgramFragment;
import com.eiralv.newtrainglog.HomeFragment;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.MyBottomNavigationView;
import com.eiralv.newtrainglog.Ovelse;
import com.eiralv.newtrainglog.ProgOvelseReg;
import com.eiralv.newtrainglog.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ChooseExerciseFragment extends android.app.Fragment {

    private TextView choose_exercise_title;
    private ListView exercise_item_ListView;
    private ArrayAdapter listAdapter;
    private ArrayList<String> list;
    private String tittel;
    private Button add_exercise_button;
    private ChooseExerciseFragment thisFragment = this;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_exercise_fragment, container, false);

        //bottom navigation
        MyBottomNavigationView bottom = new MyBottomNavigationView(thisFragment, view);

        Bundle bundle = getArguments();
        this.tittel = bundle.getString("programTittel");
        choose_exercise_title = (TextView) view.findViewById(R.id.choose_exercise_title);
        choose_exercise_title.setText(tittel);

        //handling listview, and setting adapter
        exercise_item_ListView = (ListView) view.findViewById(R.id.exercise_item_ListView);
        list = new ArrayList<>();
        //read file
        readItems();

        listAdapter = new CustomListAdapter(getActivity(), list, this);
        exercise_item_ListView.setAdapter(listAdapter);

        setupListViewListener();

        //Add exercise button handling
        add_exercise_button = (Button) view.findViewById(R.id.add_exercise_button);
        add_exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input text:
                //EditText test = new EditText(getActivity());
                //test.setHighlightColor(getResources().getColor(R.color.textColor));
                //final EditText input = test;
                final EditText input = new EditText(getActivity());
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    input.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColor)));
                }
                try{
                    Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(input, R.drawable.cursor);
                }catch (Exception e) {

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
                        Ovelse ovelse = new Ovelse(input.getText().toString());
                        ProgOvelseReg progOvelseReg = new ProgOvelseReg(tittel, input.getText().toString());
                        ((MainActivity)getActivity()).dbHandler.addExercise(ovelse);
                        ((MainActivity)getActivity()).dbHandler.addProgOvelseReg(progOvelseReg);
                        list.clear();
                        readItems();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.alertButtons));
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.alertButtons));

            }
        });
        return view;
    }

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

    public void readItems() {
        ArrayList<String> ovelser = ((MainActivity)getActivity()).dbHandler.getExercisesPerProgram(tittel);
        for (String s : ovelser) {
            list.add(s);
        }
    }

    public void deleteExercise(String text) {
        ((MainActivity)getActivity()).dbHandler.deleteExerciseFromProgram(tittel, text);
    }
}
