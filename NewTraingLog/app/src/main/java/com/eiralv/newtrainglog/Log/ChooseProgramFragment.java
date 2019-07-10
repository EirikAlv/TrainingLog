package com.eiralv.newtrainglog.Log;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eiralv.newtrainglog.Adapter.ListDeleteAdapter;
import com.eiralv.newtrainglog.Adapter.ListMoreAdapter;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.MyBottomNavigationView;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class ChooseProgramFragment extends android.app.Fragment {

    private TextView list_item_title;
    private ListView list_item_ListView;
    private ArrayList<String> list;
    private ArrayAdapter<String> listAdapter;
    private final ChooseProgramFragment thisFragment = this;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_program_fragment, container, false);

        //bottom navigation
        new MyBottomNavigationView(thisFragment, view).selectedTab("Log");

        //set title of fragment
        list_item_title = view.findViewById(R.id.list_item_title);

        //handling listview, and setting adapter
        list_item_ListView = view.findViewById(R.id.list_item_ListView);
        list = new ArrayList<>();
        readItems();

        listAdapter = new ListMoreAdapter(getActivity(), list, this);
        list_item_ListView.setAdapter(listAdapter);

        setupListViewListener();

        return view;
    }

    private void setupListViewListener() {
        list_item_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("programTittel", list.get(position));
                ((MainActivity)getActivity()).switchScreen(thisFragment, new ChooseExerciseFragment(), bundle);

            }
        });
    }

    private void readItems() {
        ArrayList<String> programer = ((MainActivity)getActivity()).dbHandler.programToList();
        for (String s : programer) {
            list.add(s);
        }
    }

}