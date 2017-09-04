package mrtequila.bookworm;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Michal on 2017-09-04.
 */

public class HTMLParser extends AsyncTask<Void, Void, Void> {
    static String url = "https://www.justbooks.co.uk/search/?isbn=";
    static String query = String.format("%s", "9788375065305");
    static String link = String.format("%s", "&mode=isbn&st=sr&ac=qr");


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Document htmlDocument = Jsoup.connect(url + query + link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String url = "https://www.justbooks.co.uk/search/?isbn=";
//        String query = String.format("%s", "9788375065305"); // Oko świata
//        String query = String.format("%s", "9788374801867"); // Maszyna różnicowa
        String query = String.format("%s", "9788375105612"); // Zagłada czarnej kompanii

        String link = String.format("%s", "&mode=isbn&st=sr&ac=qr");
        Document htmlDocument;

        try {
            htmlDocument = Jsoup.connect(url + query + link).get();

            Elements elements = htmlDocument.select("[itemprop = author]");
            Element author = elements.first();

            Elements elementsTitle = htmlDocument.select("[itemprop = name]");
            Element title = elementsTitle.first();

            Element elementImg = htmlDocument.getElementById("coverImage");
            Elements elementsImg = elementImg.getElementsByAttribute("src");

            System.out.println(author.text());
            System.out.println(title.text());
            System.out.println(elementsImg.first().attr("src"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
