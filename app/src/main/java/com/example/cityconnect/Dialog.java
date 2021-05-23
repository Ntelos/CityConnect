package com.example.cityconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {
    private EditText title, description;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Δήλωση Dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Δήλωση inflater και view για φόρτωση του layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        //Σύνδεση Components
        title = view.findViewById(R.id.given_title);
        description = view.findViewById(R.id.given_description);

        //Πέρασμα του layout μέσω του view στον builder του dialog
        builder.setView(view);

        builder.setTitle("Send a Report");

        //cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_LONG).show();
            }
        });

        //confirm button
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String t = title.getText().toString();
                String d = description.getText().toString();
                listener.takeInfo(t,d);
            }
        });

        return builder.create();
    }

    public interface DialogListener{
        void takeInfo(String title, String description);
    }

    private DialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }
}
