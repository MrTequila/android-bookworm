package mrtequila.bookworm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class AddBookActivity extends AppCompatActivity {
    private BooksDataSource dataSource;
    private MySQLiteHelper helper;
    long id;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        EditText bookAuthor = (EditText) findViewById(R.id.bookAuthor);
        EditText bookTitle = (EditText) findViewById(R.id.bookTitle);
        EditText finishDate = (EditText) findViewById(R.id.finishDate);
        EditText pagesNumber = (EditText) findViewById(R.id.pagesNo);
        EditText startDate = (EditText) findViewById(R.id.startDate);

        startDate.setShowSoftInputOnFocus(false);
        hideKeyboard(this);

        Intent intent = getIntent();
        String author = intent.getStringExtra("bookAuthor");
        String title = intent.getStringExtra("bookTitle");
        String start = intent.getStringExtra("startDate");
        String finish = intent.getStringExtra("finishDate");
        String pages = intent.getStringExtra("pagesNumber");
        id = intent.getLongExtra("id", 0);


        bookAuthor.setText(author);
        bookTitle.setText(title);
        finishDate.setText(finish);
        startDate.setText(start);
        pagesNumber.setText(pages);

        helper = new MySQLiteHelper(this);
        dataSource = new BooksDataSource(helper);
        dataSource.open();

        //List<Book> values = dataSource.getAllBooks();


    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    /** Called when the user taps add button */
    public void addBook(View view) throws ParseException {
        Intent intent = new Intent(this, Main2Activity.class);

        EditText bookAuthor = (EditText) findViewById(R.id.bookAuthor);
        EditText bookTitle = (EditText) findViewById(R.id.bookTitle);
        EditText startDate = (EditText) findViewById(R.id.startDate);
        EditText finishDate = (EditText) findViewById(R.id.finishDate);
        EditText pagesNumber = (EditText) findViewById(R.id.pagesNo);

        String strBookAuthor = bookAuthor.getText().toString();
        String strBookTitle = bookTitle.getText().toString();
        String strStartDate = startDate.getText().toString();
        String strFinishDate =  finishDate.getText().toString();
        String strPagesNumber = pagesNumber.getText().toString();

        intent.putExtra("bookAuthor", strBookAuthor);
        intent.putExtra("bookTitle", strBookTitle);
        intent.putExtra("startDate", strStartDate);
        intent.putExtra("finishDate", strFinishDate);
        intent.putExtra("pagesNumber", strPagesNumber);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStartDate = null;
        Date dateFinishDate = null;
        // try to parse start and finish date from fields - if not empty
        // to check if start date is before finish date
        try {
            dateStartDate = simpleDateFormat.parse(strStartDate);
            dateFinishDate = simpleDateFormat.parse(strFinishDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(strStartDate) && !TextUtils.isEmpty(strFinishDate)){
            if (dateFinishDate.before(dateStartDate)){
                CharSequence text = "Start date should be before finish date.";
                startDate.setError(text);
                Toast.makeText(context, text, duration).show();
            }
        }


        if (TextUtils.isEmpty(strBookAuthor)) {
            CharSequence text = "Add Book Author !";
            bookAuthor.setError(text);
            Toast.makeText(context, text, duration).show();
        }
        if (TextUtils.isEmpty(strBookTitle)) {
            CharSequence text = "Add Book Title !";
            bookTitle.setError(text);
            Toast.makeText(context, text, duration).show();
        }
        if (TextUtils.isEmpty(strStartDate)) {
            CharSequence text = "Add Starting Date!";
            startDate.setError(text);
            Toast.makeText(context, text, duration).show();
        }
        if (TextUtils.isEmpty(strFinishDate)) {
            CharSequence text = "Add Finish Date !";
            finishDate.setError(text);
            Toast.makeText(context, text, duration).show();
        }
        if (TextUtils.isEmpty(strPagesNumber)) {
            CharSequence text = "Add Number of Pages !";
            pagesNumber.setError(text);
            Toast.makeText(context, text, duration).show();
        }

        if (!TextUtils.isEmpty(strBookAuthor)
                && !TextUtils.isEmpty(strBookTitle)
                && !TextUtils.isEmpty(strStartDate)
                && !TextUtils.isEmpty(strFinishDate)
                && !TextUtils.isEmpty(strPagesNumber)
                && (dateStartDate.before(dateFinishDate) || dateStartDate.equals(dateFinishDate)) ) {

            // Open db and add book
            helper = new MySQLiteHelper(this);
            dataSource = new BooksDataSource(helper);
            dataSource.open();
            Book book;
            if (id==0){
                book = dataSource.createBook(strBookAuthor, strBookTitle, strStartDate, strFinishDate, Integer.parseInt(strPagesNumber));
            } else {
                book = dataSource.updateBook(id, strBookAuthor, strBookTitle, strStartDate, strFinishDate, Integer.parseInt(strPagesNumber));
            }

            File tempCover = null;
            File finalCover = null;
            String finalCoverDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + book.getId() + ".jpg";
            //tempCover = File.createTempFile("temp", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            tempCover = new File(mCurrentPhotoPath);
            //finalCover = File.createTempFile("__"+Long.toString(book.getId()), ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            finalCover = new File(finalCoverDir);
            //File finalCover = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + book.getId() + ".jpg" );
            
            FileCopy copier = new FileCopy();
            try {
                System.out.println("*********************************************************");
                System.out.println("tempCover" + tempCover.toString());
                System.out.println("finalCover" + finalCover.toString());
                copier.copyFile(tempCover, finalCover);
            } catch (IOException e) {
                e.printStackTrace();
            }


            startActivity(intent);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case (R.id.startDate):
                bundle.putString("inputName", "startDate");
                break;
            case (R.id.finishDate):
                bundle.putString("inputName", "finishDate");
                break;
        }
        newFragment.setArguments(bundle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        hideKeyboard(this);
        newFragment.setTargetFragment(newFragment, 10);
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public void addImageDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File

                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                        "com.example.android.fileprovider",
                                        photoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                    }
                })
                .setNegativeButton("From Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "temp",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++" + mCurrentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
           // Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView coverImage = (ImageView) findViewById(R.id.book_cover);
            if (coverImage != null) {
                //coverImage.setImageBitmap(imageBitmap);
                coverImage.setImageURI(Uri.parse(mCurrentPhotoPath));
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

