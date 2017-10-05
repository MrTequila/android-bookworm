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
    public final String Y_AXIS_SIDE_LEFT = "left";
    public final String Y_AXIS_SIDE_RIGHT = "right";

    public ChartBuilder(BarChart chart){
        this.barChart = chart;
    }

    public ChartBuilder setXAxisPos (XAxis.XAxisPosition position) {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(position);
        return this;
    }

    /**
     * Sets visibility of Y axis, left or right depending on choice
     * @param enabled true or false to show/hide axis
     * @param side use Y_AXIS_SIDE_LEFT or Y_AXIS_SIDE_RIGHT to choose side you want operate on.
     */
    public ChartBuilder setYAxisEnabled(boolean enabled, String side){
        if ( side.equals(Y_AXIS_SIDE_RIGHT) ){
            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setEnabled(enabled);
        } else if (side.equals(Y_AXIS_SIDE_LEFT) ) {
            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setEnabled(enabled);
        } else{
            // return error ?
        }
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

    public ChartBuilder setFitBars(boolean isEnabled) {
        this.barChart.setFitBars(isEnabled);
        return this;
    }

    /**
     * Sets minimum value of Y axis, left or right depending on choice
     * @param minimum floating number, for minimum value to show on Y Axis
     * @param side use Y_AXIS_SIDE_LEFT or Y_AXIS_SIDE_RIGHT to choose side you want operate on.
     */
    public ChartBuilder setYAxisMinimum(float minimum, String side){
        if ( side.equals(Y_AXIS_SIDE_RIGHT) ){
            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setAxisMinimum(minimum);
        } else if (side.equals(Y_AXIS_SIDE_LEFT) ) {
            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setAxisMinimum(minimum);
        } else{
            // return error ?
        }
        return this;
    }
    public BarChart createBarChart() {
        return barChart;
    }

}
