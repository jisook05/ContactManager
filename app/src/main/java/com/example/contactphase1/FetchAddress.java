package com.example.contactphase1;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
/*
* Background Task to fetch
* /*----------------------------------------------------------------------------------------
 * ALL Methods below associates with getting address longitude and latitude
 * Get Location of Address input: jxk161230
 * ----------------------------------------------------------------------------------------*/

public class FetchAddress extends AsyncTask<String,Void,String> {
    GoogleMap mMap;

    @Override
    protected String doInBackground(String... params) {
        String addresssUrl= params[0];
        String data="";


        try{
            // Get URL connection
            URL myUrl= new URL(addresssUrl);
            HttpsURLConnection connection = (HttpsURLConnection) myUrl.openConnection();

            connection.setReadTimeout(10000);
            connection.setConnectTimeout(45000); // Timeout is 45 seconds
            connection.setRequestMethod("GET");

            connection.connect();

            InputStream inputStream= connection.getInputStream();
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            while(line !=null){
                line= bufferedReader.readLine();
                data = data+line;
            }


        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        Log.d("data",""+data);
        return data;
        //return new String[]{data};
    }

    @Override
    protected void onPostExecute(String strings) {



        try{

            JSONObject jsonObject= new JSONObject(strings);

            JSONArray result = jsonObject.getJSONArray("results");
            if(result.length()>0) {
                // Parse JSON and get latitude and longitude
                double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");
                double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");


                // Get string value of lat lng
                String _latitude = String.valueOf(lat);
                String _longitude = String.valueOf(lng);
                String _latLng = "Lat: "+_latitude + " " + "Lng: "+ _longitude;

                // Set UI
                MapsActivity.coordinates.setText(_latLng);
//                MarkerOptions markerOptions= new MarkerOptions();
//                markerOptions.position(new LatLng(lat, lng)).title("Contact's Address");
//                mMap.addMarker(markerOptions);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        super.onPostExecute(strings);
    }
}
