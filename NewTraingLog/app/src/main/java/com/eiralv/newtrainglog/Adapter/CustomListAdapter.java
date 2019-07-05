package com.eiralv.newtrainglog.Adapter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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


import com.eiralv.newtrainglog.CreateProgramFragment;
import com.eiralv.newtrainglog.Log.EditExerciseFragment;
import com.eiralv.newtrainglog.Log.ChooseProgramFragment;
import com.eiralv.newtrainglog.MainActivity;
import com.eiralv.newtrainglog.R;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private ImageView deleteButton;
    private TextView customTV;
    private Context context;
    private ArrayList<String> list;
    private Fragment fragment;

    public CustomListAdapter(@NonNull Context context, ArrayList<String> list, Fragment fragment) {
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

        customTV = customView.findViewById(R.id.customTV);
        deleteButton = customView.findViewById(R.id.deleteButton);
        customTV.setText(currentText);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fragment.getClass() == ChooseProgramFragment.class) {

                    final PopupMenu popupMenu = new PopupMenu(v.getContext(), deleteButton);
                    popupMenu.getMenuInflater().inflate(R.menu.edit_prog_popup_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            //Toast.makeText(getContext(), "you clicked this: " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                            if(menuItem.getTitle().equals("Delete")){
                                alertDialogDelete(currentText);
                            }else if(menuItem.getTitle().equals("Edit")){
                                Bundle bundle = new Bundle();
                                bundle.putString("programTittel", currentText);
                                ((MainActivity)context).switchScreen(fragment, new EditExerciseFragment(), bundle);
                            }
                            return true;
                        }
                    });
                    popupMenu.show();

                }

                else if (fragment.getClass() == EditExerciseFragment.class) {
                    ((EditExerciseFragment) fragment).deleteExercise(currentText);
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


    /**
     * @param currentText
     * Dialog window to ask if you are sure you want to delete program and its content
     */
    private void alertDialogDelete(final String currentText) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
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


}
