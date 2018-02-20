package com.example.anjali.retreivedata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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
    private String[] xData = new String[2];
    private float[] yData = new float[2];

    // URL to get contacts JSON
    private static String url = null;
    LineChart lineChart;
    PieChart piechart;
    String result = null;
    public static ArrayList<HashMap<String, String>> contactList;
    float apl1 = 0;
    float apl2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        url = (String) bundle.get("url");
        Log.e(TAG, "The received URL is :: " + url);
        if(internetConCheck()== true){
            new GetContacts().execute();
        }
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
            /**
             * Updating parsed JSON data into ListView
             * */

            pieChart(result);
            TextView aplval = (TextView) findViewById(R.id.Id1val);
            TextView ap2val = (TextView) findViewById(R.id.Id2val);
            aplval.setText("Total units consumed by appliance 1 : "+Float.toString(yData[0]));
            ap2val.setText("Total units consumed by appliance 2 : "+Float.toString(yData[1]));
        }

    }
    public void pieChart(String result)
    {
       //piechart = (PieChart)findViewById(R.id.idpiechart);
        lineChart = (LineChart) findViewById(R.id.lineChart);
        float[][] appliance = new float[2][32];
        String[] words=result.split(":");
        System.out.println(words[0]);
        System.out.println(words[1]);
        System.out.println(words[2]);
        System.out.println(words[3]);
        String[] applia1 = words[1].split(",");
        String[] applia2 = words[2].split(",");
        yData[0]=0;
        yData[1]=1;
        for(int i=1;i<applia1.length-1;i++)
        {
            appliance[0][i-1]=Float.valueOf(applia1[i]);
            yData[0]+=appliance[0][i-1];
            System.out.println(appliance[0][i-1]);
        }
        for(int i=1;i<applia2.length-1;i++)
        {
            appliance[1][i-1]=Float.valueOf(applia2[i]);
            yData[1]+=appliance[1][i-1];
        }
        ArrayList<String> xAXES = new ArrayList<>();
        ArrayList<Entry> yAXISsin = new ArrayList<>();
        ArrayList<Entry> yAXIScos = new ArrayList<>();
        double x = 0 ;
        int numDataPoints = applia2.length-2;
        for(int i=0;i<numDataPoints;i++){
            yAXISsin.add(new Entry(i,appliance[0][i]));
            yAXIScos.add(new Entry(i,appliance[1][i]));
            xAXES.add(i, String.valueOf(i));
        }
        System.out.println(yAXIScos);
        System.out.println(yAXISsin);
        String[] xaxes = new String[xAXES.size()];
        for(int i=0; i<xAXES.size();i++){
            xaxes[i] = xAXES.get(i).toString();
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        LineDataSet lineDataSet1 = new LineDataSet(yAXIScos,"fan");
        LineDataSet lineDataSet2 = new LineDataSet(yAXISsin,"fridge");

        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColors(Color.RED);
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColors(Color.BLUE);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);

        lineChart.setData(new LineData(lineDataSets));
        lineChart.setVisibleXRangeMaximum(20f);
        lineChart.invalidate();
        lineChart.refreshDrawableState();
        xData[0]="AC";
        xData[1]="fridge";
        piechart = (PieChart) findViewById(R.id.pieChart);
        piechart.setRotationEnabled(true);
        piechart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        piechart.setHoleRadius(25f);
        piechart.setTransparentCircleAlpha(0);
        piechart.setCenterText("Sangam !");
        piechart.setCenterTextSize(10);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!

        addDataSet();
        piechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());
                //Toast.makeText(MainActivity.this, "onValueSelected: " + e.toString(),Toast.LENGTH_LONG).show();
                int pos1 = e.toString().indexOf("y:");
                String sales = e.toString().substring(pos1 + 2);

                for(int i = 0; i < yData.length; i++){
                    if(yData[i] == Float.parseFloat(sales)){
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1];
                Toast.makeText(RealActivity.this, "Appliance " + employee + "\n" + "Units consumed :: " + sales + " KWh", Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
    private void addDataSet() {
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Contribution of each appliance");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = piechart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setEnabled(true);

        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        piechart.setData(pieData);
        piechart.invalidate();
    }
    private boolean internetConCheck() {
        if (internet_connection()){
            Log.d(TAG, "cool");
            return true;
        }else{
            //create a snackbar telling the user there is no internet connection and issuing a chance to reconnect
            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(RealActivity.this);
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

}