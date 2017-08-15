package mrtequila.bookworm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Book> implements View.OnClickListener {

    private ArrayList<Book> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView bookName;
        TextView bookAuthor;
        TextView bookPages;
        ImageView cover;
    }

    public CustomAdapter(ArrayList<Book> books, Context context) {
        super(context, R.layout.row_item, books);
        this.dataSet = books;
        this.mContext = context;
    }

    @Override
    public void onClick(View view) {

        int position = (Integer) view.getTag();
        Object object = getItem(position);
        Book book = (Book) object;
        Intent intent = new Intent(mContext, BookDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        intent.putExtra("bookAuthor", book.getAuthor());
        intent.putExtra("bookTitle", book.getTitle());
        intent.putExtra("startDate", book.getStartDate());
        intent.putExtra("finishDate", book.getFinishDate());
        intent.putExtra("pagesNumber", Integer.toString(book.getPageNumber()));

        mContext.startActivity(intent);


        // TODO Add book details view after click
        /*switch ( view.getId()){
            case R.id.item_info:
                Toast.makeText(mContext,"click !", Toast.LENGTH_SHORT).show();
                break;
        }*/
    }

    //private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = getItem(position);
        ViewHolder viewHolder;

        //final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.bookAuthor = (TextView) convertView.findViewById(R.id.author);
            viewHolder.bookName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.bookPages = (TextView) convertView.findViewById(R.id.pages_number);
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.item_info);

            //result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //result = convertView;
        }

        viewHolder.bookAuthor.setText(book.getAuthor());
        viewHolder.bookName.setText(book.getTitle());
        viewHolder.bookPages.setText(Integer.toString(book.getPageNumber()));
        viewHolder.cover.setOnClickListener(this);
        viewHolder.cover.setTag(position);

        return convertView;
    }
}
