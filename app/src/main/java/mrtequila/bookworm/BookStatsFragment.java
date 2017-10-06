package mrtequila.bookworm;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by Michal on 2017-09-28.
 */

public class BookStatsFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener {
    Context mContext;
    private MySQLiteHelper helper;
    private BooksDataSource dataSource;
    private ArrayList<String> availableYears;

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
        setRetainInstance(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        this.mContext = getActivity().getApplicationContext();
        helper = new MySQLiteHelper(mContext);
        dataSource = new BooksDataSource(helper);
        dataSource.open();
        StatisticsHelper statisticsHelper = new StatisticsHelper(dataSource);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();

        availableYears = statisticsHelper.getAvailableYears();

        Spinner yearSpinner = (Spinner) getActivity().findViewById(R.id.year_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, availableYears);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);
        yearSpinner.setOnItemSelectedListener(this);

        int yearStat = 2017;

        ArrayList<BarEntry> chartEntry = statisticsHelper.getPagesReadPerMonth(yearStat);

        BarDataSet dataSet = new BarDataSet(chartEntry, "pages read");

        BarData barData = new BarData(dataSet);
        buildBarChart(barData);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        System.out.println("position: " + position);
        StatisticsHelper statisticsHelper = new StatisticsHelper(dataSource);
        int yearStat = Integer.parseInt(availableYears.get(position));
        ArrayList<BarEntry> chartEntry = statisticsHelper.getPagesReadPerMonth(yearStat);

        BarDataSet dataSet = new BarDataSet(chartEntry, "pages read");

        BarData barData = new BarData(dataSet);
        buildBarChart(barData);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void buildBarChart(BarData barData) {
        barData.setBarWidth(0.9f);

        ChartBuilder builder = new ChartBuilder((BarChart) getView().findViewById(R.id.pages_chart));
        BarChart barChart = builder.setChartData(barData)
                .setXAxisPos(XAxis.XAxisPosition.BOTTOM)
                .setYAxisEnabled(false, builder.Y_AXIS_SIDE_RIGHT)
                .setYAxisMinimum(0f, builder.Y_AXIS_SIDE_LEFT)
                .setDescriptionEnabled(false)
                .setFitBars(true)
                .createBarChart();

        barChart.invalidate();
    }
}
