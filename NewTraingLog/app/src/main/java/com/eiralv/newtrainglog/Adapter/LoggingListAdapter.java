package com.eiralv.newtrainglog.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eiralv.newtrainglog.Adapter.ListAdapterItem;
import com.eiralv.newtrainglog.Log.LogWorkout;
import com.eiralv.newtrainglog.Log.LogWorkoutFragment;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoggingListAdapter extends ArrayAdapter<ListAdapterItem> {

    private ImageView deleteButton;

    private TextView repsView;
    private TextView weightView;
    private Context context;
    private ArrayList<ListAdapterItem> list;
    private Object fragment;
    private String tittel;
    private String programName;

    public LoggingListAdapter(@NonNull Context context, ArrayList<ListAdapterItem> list, Object fragment, String tittel, String programName) {
        super(context, R.layout.logging_list_row, list);
        this.context = context;
        this.list = list;
        this.fragment = fragment;
        this.tittel = tittel;
        this.programName = programName;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View customView = convertView;
        if (customView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            customView = layoutInflater.inflate(R.layout.logging_list_row, parent, false);
        }

        final ListAdapterItem currentItem = list.get(position);
        final String currentReps = list.get(position).getReps();
        final String currentWeight = list.get(position).getWeight();

        weightView = customView.findViewById(R.id.weightsView);
        TextView logMesurementTV = customView.findViewById(R.id.logMesurementTV);
        repsView = customView.findViewById(R.id.repsView);
        deleteButton = customView.findViewById(R.id.deleteButton);

        Date dato = new Date();
        ArrayList<String> mesureListe = ((MainActivity)context).dbHandler
                .getMesurementPerExerciseDate(tittel, new SimpleDateFormat("yyyy-MM-dd").format(dato), programName);

        logMesurementTV.setText(mesureListe.get((list.size() - 1) - position));
        weightView.setText(currentWeight);
        repsView.setText(currentReps);

        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //testing new fragment
                //må byttes tilbake hvis du angrer på å dropdown meny for øvelser
                //((LogWorkout) fragment).deleteLogLine(currentItem);
                ((MainActivity) getContext()).dbHandler.deleteLogLine(programName, tittel, currentItem);
                list.remove(currentItem);
                notifyDataSetChanged();

            }
        });
        return customView;
    }

}
