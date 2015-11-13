package com.example.shubham.bille;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Menu";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;

   // private ProgressBar progressBar;

    TextView merchName,merchEmail;
    ImageView merchImage;
    Bitmap bitmap;


    SessionManager session;
    private int backpresscount = 0;

    final String url = "http://54.68.65.111/mozipper/mongo_api/billing_merchant.php?mid=55e5771b83b115a1048b4567";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        session = new SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.shoppingadd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createbill = new Intent(getApplicationContext(),CreateBill.class);
                startActivity(createbill);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_home_screen, null);
        navigationView.addHeaderView(header);

        merchName = (TextView)header.findViewById(R.id.MerchantName);
        merchEmail = (TextView)header.findViewById(R.id.MerchantEmail);
        merchImage = (ImageView)header.findViewById(R.id.MerchantImg);





        boolean check = session.checkLogin();

        if(check)
        {
            this.finish();
        }
        else
        {

        }

      //  boolean c = session.isLoggedIn();


        //Log.d("testit",""+merchemail);
        HashMap<String, String> user = session.getUserDetails();

        String merchemail = user.get(SessionManager.KEY_Email);

        // email
        String merchname = user.get(SessionManager.KEY_MerchantName);

        String imgurl = user.get(SessionManager.KEY_LogoUrl);

        merchName.setText(merchname);
        merchEmail.setText(merchemail);

        new LoadImage().execute(imgurl);


        /*if(c)
        {
            String sharedname,sharedemail;
           sharedname = sharedpreferences.getString(Name,"");
           sharedemail = sharedpreferences.getString(Email,"");
            merchName.setText(sharedname);
            merchEmail.setText(sharedemail);
        }
        else {
            Log.d("click", "clicked");
            merchName.setText(merchname);
            merchEmail.setText(merchemail);
            //  setContentView(merchEmail);
        }*/




        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

       // progressBar = (ProgressBar) findViewById(R.id.progress_bar);
       // progressBar.setVisibility(View.VISIBLE);

        new AsyncHttpTask().execute(url);





    }

    @Override
    public void onResume()
    {
        super.onResume();

        boolean check = session.checkLogin();

        if(check)
        {
            this.finish();
        }
        else
        {
            /*Log.d("sdhs","kgfogfow");
            Intent i = getIntent();
            String merchemail = i.getStringExtra("email");
            Log.d("testit",""+merchemail);
            merchEmail.setText(merchemail);*/
            //session.checkLogin();
        }

    }
    @Override
    public void onBackPressed() {

        /*if (backpresscount==1) {
            Toast.makeText(getApplicationContext(),"Press again to exit.",Toast.LENGTH_SHORT);
        }
        else if(backpresscount==2)
        {
            this.finish();
        }*/
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
        getMenuInflater().inflate(R.menu.home_screen, menu);
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
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Intent about = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(about);

            // Handle the camera action
        } else if (id == R.id.nav_bills) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.navAboutus) {

            Intent about = new Intent(getApplicationContext(),AboutUs.class);
            startActivity(about);

        } else if (id == R.id.navLogout) {

            session.logoutUser();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("str",response.toString());
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
           // progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new MyRecyclerAdapter(HomeScreen.this, feedsList);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(HomeScreen.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("bill");
            feedsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
                Log.d("val", post.getString("c_name"));

                item.setTitle(post.optString("c_name"));
                item.setPrice(post.optString("amount"));
               /* item.setThumbnail(post.optString("thumbnail"));
*/
                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                merchImage.setImageBitmap(image);


            }else{


                merchImage.setImageResource(R.drawable.defaultauthorimage);

            }
        }
    }


}