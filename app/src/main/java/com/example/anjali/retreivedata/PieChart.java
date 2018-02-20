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

    public static void pieChart(String result)
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