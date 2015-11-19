package com.apptreesoftware.testapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class ErrorDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Oops!")
                .setMessage("There was an error. Please try again.")
                .setPositiveButton("OK",null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
