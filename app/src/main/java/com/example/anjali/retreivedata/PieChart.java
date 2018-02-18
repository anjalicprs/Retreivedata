package com.example.anjali.retreivedata;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anjali on 18/2/18.
 */

public class PieChart {
    private static final String TAG ="" ;

    public static void pieChart()
    {
        try
        {
            ArrayList<HashMap<String, String>> pieList = RealActivity.contactList;
            ArrayList<HashMap<Integer,Double>> summedValue;
            for(int count=0;count<pieList.size();count++){

            }
        } catch (Exception e) {
            e.printStackTrace();


        }
    }
}
