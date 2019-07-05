package com.eiralv.newtrainglog.Log;
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
import android.widget.ListView;
import android.widget.TextView;

import com.eiralv.newtrainglog.Adapter.CustomListAdapter;
import com.eiralv.newtrainglog.Display.DisplayChooseProgramFragment;
import com.eiralv.newtrainglog.HomeFragment;
import com.eiralv.newtrainglog.MainActivity;
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
                return true;
            }
        });
        bottomNavigationView.getMenu().findItem(R.id.log_item).setChecked(true);



        //set title of fragment
        list_item_title = (TextView) view.findViewById(R.id.list_item_title);
        list_item_title.setText("Log: Choose Program");


        //handling listview, and setting adapter
        list_item_ListView = (ListView) view.findViewById(R.id.list_item_ListView);
        list = new ArrayList<>();
        readItems();
        listAdapter = new CustomListAdapter(getActivity(), list, this);
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