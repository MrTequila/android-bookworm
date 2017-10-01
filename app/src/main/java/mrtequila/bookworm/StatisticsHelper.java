package mrtequila.bookworm;

import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Michal on 2017-10-01.
 */

public class StatisticsHelper {
    private BooksDataSource dataSource;

    public StatisticsHelper(BooksDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<BarEntry> getPagesReadPerMonth (int year){
        final ArrayList<Book> booksList = dataSource.getAllBooksArray();

        final Map<Integer, Integer> chartData = new TreeMap<>();
        for (int i=1; i<=12; i++)
            chartData.put(i, 0);

        ArrayList<BarEntry> chartEntry = new ArrayList<>();

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


            String yearFinished = new SimpleDateFormat("yyyy").format(dateFinishDate);
            int yearFinishedInt = Integer.parseInt(yearFinished);

            if(yearFinishedInt == year){

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

        }

        for (Map.Entry<Integer, Integer> entry : chartData.entrySet() ){
            chartEntry.add(new BarEntry(entry.getKey(), entry.getValue()));
            //System.out.println("Key: " + entry.getKey() + " value: " + entry.getValue());
        }

        return chartEntry;
    }
}
