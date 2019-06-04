package com.eiralv.newtrainglog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayLogRowAdapter extends ArrayAdapter<String> {
    private TextView display_log_row_text_view;
    private Context context;
    private ArrayList<String> list;

    public DisplayLogRowAdapter(@NonNull Context context, ArrayList<String> list) {
        super(context, R.layout.custom_basic_list_row, list);
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View customView = convertView;
        if (customView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            customView = layoutInflater.inflate(R.layout.display_log_row, parent, false);
        }
        final String currentText = list.get(position);

        display_log_row_text_view = (TextView) customView.findViewById(R.id.display_log_row_text_view);
        display_log_row_text_view.setText(currentText);

        return customView;
    }



}
