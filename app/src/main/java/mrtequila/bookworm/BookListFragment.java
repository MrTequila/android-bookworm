package mrtequila.bookworm;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import java.util.ArrayList;


public class BookListFragment extends ListFragment {
    private BooksDataSource dataSource;
    private static CustomAdapter adapter;
    private MySQLiteHelper helper;
    boolean mDualPane;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        helper = new MySQLiteHelper(getContext());
        dataSource = new BooksDataSource(helper);
        dataSource.open();

        final ArrayList<Book> values = dataSource.getAllBooksArray();
        adapter = new CustomAdapter(values, getContext());

        setListAdapter(adapter);

        View detailsFrame = getActivity().findViewById(R.id.list);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

    }

}

