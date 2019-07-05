package com.eiralv.newtrainglog;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private FragmentManager fragmentManager;
    public MyDBHandler dbHandler;
    private String mesurement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new MyDBHandler(this, null, null, 1);

        if(savedInstanceState == null) {
            fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.myContainer, homeFragment);
            fragmentTransaction.commit();
        }
        mesurement = dbHandler.getMesure();

    }


    public void switchScreen(Fragment oldFramgnet, Fragment newFragment, Bundle bundle) {
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null) {
            newFragment.setArguments(bundle);
        }
        fragmentTransaction.remove(oldFramgnet);
        fragmentTransaction.add(R.id.myContainer, newFragment);
        //fragmentTransaction.replace(R.id.myContainer, newFragment);
        fragmentTransaction.addToBackStack("myfragment");
        fragmentTransaction.commit();
    }


    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public String getMesurement() {
        return mesurement;
    }

    public void setMesurement(String mesurement) {
        this.mesurement = mesurement;
    }
}
