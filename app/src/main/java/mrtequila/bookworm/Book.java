package mrtequila.bookworm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Michal on 2017-04-04.
 */

public class Book {
    private long id;
    private String author;
    private String title;
    private String startDate;
    private String finishDate;
    private int pageNumber;

    public Book (long id, String author, String title, String startDate, String finishDate, int pageNumber){
        this.id = id;
        this.author = author;
        this.title = title;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.pageNumber = pageNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getFinishYear() {
        String finish = this.getFinishDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFinishDate = null;

        Calendar finishDateCalendar = Calendar.getInstance();

        try {
            dateFinishDate = simpleDateFormat.parse(finish);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        finishDateCalendar.setTime(dateFinishDate);

        String yearFinished = new SimpleDateFormat("yyyy").format(dateFinishDate);
        return yearFinished;
    }

    public String getFinishMonth() {
        String finish = this.getFinishDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFinishDate = null;

        Calendar finishDateCalendar = Calendar.getInstance();

        try {
            dateFinishDate = simpleDateFormat.parse(finish);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        finishDateCalendar.setTime(dateFinishDate);

        String monthFinished = new SimpleDateFormat("MM").format(dateFinishDate);
        return monthFinished;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

        @Override
    public String toString() {
            return title + ", " + author + ", " + pageNumber + " pages.";
        }

}
