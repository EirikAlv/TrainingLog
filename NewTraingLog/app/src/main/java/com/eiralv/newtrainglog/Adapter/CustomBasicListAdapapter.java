package com.eiralv.newtrainglog.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class CustomBasicListAdapapter extends ArrayAdapter<String> {

    private TextView basic_list_row_text_view;
    private Context context;
    private ArrayList<String> list;
    private Object fragment;

    public CustomBasicListAdapapter(@NonNull Context context, ArrayList<String> list, Object fragment) {
        super(context, R.layout.custom_basic_list_row, list);
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
            customView = layoutInflater.inflate(R.layout.custom_basic_list_row, parent, false);
        }
        final String currentText = list.get(position);

        basic_list_row_text_view = (TextView) customView.findViewById(R.id.basic_list_row_text_view);
        basic_list_row_text_view.setText(currentText);

        return customView;
    }
}
