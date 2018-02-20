package com.example.anjali.retreivedata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectView extends AppCompatActivity {
    String url = "";
    private ProgressDialog pDialog;
    String result;
    float totalSum=0;
    private String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_select_view);
        if(internetConCheck()== true){
            url="http://165.227.105.231/sangam/month.php";
            new SelectView.GetContacts().execute();
        }
    }
    public void doneSelection(View v){
        Button hour = (Button)findViewById(R.id.month);
        Button day = (Button) findViewById(R.id.day);
        Button week = (Button) findViewById(R.id.week);
        switch(v.getId()) {
            case R.id.month:
                    url = "http://165.227.105.231/sangam/month.php";
                break;
            case R.id.day:
                url = "http://165.227.105.231/sangam/week.php";
                break;
            case R.id.week:
                url = "http://165.227.105.231/sangam/day.php";
                break;
        }
        show();
    }
    public void show(){
        Intent i = new Intent(this, RealActivity.class);
        i.putExtra("url", url);
        startActivity(i);
    }
    class GetContacts extends AsyncTask<String, Void, String> {
        final String REQUEST_METHOD = "GET";
        final int READ_TIMEOUT = 15000;
        final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SelectView.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String inputLine;
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
            String[] words=result.split(":");
            System.out.println(words[0]);
            System.out.println(words[1]);
            System.out.println(words[2]);
            System.out.println(words[3]);
            String[] applia1 = words[1].split(",");
            String[] applia2 = words[2].split(",");
            for(int i=1;i<applia1.length-1;i++)
            {
                totalSum+=Float.valueOf(applia1[i]);
            }
            for(int i=1;i<applia2.length-1;i++)
            {
                totalSum+=Float.valueOf(applia2[i]);
            }
            float totalMoney;
            TextView totalval = (TextView) findViewById(R.id.units);
            totalval.setText("Total units consumed : "+Float.toString(totalSum));
            TextView totalBal = (TextView) findViewById(R.id.money);
            if(totalSum > 400)
            {
                totalMoney = totalSum * 3;
            }
            else if(totalSum > 200){
                totalMoney = totalSum*2;
            }
            else{
                totalMoney= totalSum;
            }
            totalBal.setText("Total Money to be paid: "+Float.toString(totalMoney));
        }

    }
    private boolean internetConCheck() {
        if (internet_connection()){
            Log.d(TAG, "cool");
            return true;
        }else{
            //create a snackbar telling the user there is no internet connection and issuing a chance to reconnect
            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(SelectView.this);
            alertDialogBuilder
                    .setMessage("No internet connection")
                    .setCancelable(false)
                    .setPositiveButton("Try again ",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            })
                    .setNegativeButton("EXIT",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();
        }
        return false;
    }
    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    public void about(View v){

    }
}
