package com.eiralv.newtrainglog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class LoggingListAdapter extends ArrayAdapter<ListAdapterItem> {

    private ImageView deleteButton;

    private TextView repsView;
    private TextView weightView;
    private Context context;
    private ArrayList<ListAdapterItem> list;
    private Object fragment;

    public LoggingListAdapter(@NonNull Context context, ArrayList<ListAdapterItem> list, Object fragment) {
        super(context, R.layout.logging_list_row, list);
        this.context = context;
        this.list = list;
        this.fragment = fragment;
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

        repsView = customView.findViewById(R.id.repsView);
        weightView = customView.findViewById(R.id.weightsView);
        deleteButton = customView.findViewById(R.id.deleteButton);

        repsView.setText(currentReps);
        weightView.setText(currentWeight);

        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((LogWorkout) fragment).deleteLogLine(currentItem);
                list.remove(currentItem);
                notifyDataSetChanged();

            }
        });
        return customView;
    }

}
