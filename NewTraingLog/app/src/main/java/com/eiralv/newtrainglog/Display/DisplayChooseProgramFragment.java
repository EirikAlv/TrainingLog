package com.eiralv.newtrainglog.Display;

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

import com.eiralv.newtrainglog.Adapter.CustomBasicListAdapapter;
import com.eiralv.newtrainglog.HomeFragment;
import com.eiralv.newtrainglog.Log.ChooseProgramFragment;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class DisplayChooseProgramFragment extends android.app.Fragment {


    private ListView displayProgramListview;
    private ArrayList<String> list;
    private ArrayAdapter<String> listAdapter;
    private DisplayChooseProgramFragment thisFragment = this;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_choose_program_fragment, container, false);

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
        bottomNavigationView.getMenu().findItem(R.id.display_item).setChecked(true);


        displayProgramListview = (ListView) view.findViewById(R.id.displayProgramListview);
        list = new ArrayList<>();
        readItems();
        listAdapter = new CustomBasicListAdapapter(getActivity(), list, this);
        //listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        //listAdapter = new CustomListAdapter(getActivity(), list, this);
        displayProgramListview.setAdapter(listAdapter);

        setupListViewListener();

        return view;
    }

    private void setupListViewListener() {
        displayProgramListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("programName", list.get(position));
                ((MainActivity)getActivity()).switchScreen(thisFragment, new DisplayDatesFragment(), bundle);
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