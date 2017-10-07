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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddBookActivity extends AppCompatActivity {
    private BooksDataSource dataSource;
    private MySQLiteHelper helper;
    long id;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    String mCurrentPhotoPath;


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
        String coverLink = intent.getStringExtra("coverLink");

        id = intent.getLongExtra("id", 0);

        bookAuthor.setText(author);
        bookTitle.setText(title);
        finishDate.setText(finish);
        startDate.setText(start);
        pagesNumber.setText(pages);

        File finalCover;
        String finalCoverDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + id + ".jpg";

        finalCover = new File(finalCoverDir);
        final ImageView coverImage = (ImageView) findViewById(R.id.book_cover);
        if (finalCover.exists()) {
            if (coverImage != null) {
                coverImage.setImageURI(Uri.parse(finalCover.toString()));
            }
        } else if (coverLink != null){
            RequestQueue imageQueue = Volley.newRequestQueue(this);
            ImageRequest imageRequest = new ImageRequest(coverLink, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    try {
                        File imageFile = createImageFile();
                        OutputStream stream = null;
                        stream = new FileOutputStream(imageFile);
                        response.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        stream.flush();
                        stream.close();

                        coverImage.setImageURI(Uri.parse(imageFile.getAbsolutePath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast errorToast = Toast.makeText(getApplicationContext(), "Didn't work !", Toast.LENGTH_SHORT);
                            errorToast.show();
                            System.out.println("Didn't work !");
                        }
                    });
            imageRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            imageQueue.add(imageRequest);
        }

        helper = new MySQLiteHelper(this);
        dataSource = new BooksDataSource(helper);

    }


    @Override
    protected void onResume() {
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

        isFieldEmpty(bookAuthor, "Add Book Author!", duration);
        isFieldEmpty(bookTitle, "Add Book Title!", duration);
        isFieldEmpty(startDate, "Add Starting Date!", duration);
        isFieldEmpty(finishDate, "Add Finish Date!", duration);
        isFieldEmpty(pagesNumber, "Add Number of Pages !", duration);

        if (   !TextUtils.isEmpty(strBookAuthor)
            && !TextUtils.isEmpty(strBookTitle)
            && !TextUtils.isEmpty(strStartDate)
            && !TextUtils.isEmpty(strFinishDate)
            && !TextUtils.isEmpty(strPagesNumber)
            && (dateStartDate.before(dateFinishDate) || dateStartDate.equals(dateFinishDate)) ) {

            // Open db and add book
            helper = new MySQLiteHelper(this);
            dataSource = new BooksDataSource(helper);
            Book book;

            if (id==0){
                book = dataSource.createBook(strBookAuthor, strBookTitle, strStartDate,
                        strFinishDate, Integer.parseInt(strPagesNumber));
            } else {
                int bookPages = dataSource.getBook(id).getPageNumber();
                book = dataSource.updateBook(id, strBookAuthor, strBookTitle, strStartDate,
                        strFinishDate, bookPages);
            }

            File tempCover = null;
            File finalCover = null;
            String finalCoverDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .getAbsolutePath() + book.getId() + ".jpg";

            if (mCurrentPhotoPath != null) {
                tempCover = new File(mCurrentPhotoPath);
                finalCover = new File(finalCoverDir);
                FileCopy copier = new FileCopy();
                try {
                    copier.copyFile(tempCover, finalCover);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            intent.putExtra("bookAuthor", strBookAuthor);
            intent.putExtra("bookTitle", strBookTitle);
            intent.putExtra("startDate", strStartDate);
            intent.putExtra("finishDate", strFinishDate);
            intent.putExtra("pagesNumber", strPagesNumber);

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
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), REQUEST_IMAGE_GALLERY);
                    }
                });

        builder.show();

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "temp",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
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
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            ImageView coverImage = (ImageView) findViewById(R.id.book_cover);
            if (coverImage != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        File outputFile = createImageFile();
                        OutputStream outputStream = new FileOutputStream(outputFile);
                        FileCopy copier = new FileCopy();
                        copier.copyFileStreams(inputStream, outputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                coverImage.setImageURI(selectedImageUri);
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

    public void isFieldEmpty (EditText textField, String error, int duration){
        if (TextUtils.isEmpty(textField.getText().toString())) {
            textField.setError(error);
            Toast.makeText(getApplicationContext(), error, duration).show();
        }
    }

}

