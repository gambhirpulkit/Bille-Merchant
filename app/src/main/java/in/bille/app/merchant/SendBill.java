package in.bille.app.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class SendBill extends AppCompatActivity {

    final Context context = this;

    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private SendBillRecyclerAdapter adapter;
    private ProgressBar progressBar;

    private String stringQty;
    private String strindIds;
    private String phone;
    SessionManager session;
    private String apiUrl = Config.url;

    private String checkoutUrl = "";

    TextView cName;

    FeedItem item = new FeedItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();

        final String mid = user.get(SessionManager.KEY_MID);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_send);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            strindIds = extras.getString("itemString");
            stringQty = extras.getString("qtyString");
            phone = extras.getString("cPhone");

        }
        //Log.d("phone", phone);
        String url = apiUrl + "billing_info.php?mid="+mid + "&phone=" + phone + "&order=" + strindIds + "&qty=" + stringQty;

        Log.d("url", url);
        new AsyncHttpTask().execute(url);

        Button sendBtn =(Button) findViewById(R.id.verifyBill);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemStr = TextUtils.join(",", adapter.itemIds);
                String qtyStr = TextUtils.join(",", adapter.itemQty);

                Log.d("xxxxxxxxxxxx",""+adapter.itemIds);
                checkoutUrl = apiUrl + "billing.php?mid="+mid + "&phone=" + phone + "&order=" + itemStr + "&qty=" + qtyStr;
                Log.d("checkoutUrl",checkoutUrl);
                new VerifyBill().execute();
            }
        });

    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            //  setProgressBarIndeterminateVisibility(true);
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
                    Log.d("str", response.toString());
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("order", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            //progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new SendBillRecyclerAdapter(SendBill.this,feedsList);
                mRecyclerView.setAdapter(adapter);
                TextView c_name = (TextView) findViewById(R.id.cName);
                TextView c_phone = (TextView) findViewById(R.id.cPhone);

                c_name.setText(item.getName());
                c_phone.setText(item.getPhone());

            } else {
                Toast.makeText(SendBill.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);

            String name = response.optString("name");
            String phone = response.optString("phone");
            String total = response.optString("total");

            item.setName(name);
            item.setPhone(phone);
            item.setTotalBill(total);

            feedsList = new ArrayList<>();



            JSONArray posts = response.optJSONArray("order");
            feedsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
               // Log.d("val",post.getString("item"));


                Log.d("item",post.optString("item"));
                item.setTitle(post.optString("item"));
                item.setPrice(post.optString("price"));
                item.setTotal(post.optString("cost"));
                item.setQty(post.optString("qty"));
                item.setMenuId(post.optString("menu_id"));
               // item.setThumbnail(post.optString("thumbnail"));


                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class VerifyBill extends AsyncTask<Void, Void, Void> {

        private Integer flag = 0;
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(checkoutUrl, ServiceHandler.GET);

            Log.d("Response", "<" + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    String err = jsonObj.getString("error");
                    String msg = jsonObj.getString("msg");
                    Log.d("err",err);


                    if(err.equals("false")) {
                        flag = 1;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Could not get data");
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected void onPostExecute(Void result) {
            if(flag == 1) {
                Toast.makeText(getApplicationContext(), "Bill sent", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Error generating bill", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), "Bill sent", Toast.LENGTH_LONG).show();
            Intent intentBack = new Intent(getApplicationContext(),HomeScreen.class);
            startActivity(intentBack);
            SendBill.this.finish();

        }

    }




}