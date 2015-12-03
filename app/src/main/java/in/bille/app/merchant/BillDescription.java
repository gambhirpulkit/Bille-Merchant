package in.bille.app.merchant;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BillDescription extends AppCompatActivity {


    TextView name,amount,custphone;
    TextView menulist,totalamt;

    String cname,bamt,billid,cphone;
    String itemandprice="";
    String a,b;
    String url = Config.url+"order_mer.php?bill_id=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_description);
        String fontPath = "fonts/Walkway_Black.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        name = (TextView)findViewById(R.id.textViewCustomerName);
        amount = (TextView)findViewById(R.id.textViewBillAmount);
        menulist = (TextView)findViewById(R.id.textView_menulist);
        custphone = (TextView)findViewById(R.id.textView_cphone);
        totalamt = (TextView)findViewById(R.id.textView_total);
        amount.setTypeface(tf);
        custphone.setTypeface(tf);
        name.setTypeface(tf);
        totalamt.setTypeface(tf);



        a = totalamt.getText().toString();
        b=a;

        Intent billdes = getIntent();
        cname = billdes.getStringExtra("cusname");
        bamt = billdes.getStringExtra("billamt");
        billid = billdes.getStringExtra("bill_id");
        cphone = billdes.getStringExtra("c_phone");

        menulist.setTypeface(tf);
        Log.d("billid",""+billid);

        url += billid;
        Log.d("oooooooooooooooo", "" + url);
        new AsyncHttpTask().execute(url);




        custphone.setText("+91"+cphone);
        name.setText(cname);
        //amount.setText(""+bamt);
        //totalamt.setText("@string/Rs"+bamt);



        a += bamt;
        amount.setText(a);



        totalamt.setText(a);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
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
            Log.d("22222222222","99999999999");
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                Log.d("22222222222","88888888888");
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
                    Log.d("str", response.toString());
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    Log.d("22222222222","7777777777");
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                //Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            // progressBar.setVisibility(View.GONE);

            if (result == 1) {

                menulist.setText(itemandprice);


                Log.d("22222222222","444444444444");

            } else {
                Toast.makeText(BillDescription.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {

        Log.d("22222222222",""+itemandprice);
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("order");

            Log.d("parse","result");

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
               // FeedItem item = new FeedItem();
               // Log.d("val", post.getString("c_name"));
                String itemname = post.optString("item");
                String itemprice = post.optString("price");
                String phone = post.optString("c_phone");
                String quantity = post.optString("qty");
                String totalcost = post.optString("cost");


                Log.d("2222222",""+itemname);
                Log.d("2222222",""+itemprice);

                itemandprice+="\n\t"+itemname+ "\n\t"+b+itemprice+" X "+quantity+"\t"+"\t"+"\t"+"\t"+"\t\t\t\t\t\t\t"+""+b+totalcost;
                itemandprice+="\n";
               /* item.setThumbnail(post.optString("thumbnail"));
*/              Log.d("22222222222",""+itemandprice);



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}