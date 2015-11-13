package com.example.shubham.bille;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity implements View.OnClickListener {


    EditText username,password;
    Button login,register;
    SessionManager session;
    String useremail,pass;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // toolbar.setBackgroundColor(Color.GREEN);


        username = (EditText)findViewById(R.id.edit_username);
        password = (EditText)findViewById(R.id.edit_password);

        login = (Button)findViewById(R.id.buttonLogin);

        register = (Button)findViewById(R.id.button_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Register.class);
                startActivity(i);

            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        useremail = username.getText().toString();
        pass = password.getText().toString();


        boolean flag = true;

        if (useremail.equals("")) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Email is required.",
                    Toast.LENGTH_SHORT).show();
        } else if (useremail.contains("@") != true) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Invalid email.",
                    Toast.LENGTH_SHORT).show();
        } else if (useremail.contains(".") != true) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Invalid email.",
                    Toast.LENGTH_SHORT).show();
        } else if (pass.equals("")) {
            flag = false;
            Toast.makeText(getApplicationContext(), "Password is required.",
                    Toast.LENGTH_SHORT).show();
        }

        if(flag) {
            //  Toast.makeText(getApplicationContext(),""+Email,Toast.LENGTH_LONG).show();
            String url;
            url = "http://54.68.65.111/mozipper/mongo_api/login_mer.php?user="+useremail+"&pwd="+pass;
            // Log.d(url,"url");
           //  Toast.makeText(getApplicationContext(),""+url,Toast.LENGTH_SHORT).show();
            new ReadJSONFeedTask().execute(url);
        }
        clicked = true;

    }

    public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
        } catch (Exception e) {

        }
        return stringBuilder.toString();
    }

    public class ReadJSONFeedTask extends AsyncTask<String, Void, String>
    {


        @Override
        protected String doInBackground(String... urls)
        {
            //Log.d("backg","yo");
            return readJSONFeed(urls[0]);
        }
        protected void onPostExecute(String result)
        {
            try
            {
                JSONObject jsonObject= new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                Log.d("test", "test");
                // Toast.makeText(getApplicationContext(), jsonObject.getString("error"),Toast.LENGTH_LONG).show();

                String checkerror = jsonObject.getString("error");
                String checkemail = jsonObject1.getString("email");
                String checkMID = jsonObject1.getString("m_id");
                String merchlogo = jsonObject1.getString("logo");
                String merchname = jsonObject1.getString("name");
                Log.d("email",checkemail);
                if(checkerror.equals("false"))
                {
                    Log.d("test", "login");
                    //SharedPreferences.Editor editor = sharedpreferences.edit();
                    Toast.makeText(getApplicationContext(),"WELCOME",Toast.LENGTH_SHORT).show();
                    session.createLoginSession(checkemail, merchname, checkMID, merchlogo);



                    Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                    startActivity(i);
                    Login.this.finish();

                    //editor.putBoolean(IS_LOGIN, true);
                    // editor.putString(Password1,Password);
                   /* editor.putString(Email1, checkemail);
                    editor.putString(MID,checkMID);

                    editor.commit();

                    String show = sharedpreferences.getString(Email1,checkemail);*/
                    // Toast.makeText(getApplicationContext(),"WELCOME",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Incorrect username or password",Toast.LENGTH_SHORT).show();
                }

    			/*for(int i=0;i<jsonArray.length();i++)
    			{
    				JSONObject jsonObject=jsonArray.getJSONObject(i);
    				Toast.makeText(getApplicationContext(), jsonObject.getString("survId"),Toast.LENGTH_LONG).show();
    			}*/
                // Log.d("string",jsonArray.toString());
            }
            catch(Exception e)
            {

            }
        }
    }

}
