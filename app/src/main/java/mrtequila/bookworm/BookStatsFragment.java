package mrtequila.bookworm;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

        final Map<Integer, Integer> chartData = new TreeMap<Integer, Integer>();
        List<BarEntry> chartEntry = new ArrayList<>();

        for (Book book:booksList){
            String finish = book.getFinishDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date dateFinishDate = null;

            Calendar finishDateCalendar = Calendar.getInstance();

            // try to parse start and finish date from fields - if not empty
            // to check if start date is before finish date
            try {
                dateFinishDate = simpleDateFormat.parse(finish);


            } catch (ParseException e) {
                e.printStackTrace();
            }

            finishDateCalendar.setTime(dateFinishDate);

            int pagesRead = book.getPageNumber();

            String monthFinished = new SimpleDateFormat("MM").format(dateFinishDate);
            int monthFinishedInt = Integer.parseInt(monthFinished);

            if (chartData.containsKey(monthFinishedInt)){
                pagesRead += chartData.get(monthFinishedInt);
                chartData.put(monthFinishedInt, pagesRead);
            } else {
                chartData.put(monthFinishedInt, pagesRead);
            }

        }

        for (Map.Entry<Integer, Integer> entry : chartData.entrySet() ){
            chartEntry.add(new BarEntry(entry.getKey(), entry.getValue()));
            System.out.println("Key: " + entry.getKey() + " value: " + entry.getValue());
        }


        BarDataSet dataSet = new BarDataSet(chartEntry, "pages read");

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);


        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();

        BarChart barChart = (BarChart) getView().findViewById(R.id.pages_chart);

        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.invalidate();

    }
}
