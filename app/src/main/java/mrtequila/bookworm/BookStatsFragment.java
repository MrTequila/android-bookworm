package mrtequila.bookworm;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Michal on 2017-09-28.
 */

public class BookStatsFragment extends android.support.v4.app.Fragment {
    Context mContext;
    private CustomAdapter adapter;
    private MySQLiteHelper helper;
    private BooksDataSource dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_stats, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        this.mContext = getActivity().getApplicationContext();

        helper = new MySQLiteHelper(mContext);
        dataSource = new BooksDataSource(helper);
        dataSource.open();

        final ArrayList<Book> booksList = dataSource.getAllBooksArray();

        Book testOne = booksList.get(0);

        String name = testOne.getAuthor();

        TextView nameText = (TextView) getView().findViewById(R.id.stat_info_1);
        nameText.setText(name);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();

    }
}
