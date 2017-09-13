package mrtequila.bookworm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BookDetails extends AppCompatActivity {
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String author = intent.getStringExtra("bookAuthor");
        String title = intent.getStringExtra("bookTitle");
        String start = intent.getStringExtra("startDate");
        String finish = intent.getStringExtra("finishDate");
        String pages = intent.getStringExtra("pagesNumber");
        id = intent.getLongExtra("id", 0);

        //Calculate time read for book
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date dateStartDate = null;
        Date dateFinishDate = null;

        Calendar startDateCalendar = Calendar.getInstance();
        Calendar finishDateCalendar = Calendar.getInstance();

        // try to parse start and finish date from fields - if not empty
        // to check if start date is before finish date
        try {
            dateStartDate = simpleDateFormat.parse(start);
            dateFinishDate = simpleDateFormat.parse(finish);


        } catch (ParseException e) {
            e.printStackTrace();
        }

            startDateCalendar.setTime(dateStartDate);
            finishDateCalendar.setTime(dateFinishDate);


        long readingDurationMili = finishDateCalendar.getTimeInMillis() - startDateCalendar.getTimeInMillis();
        long readingDurationDays = TimeUnit.MILLISECONDS.toDays(readingDurationMili);

        String readingDuration = Long.toString(readingDurationDays);

        // Capture the layout's TextView
        TextView bookAuthor = (TextView) findViewById(R.id.BookAuthorID);
        TextView bookTitle = (TextView) findViewById(R.id.BookTitleID);
        TextView startDate = (TextView) findViewById(R.id.StartDateID);
        TextView finishDate = (TextView) findViewById(R.id.FinishDateID);
        TextView pagesNumber = (TextView) findViewById(R.id.PagesNumberID);
        TextView timeRead = (TextView) findViewById(R.id.TimeReadID);

        // Set the strings as TextViews values
        bookAuthor.setText(author);
        bookTitle.setText(title);
        startDate.setText(start);
        finishDate.setText(finish);
        pagesNumber.setText(pages);
        timeRead.setText(readingDuration);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_book_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_book) {
            Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);

            TextView bookAuthor = (TextView) findViewById(R.id.BookAuthorID);
            TextView bookTitle = (TextView) findViewById(R.id.BookTitleID);
            TextView startDate = (TextView) findViewById(R.id.StartDateID);
            TextView finishDate = (TextView) findViewById(R.id.FinishDateID);
            TextView pagesNumber = (TextView) findViewById(R.id.PagesNumberID);

            String strBookAuthor = bookAuthor.getText().toString();
            String strBookTitle = bookTitle.getText().toString();
            String strStartDate = startDate.getText().toString();
            String strFinishDate =  finishDate.getText().toString();
            String strPagesNumber = pagesNumber.getText().toString();

            intent.putExtra("bookAuthor", strBookAuthor);
            intent.putExtra("bookTitle", strBookTitle);
            intent.putExtra("startDate", strStartDate);
            intent.putExtra("finishDate", strFinishDate);
            intent.putExtra("pagesNumber", strPagesNumber);
            intent.putExtra("id", this.id);

            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

}
