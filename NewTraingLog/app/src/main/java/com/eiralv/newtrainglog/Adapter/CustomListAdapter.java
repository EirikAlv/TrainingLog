package com.eiralv.newtrainglog.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.eiralv.newtrainglog.CreateProgramFragment;
import com.eiralv.newtrainglog.Log.ChooseExerciseFragment;
import com.eiralv.newtrainglog.Log.ChooseProgramFragment;
import com.eiralv.newtrainglog.Log.LogWorkout;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private ImageView deleteButton;
    private TextView customTV;
    private Context context;
    private ArrayList<String> list;
    private Object fragment;

    public CustomListAdapter(@NonNull Context context, ArrayList<String> list, Object fragment) {
        super(context, R.layout.custom_list_row, list);
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
            customView = layoutInflater.inflate(R.layout.custom_list_row, parent, false);
        }


        final String currentText = list.get(position);

        customTV = (TextView) customView.findViewById(R.id.customTV);
        deleteButton = (ImageView ) customView.findViewById(R.id.deleteButton);
        customTV.setText(currentText);
        if(fragment.getClass() == LogWorkout.class) {
            customTV.setTypeface(Typeface.MONOSPACE);
            customTV.setTextSize(20);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fragment.getClass() == ChooseProgramFragment.class) {

                    //Dialog window to ask if you are sure you want to delete program and its content
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setMessage("Are you sure you want to delete this program and its content ?");
                    alertDialogBuilder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((MainActivity)getContext()).dbHandler.deleteProgram(currentText);
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
                /*else if (fragment.getClass() == LogWorkout.class) {
                    //INSERT DELTE METHOD CALL HERE
                    ((LogWorkout) fragment).deleteLogLine(currentText);
                    list.remove(currentText);
                    notifyDataSetChanged();
                }*/
                else if (fragment.getClass() == ChooseExerciseFragment.class) {
                    ((ChooseExerciseFragment) fragment).deleteExercise(currentText);
                    list.remove(currentText);
                    notifyDataSetChanged();
                }else if (fragment.getClass() == CreateProgramFragment.class) {
                    if(!((CreateProgramFragment) fragment).getNothingAdded()) {
                        list.remove(currentText);
                        notifyDataSetChanged();
                    }

                }
            }
        });
        return customView;
    }

}
