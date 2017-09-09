package mrtequila.bookworm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class BookListFragment extends ListFragment {
    private BooksDataSource dataSource;
    private static CustomAdapter adapter;
    private MySQLiteHelper helper;
    boolean mDualPane;
    Context mContext;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mContext = getContext();

        helper = new MySQLiteHelper(getContext());
        dataSource = new BooksDataSource(helper);
        dataSource.open();

        final ArrayList<Book> values = dataSource.getAllBooksArray();
        adapter = new CustomAdapter(values, getContext());

        setListAdapter(adapter);

        View detailsFrame = getActivity().findViewById(R.id.list);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

    }

    @Override
    public void onListItemClick (ListView listView, View view, int position, long id) {

        Object object = adapter.getItem(position);
        Book book = (Book) object;
        Intent intent = new Intent(mContext, BookDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        intent.putExtra("bookAuthor", book.getAuthor());
        intent.putExtra("bookTitle", book.getTitle());
        intent.putExtra("startDate", book.getStartDate());
        intent.putExtra("finishDate", book.getFinishDate());
        intent.putExtra("pagesNumber", Integer.toString(book.getPageNumber()));

        mContext.startActivity(intent);
    }

}

