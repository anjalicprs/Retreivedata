package com.example.anjali.retreivedata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class RealActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = null;
    PieChart pieChart;
    public static ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        url = (String) bundle.get("url");
        Log.e(TAG, "The received URL is :: " + url);
        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<String, Void, String> {
        final String REQUEST_METHOD = "GET";
        final int READ_TIMEOUT = 15000;
        final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RealActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();


            String inputLine;
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Jason string is :: " + jsonStr);
            String result = null;
            //Create a connection
            URL myUrl = null;
            try {
                myUrl = new URL(url);
                Log.e(TAG, "the myURL is :: "+myUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                assert myUrl != null;
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
                TAG = "value";
                Log.e(TAG, "RESULT : "+result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            pieChart(result);
        }

    }
    public void pieChart(String result)
    {
        try
        {
            float[][] array = new float[2][5];
            String[] words=result.split(":");
            System.out.println(words[0]);
            System.out.println(words[1]);
            System.out.println(words[2]);
            System.out.println(words[3]);
            String[] applia1 = words[1].split(",");
            String[] applia2 = words[2].split(",");
            for(int i=1;i<applia1.length-1;i++)
            {
                array[0][i-1]=Float.valueOf(applia1[i]);
                System.out.println(array[0][i-1]);
            }
            for(int i=1;i<applia2.length-1;i++)
            {
                array[1][i-1]=Float.valueOf(applia1[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}