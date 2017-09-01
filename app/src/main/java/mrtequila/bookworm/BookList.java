package mrtequila.bookworm;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

public class BookList extends ListActivity {
    private BooksDataSource dataSource;
    private MySQLiteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        helper = new MySQLiteHelper(this);
        dataSource = new BooksDataSource(helper);
        dataSource.open();

        List<Book> values = dataSource.getAllBooks();

        System.out.println(values);
        // use the SimpleCursorAdapter to show the elements in a List View
        ArrayAdapter<Book> adapter = new ArrayAdapter<Book>(this,
                R.layout.activity_book_list, R.id.textview, values);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        dataSource.open();
        List<Book> values = dataSource.getAllBooks();
        ArrayAdapter<Book> adapter = new ArrayAdapter<Book>(this,
                R.layout.activity_book_list, R.id.textview, values);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
