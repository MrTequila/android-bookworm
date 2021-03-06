package mrtequila.bookworm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddBookDialogFragment.AddBookDialogListener {

    final static int MY_REQUEST_CODE = 100;
    final static int MY_REQUEST_CODE_EXT_STORAGE = 101;
    final static int MY_REQUEST_CODE_INTERNET = 102;
    final static int MY_REQUEST_CODE_ACCESS_NETWORK_STATE = 103;
    private int id = R.id.nav_book_list;
    private final static String STATE_ID = "fragmentId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_REQUEST_CODE);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_EXT_STORAGE);
            }
            if(checkSelfPermission(Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.INTERNET},
                        MY_REQUEST_CODE_INTERNET);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_NETWORK_STATE},
                        MY_REQUEST_CODE_ACCESS_NETWORK_STATE);
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddBookDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        findViewById(R.id.searchProgressBar).setVisibility(View.INVISIBLE);


        FragmentManager fragmentManager = getSupportFragmentManager();
        if(savedInstanceState != null){
            this.id = savedInstanceState.getInt(STATE_ID);
        }

        if(this.id == R.id.nav_book_list){
            Fragment fragment = new BookListFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else if(this.id == R.id.nav_stats){
            Fragment fragment = new BookStatsFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(STATE_ID, this.id);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close
            }
        }
        if (requestCode == MY_REQUEST_CODE_EXT_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted
            }
            else {
                // Like above
            }
        }
        if (requestCode == MY_REQUEST_CODE_INTERNET) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else {

            }
        }

        if (requestCode == MY_REQUEST_CODE_ACCESS_NETWORK_STATE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else {

            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
       this.id = item.getItemId();

        if (id == R.id.nav_book_list) {
            Fragment fragment = new BookListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_stats) {
            Fragment fragment = new BookStatsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void openAddBook(View view) {
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }
    public void showAddBookDialog() {
        DialogFragment dialog = new AddBookDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddBookDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        openAddBook(getCurrentFocus().getRootView());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        EditText isbn = (EditText) dialog.getDialog().findViewById(R.id.isbn);
        String query = isbn.getText().toString();
         if(TextUtils.isEmpty(query)) {
             isbn.setError("Add ISBN number to search !");
         } else {

             getCurrentFocus().getRootView().findViewById(R.id.searchProgressBar).setVisibility(View.VISIBLE);

             RequestQueue queue = Volley.newRequestQueue(this);

             String url = "https://www.justbooks.co.uk/search/?isbn=";
             String link = "&mode=isbn&st=sr&ac=qr";


             StringRequest stringRequest = new StringRequest(Request.Method.GET,
                     url + query + link, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {

                     HTMLParser htmlParser = new HTMLParser(response);

                     Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);
                     intent.putExtra("bookAuthor", htmlParser.getAuthor());
                     intent.putExtra("bookTitle", htmlParser.getTitle());
                     intent.putExtra("coverLink", htmlParser.getCoverLink());

                     getCurrentFocus().getRootView().findViewById(R.id.searchProgressBar).setVisibility(View.GONE);

                     startActivity(intent);

                     System.out.println(htmlParser.getAuthor());
                     System.out.println(htmlParser.getTitle());
                     System.out.println(htmlParser.getCoverLink());

                 }
             }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     getCurrentFocus().getRootView().findViewById(R.id.searchProgressBar).setVisibility(View.GONE);

                     Toast errorToast = Toast.makeText(getApplicationContext(), "Didn't work !", Toast.LENGTH_SHORT);
                     errorToast.show();
                     System.out.println("Didn't work !");
                 }
             });
             stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                     DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                     DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

             queue.add(stringRequest);
         }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
