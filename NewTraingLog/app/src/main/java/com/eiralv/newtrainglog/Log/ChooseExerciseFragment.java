package com.eiralv.newtrainglog.Log;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eiralv.newtrainglog.Adapter.CustomBasicListAdapapter;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.MyBottomNavigationView;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class ChooseExerciseFragment extends android.app.Fragment {

    private TextView list_item_title;
    private ListView list_item_ListView;
    private ArrayList<String> list;
    private String tittel;
    private ArrayAdapter<String> listAdapter;

    private final ChooseExerciseFragment thisFragment = this;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_exercise_fragment, container, false);

        new MyBottomNavigationView(thisFragment, view).selectedTab("Log");

        //set title of fragment
        Bundle bundle = getArguments();
        this.tittel = bundle.getString("programTittel");
        list_item_title = view.findViewById(R.id.list_item_title);
        list_item_title.setText(tittel);

        //handling listview, and setting adapter
        list_item_ListView = view.findViewById(R.id.list_item_ListView);
        list = new ArrayList<>();
        readItems();

        listAdapter = new CustomBasicListAdapapter(getActivity(), list, this);
        list_item_ListView.setAdapter(listAdapter);

        setupListViewListener();

        return view;
    }
    public void readItems() {
        ArrayList<String> ovelser = ((MainActivity)getActivity()).dbHandler.getExercisesPerProgram(tittel);
        for (String s : ovelser) {
            list.add(s);
        }
    }
    public void setupListViewListener() {
        list_item_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("exerciseTittel", list.get(position));
                bundle.putString("programTittel", tittel);
                ((MainActivity)getActivity()).switchScreen(thisFragment, new LogWorkout(), bundle);
            }
        });
    }
}
