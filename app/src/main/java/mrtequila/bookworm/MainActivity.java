package mrtequila.bookworm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private BooksDataSource dataSource;
    private MySQLiteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText startDate = (EditText) findViewById(R.id.startDate);
        startDate.setShowSoftInputOnFocus(false);
        hideKeyboard(this);

        helper = new MySQLiteHelper(this);
        dataSource = new BooksDataSource(helper);
        dataSource.open();

        List<Book> values = dataSource.getAllBooks();


    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    /** Called when the user taps Send button */
    public void sendMessage(View view) throws ParseException {
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        EditText bookAuthor = (EditText) findViewById(R.id.bookAuthor);
        EditText bookTitle = (EditText) findViewById(R.id.bookTitle);
        EditText startDate = (EditText) findViewById(R.id.startDate);
        EditText finishDate = (EditText) findViewById(R.id.finishDate);
        EditText pagesNumber = (EditText) findViewById(R.id.pagesNo);

        String strBoookAuthor = bookAuthor.getText().toString();
        String strBoookTitle = bookTitle.getText().toString();
        String strStartDate = startDate.getText().toString();
        String strFinishDate =  finishDate.getText().toString();
        String strPagesNumber = pagesNumber.getText().toString();

        intent.putExtra("bookAuthor", strBoookAuthor);
        intent.putExtra("bookTitle", strBoookTitle);
        intent.putExtra("startDate", strStartDate);
        intent.putExtra("finishDate", strFinishDate);
        intent.putExtra("pagesNumber", strPagesNumber);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStartDate = null;
        Date dateFinishDate = null;
        // try to parse start and finish date from fields - if not empty
        // to check if start date is before finish date
        try {
            dateStartDate = simpleDateFormat.parse(strStartDate);
            dateFinishDate = simpleDateFormat.parse(strFinishDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(strStartDate) && !TextUtils.isEmpty(strFinishDate)){
            if (dateFinishDate.before(dateStartDate)){
                CharSequence text = "Start date should be before finish date.";
                startDate.setError(text);
                Toast.makeText(context, text, duration).show();
            }
        }


        if (TextUtils.isEmpty(strBoookAuthor)) {
            CharSequence text = "Add Book Author !";
            bookAuthor.setError(text);
            Toast.makeText(context, text, duration).show();
        }
        if (TextUtils.isEmpty(strBoookTitle)) {
            CharSequence text = "Add Book Title !";
            bookTitle.setError(text);
            Toast.makeText(context, text, duration).show();
        }
        if (TextUtils.isEmpty(strStartDate)) {
            CharSequence text = "Add Starting Date!";
            startDate.setError(text);
            Toast.makeText(context, text, duration).show();
        }
        if (TextUtils.isEmpty(strFinishDate)) {
            CharSequence text = "Add Finish Date !";
            finishDate.setError(text);
            Toast.makeText(context, text, duration).show();
        }
        if (TextUtils.isEmpty(strPagesNumber)) {
            CharSequence text = "Add Number of Pages !";
            pagesNumber.setError(text);
            Toast.makeText(context, text, duration).show();
        }

        if (!TextUtils.isEmpty(strBoookAuthor)
                && !TextUtils.isEmpty(strBoookTitle)
                && !TextUtils.isEmpty(strStartDate)
                && !TextUtils.isEmpty(strFinishDate)
                && !TextUtils.isEmpty(strPagesNumber)
                && (dateStartDate.before(dateFinishDate) || dateStartDate.equals(dateFinishDate)) ) {
            startActivity(intent);
        }
    }

    public void openList(View view) {
        Intent intent = new Intent(this, BookList2.class);
        startActivity(intent);
    }

    public void openSideBar(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use current date as the default date in the picker
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String inputName = getArguments().getString("inputName");

            // Create new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Initiate calendar and set picked date to it
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Create startDate TextView instance and set picked date
            String inputName = getArguments().getString("inputName");
            int resId = getResources().getIdentifier(inputName, "id", "mrtequila.bookworm");
            TextView startDate = (TextView) getActivity().findViewById(resId);
            startDate.setText(dateFormat.format(calendar.getTime()));

        }

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case (R.id.startDate):
                bundle.putString("inputName", "startDate");
                break;
            case (R.id.finishDate):
                bundle.putString("inputName", "finishDate");
                break;
        }
        newFragment.setArguments(bundle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        hideKeyboard(this);
        newFragment.setTargetFragment(newFragment, 10);
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

