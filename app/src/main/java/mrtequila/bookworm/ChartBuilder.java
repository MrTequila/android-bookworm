package mrtequila.bookworm;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

/**
 * Created by Michal on 2017-10-04.
 */

public class ChartBuilder {
    private BarChart barChart;

    public ChartBuilder(BarChart chart){
        this.barChart = chart;


        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);

        barChart.setFitBars(true);
        barChart.invalidate();
    }

    public ChartBuilder setXAxisPos (XAxis.XAxisPosition position) {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(position);
        return this;
    }

    public ChartBuilder setLabelCount (int count, boolean choice){
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(count, choice);
        return this;
    }

    public ChartBuilder setDescriptionEnabled(boolean isEnabled) {
        this.barChart.getDescription().setEnabled(isEnabled);
        return this;
    }

    public ChartBuilder setChartData (BarData barData){
        this.barChart.setData(barData);
        return this;
    }

}
