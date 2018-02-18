package com.example.anjali.retreivedata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String url = "";
    private String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void doneSelection(View v){
        boolean checked = ((RadioButton) v).isChecked();
        RadioButton hour = (RadioButton)findViewById(R.id.hour);
        RadioButton day = (RadioButton) findViewById(R.id.day);
        RadioButton week = (RadioButton) findViewById(R.id.week);
        switch(v.getId()) {
            case R.id.hour:
                if (checked){
                    url = "http://165.227.105.231/sangam/php4.php";
                }
                break;
            case R.id.day:
                if (checked){
                    url = "http://165.227.105.231/sangam/php4.php";
                }
                break;
            case R.id.week:
                if (checked){
                    url = "http://165.227.105.231/sangam/php4.php";
                }
                break;
        }
    }
    public void show(View v){
        Intent i = new Intent(this, RealActivity.class);
        i.putExtra("url", url);
        startActivity(i);
    }
}
