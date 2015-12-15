package in.bille.app.merchant;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Menu";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyMenuRecyclerAdapter adapter;
    private ProgressBar progressBar;

    SessionManager session;

    String url = Config.url+"list_menu.php?mid=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        session = new SessionManager(getApplicationContext());

        Log.d("test", "onCreate Menu");
        Intent intentBack = getIntent();

        /*Button addBtn = (Button) findViewById(R.id.create);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddItem.class);
                MainActivity.this.startActivity(intent);
            }
        });*/



        HashMap<String, String> user = session.getUserDetails();

        String mid = user.get(SessionManager.KEY_MID);

       // ImageButton editBtn = (ImageButton) findViewById(R.id.editMenu);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Downloading data from below url
        //final String url = "http://javatechig.com/?json=get_recent_posts&count=45";

        url += mid;

        new AsyncHttpTask().execute(url);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("test", "The onResume() event");
    }



/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    @SuppressLint("NewApi")
    private void CreateMenu(Menu menu)
    {
        MenuItem menu1=menu.add(0,0,0,"AddItem");
        {
            menu1.setIcon(R.drawable.additem);
            menu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(i);
                MainActivity.this.finish();
                return true;
            case 0:
                Toast.makeText(getApplicationContext(), "Add Item into Menu", Toast.LENGTH_SHORT).show();
                Intent add=new Intent(getApplicationContext(),AddItem.class);
                startActivity(add);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new MyMenuRecyclerAdapter(MainActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("menu");
            feedsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
                Log.d("val",post.getString("item"));
                item.setTitle(post.optString("item"));
                item.setPrice(post.optString("price"));
                item.setMenuId(post.optString("menu_id"));
               /* item.setThumbnail(post.optString("thumbnail"));
*/
                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
