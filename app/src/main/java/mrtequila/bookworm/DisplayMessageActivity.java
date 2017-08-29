package mrtequila.bookworm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {
    private BooksDataSource dataSource;
    private MySQLiteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Open db
        helper = new MySQLiteHelper(this);
        dataSource = new BooksDataSource(helper);
        dataSource.open();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String author = intent.getStringExtra("bookAuthor");
        String title = intent.getStringExtra("bookTitle");
        String start = intent.getStringExtra("startDate");
        String finish = intent.getStringExtra("finishDate");
        String pages = intent.getStringExtra("pagesNumber");


        // Capture the layout's TextView
        TextView bookAuthor = (TextView) findViewById(R.id.bookAuthor);
        TextView bookTitle = (TextView) findViewById(R.id.bookTitle);
        TextView startDate = (TextView) findViewById(R.id.startDate);
        TextView finishDate = (TextView) findViewById(R.id.finishDate);
        TextView pagesNumber = (TextView) findViewById(R.id.pagesNo);

        // Set the strings as TextViews values
        bookAuthor.setText(author);
        bookTitle.setText(title);
        startDate.setText(start);
        finishDate.setText(finish);
        pagesNumber.setText(pages);

        dataSource.createBook(author, title, start, finish, Integer.parseInt(pages));


    }
}
