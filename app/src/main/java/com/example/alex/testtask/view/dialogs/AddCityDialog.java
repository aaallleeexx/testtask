package com.example.alex.testtask.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.example.alex.testtask.R;
import com.example.alex.testtask.fragments.RecyclerFragment;


public class AddCityDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String ADD_CITY_DIALOG_NAME_KEY = "add_city_dialog_name_key";
    public static final String ADD_CITY_DIALOG_BUTTON_KEY = "add_city_dialog_button_key";
    private EditText mEtCityName;

    public static AddCityDialog newInstance() {

        Bundle args = new Bundle();

        AddCityDialog dialog = new AddCityDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = View.inflate(getActivity(), R.layout.add_city_dialog, null);
        mEtCityName = (EditText) view.findViewById(R.id.add_city_dialog_et_name);

        builder.setView(view);
        builder.setPositiveButton("Add", this);
        builder.setNegativeButton("Cancel", this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int key) {
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(Activity.RESULT_OK,
                    RecyclerFragment.CITY_NAME_REQUEST_CODE,
                    new Intent().putExtra(ADD_CITY_DIALOG_NAME_KEY, mEtCityName.getText().toString())
                            .putExtra(ADD_CITY_DIALOG_BUTTON_KEY, key));
        }
        this.getDialog().dismiss();
    }
}