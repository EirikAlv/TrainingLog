package com.eiralv.newtrainglog.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class DisplayLogRowAdapter extends ArrayAdapter<String> {
    private TextView display_log_row_text_view;
    private Context context;
    private ArrayList<String> list;
    private String date;

    public DisplayLogRowAdapter(@NonNull Context context, ArrayList<String> list, String date) {
        super(context, R.layout.list_in_list, list);
        this.context = context;
        this.list = list;
        this.date = date;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View customView = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        if (customView == null) {
            customView = layoutInflater.inflate(R.layout.list_in_list, parent, false);
        }

        TextView titleTv = customView.findViewById(R.id.titleTv);
        titleTv.setText(list.get(position));

        ArrayList<ListAdapterItem> tekstList = ((MainActivity)context).dbHandler.
                getLogLinePerExerciseDateNoMesurement(list.get(position), date);
        ArrayList<String> mesureListe = ((MainActivity)context).dbHandler.
                getMesurementPerExerciseDate(list.get(position), date);

        LinearLayout linList = customView.findViewById(R.id.linear_list);
        linList.removeAllViews();

        for(int i = 0; i < tekstList.size(); i++) {
            View line = layoutInflater.inflate(R.layout.display_log_row, null);
            TextView weightTV = line.findViewById(R.id.weightTV);
            TextView mesurementTV = line.findViewById(R.id.mesurementTV);
            TextView repsTV = line.findViewById(R.id.repsTV);
            weightTV.setText(tekstList.get(i).getWeight());
            mesurementTV.setText(mesureListe.get(i));
            repsTV.setText(tekstList.get(i).getReps());

            linList.addView(line);
        }

        return customView;
    }



}
