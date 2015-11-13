package com.example.shubham.bille;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pulkit-mac on 11/2/15.
 */
public class AddItem extends AppCompatActivity {

    private static String url = "http://54.68.65.111/mozipper/mongo_api/add_menu.php?mid=55daaaa483b1152c058b4567";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getActionBar().setDisplayHomeAsUpEnabled(true);



        Intent intent = getIntent();

        // Initialize recycler view

        final EditText itemName = (EditText) findViewById(R.id.editItem);
        final EditText itemPrice = (EditText) findViewById(R.id.editPrice);
        Log.d(itemName.getText().toString(),"text");
        Button addItem = (Button) findViewById(R.id.addItem);




        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String iName = itemName.getText().toString();
                final String iPrice = itemPrice.getText().toString();
                if(iName.length()<1) {
                    Toast.makeText(getApplicationContext(), "Enter valid item name", Toast.LENGTH_LONG).show();
                }
               else if(iPrice.length()<1) {
                    Toast.makeText(getApplicationContext(), "Enter valid price", Toast.LENGTH_LONG).show();
                }
                else {
                    url = url+"&item="+iName+"&price="+iPrice;
                    new GetBill().execute();
                }

                Log.d("url",url);

            }
        });

    }

    private class GetBill extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response", "<" + jsonStr);

            if (jsonStr != null) {
                try {
                    Integer result = 0;
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    String err = jsonObj.getString("error");
                    String msg = jsonObj.getString("msg");
                    Log.d("err",err);

                    if(err.equals("false")) {
                        Log.d("err1", err);
                        result = 1;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
        /*            // Getting JSON Array node
                    bill = jsonObj.getJSONArray(TAG_BILL);

                    // looping through All Contacts
                    for (int i = 0; i < bill.length(); i++) {
                        JSONObject c = bill.getJSONObject(i);
`
                        String id = c.getString(TAG_ID);
                        String phone = c.getString(TAG_PHONE);
                        String total = c.getString(TAG_TOTAL);
                        String name = c.getString(TAG_NAME);


                        // Phone node is JSON Object


                        // tmp hashmap for single contact
                        HashMap<String, String> bill = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        bill.put(TAG_ID, id);
                        bill.put(TAG_PHONE, phone);
                        bill.put(TAG_TOTAL, total);
                        bill.put(TAG_NAME, name);


                        // adding contact to contact list
                        billList.add(bill);
                    }*/
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

            // Dismiss the progress dialog
/*            if (pDialog.isShowing())
                pDialog.dismiss();*/
            /**
             * Updating parsed JSON data into ListView
             * */
/*            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, billList,
                    R.layout.list_item, new String[]{TAG_NAME, TAG_PHONE,
                    TAG_TOTAL}, new int[]{R.id.text1,
                    R.id.text2, R.id.text4});

            setListAdapter(adapter);*/
            Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_LONG).show();
            Intent intentBack = new Intent(AddItem.this,MainActivity.class);
            AddItem.this.startActivity(intentBack);
            AddItem.this.finish();
        }

    }


}
