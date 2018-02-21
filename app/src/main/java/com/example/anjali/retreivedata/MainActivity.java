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
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String url = "http://165.227.105.231/sangam/authenticate.php";
    private ProgressDialog pDialog;
    String result;
    String passWord, userName;
    private String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        internetConCheck();
    }
    public void register(View v){
        EditText user = (EditText) findViewById(R.id.username);
        EditText pass = (EditText) findViewById(R.id.pass);
        userName = user.getText().toString();
        passWord = pass.getText().toString();
        url = url+"?mod=1&user="+userName+"&pass="+passWord;
        boolean isConnected = internetConCheck();
        if(isConnected == true){
            new MainActivity.GetContacts().execute();
        }
    }

    private boolean internetConCheck() {
        if (internet_connection()){
            Log.d(TAG, "cool");
            return true;
        }else{
            //create a snackbar telling the user there is no internet connection and issuing a chance to reconnect
            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(MainActivity.this);
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
 public void login(View v){
        EditText user = (EditText) findViewById(R.id.username);
        EditText pass = (EditText) findViewById(R.id.pass);
        userName = user.getText().toString();
        passWord = pass.getText().toString();
        url = url+"?mod=2&user="+userName+"&pass="+passWord;
        if(internetConCheck()== true){
            new MainActivity.GetContacts().execute();
            Log.d(TAG, url);
	}
  }

    private void startMain() {
        Log.d(TAG, "hey");
        Intent i = new Intent(this, SelectView.class);
        i.putExtra("name",userName);
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
            pDialog = new ProgressDialog(MainActivity.this);
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
            if(result.equals("authenticated"))
            {
                startMain();
            }
            else if(result.equals("registered")){
                startMain();
            }
            else{

            }
            /**
             * Updating parsed JSON data into ListView
             * */
        }

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
}
