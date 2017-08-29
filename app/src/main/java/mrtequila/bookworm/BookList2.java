package mrtequila.bookworm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class BookList2 extends AppCompatActivity {
    private BooksDataSource dataSource;
    ListView listView;
    private static CustomAdapter adapter;
    private MySQLiteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list2);

        helper = new MySQLiteHelper(this);
        dataSource = new BooksDataSource(helper);
        dataSource.open();
        listView = (ListView) findViewById(R.id.list);

        final ArrayList<Book> values = dataSource.getAllBooksArray();

        adapter = new CustomAdapter(values, getApplicationContext());

        listView.setAdapter(adapter);


    }
}

