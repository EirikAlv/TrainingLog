package com.eiralv.newtrainglog;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;

import com.eiralv.newtrainglog.Display.DisplayChooseProgramFragment;
import com.eiralv.newtrainglog.Log.ChooseProgramFragment;

public class MyBottomNavigationView {
    private BottomNavigationView bottomNavigationView;

    public MyBottomNavigationView(final Fragment thisFragment, final View view) {

        bottomNavigationView = view.findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home_item:
                        HomeFragment.hideKeyboardFrom(view.getContext(), view);
                        ((MainActivity) view.getContext()).switchScreen(thisFragment, new HomeFragment(), null);
                        break;
                    case R.id.log_item:
                        HomeFragment.hideKeyboardFrom(view.getContext(), view);
                        ((MainActivity) view.getContext()).switchScreen(thisFragment, new ChooseProgramFragment(), null);
                        break;
                    case R.id.display_item:
                        HomeFragment.hideKeyboardFrom(view.getContext(), view);
                        ((MainActivity) view.getContext()).switchScreen(thisFragment, new DisplayChooseProgramFragment(), null);
                        break;
                }
                return true;
            }
        });
        //bottomNavigationView.getMenu().findItem(R.id.display_item).setChecked(true);
    }

    public void selectedTab(String selected) {
        switch (selected) {
            case "Log":
                bottomNavigationView.getMenu().findItem(R.id.log_item).setChecked(true);
                break;

            case "Display":
                bottomNavigationView.getMenu().findItem(R.id.display_item).setChecked(true);
                break;
        }
    }
    public void resetSelected() {
        bottomNavigationView.getMenu().findItem(R.id.display_item).setChecked(false);
    }
}
