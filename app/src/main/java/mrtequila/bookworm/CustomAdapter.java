package mrtequila.bookworm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import static android.R.attr.id;

public class CustomAdapter extends ArrayAdapter<Book> implements View.OnClickListener {

    private ArrayList<Book> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView bookName;
        TextView bookAuthor;
        TextView bookPages;
        ImageView cover;
        RelativeLayout item;
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

    }

    //private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.bookAuthor = (TextView) convertView.findViewById(R.id.author);
            viewHolder.bookName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.bookPages = (TextView) convertView.findViewById(R.id.pages_number);
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.book_cover);
            viewHolder.item = (RelativeLayout) convertView.findViewById(R.id.item_info);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bookAuthor.setText(book.getAuthor());
        viewHolder.bookName.setText(book.getTitle());
        viewHolder.bookPages.setText(Integer.toString(book.getPageNumber()));
        //viewHolder.cover.setOnClickListener(this);
        //viewHolder.cover.setTag(position);

        String finalCoverDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + book.getId() + ".jpg";
        File coverImgFile = new File(finalCoverDir);
        if (coverImgFile.exists()) {
            viewHolder.cover.setImageURI(Uri.parse(finalCoverDir));
        }
        return convertView;
    }
}
