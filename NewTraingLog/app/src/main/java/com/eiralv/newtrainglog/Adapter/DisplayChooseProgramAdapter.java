package com.eiralv.newtrainglog.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class DisplayChooseProgramAdapter extends ArrayAdapter<String> {

    private TextView customTV;
    private Context context;
    private ArrayList<String> list;
    private Object fragment;

    public DisplayChooseProgramAdapter(@NonNull Context context, ArrayList<String> list, Object fragment) {
        super(context, R.layout.list_row_more_icon, list);
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
            customView = layoutInflater.inflate(R.layout.list_row_more_icon, parent, false);
        }
        final String currentText = list.get(position);

        customTV = customView.findViewById(R.id.customTV);
        customTV.setText(currentText);
        ImageView moreButton = customView.findViewById(R.id.moreButton);

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.delete_history_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("Delete")){
                            //delete history of selected program, if program is in history_table, delete in
                            //history table and div
                            alertDialogDelete(currentText);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        return customView;
    }
    /**
     * @param currentText Dialog window to ask if you are sure you want to delete program and its content
     */
    private void alertDialogDelete(final String currentText) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Are you sure you want to delete the history of this program ?");
        alertDialogBuilder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //delete history of selected program, if program is in history_table, delete in
                //history table and div
                ((MainActivity) getContext()).dbHandler.deleteHistoryProgram(currentText);
                ((MainActivity) getContext()).dbHandler.deleteAllLoggedLines(currentText);
                list.remove(currentText);
                notifyDataSetChanged();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getContext().getResources().getColor(R.color.alertButtons));
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.alertButtons));
    }
}