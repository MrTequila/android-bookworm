package mrtequila.bookworm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

/**
 * Created by Michal on 2017-09-10.
 */

public class AddBookDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add_book, null));

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
                        mListener.onDialogNegativeClick(AddBookDialogFragment.this);
                    }
                });
        return builder.create();
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
