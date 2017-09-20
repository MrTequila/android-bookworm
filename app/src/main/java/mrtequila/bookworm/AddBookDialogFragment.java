package mrtequila.bookworm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Michal on 2017-09-10.
 */

public class AddBookDialogFragment extends DialogFragment {
    final static int BARCODE_SCAN_REQ_CODE = 10;
    EditText isbnInput;

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.dialog_add_book, null);

        builder.setView(layoutView);
        isbnInput = (EditText) layoutView.findViewById(R.id.isbn);


        builder.setMessage("Search book by ISBN or add manually")
                .setPositiveButton("Add manually", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // add book
                        mListener.onDialogPositiveClick(AddBookDialogFragment.this);
                    }
                })
                .setNegativeButton("Search book", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;
                String isbnString = isbnInput.getText().toString();
                //Do stuff, possibly set wantToCloseDialog to true then...
                if(TextUtils.isEmpty(isbnString)) {
                    isbnInput.setError("Add ISBN number to search !");
                } else {
                    wantToCloseDialog = true;
                }
                mListener.onDialogNegativeClick(AddBookDialogFragment.this);

                if(wantToCloseDialog)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

        ImageButton button = (ImageButton)layoutView.findViewById(R.id.scanButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBarCodeScan();
            }
        });

        return dialog;
    }

    public void startBarCodeScan(){
        Intent intent = new Intent(getActivity().getApplicationContext(), BarCodeScannerActivity.class);
        startActivityForResult(intent, BARCODE_SCAN_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if(requestCode == BARCODE_SCAN_REQ_CODE && resultCode == Activity.RESULT_OK){

            System.out.println("++++++++++++++++++++++++++++ Fragment +++++++++++++++++++++++++++ " +data.getStringExtra("result"));
            isbnInput.setText(data.getStringExtra("result"));
        //}
    }

    public interface AddBookDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    AddBookDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddBookDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement AddBookDialogListener");
        }
    }

}
