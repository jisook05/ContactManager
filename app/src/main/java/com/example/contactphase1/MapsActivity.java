package com.example.contactphase1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;


/*
 * jxk161230 cs4301.002 ContactPhase4
 * Due: 05/04/20
 * This class handles all maps activity
 * Once class is called, it gets user location
 *  #### PLEASE ENABLE DEVICE GPS ####
 * Takes address location from user contact input
 *  - Address 1, City, State
 *  - request using Google Maps API
 * */
public class   MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_ID = 11 ;
    private static String data;
    private GoogleMap mMap;
    private String mAddress, mCity, mState, completeAddress;
    public static TextView formattedAddress, coordinates, distance;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get address string
        Intent intent = getIntent();
        intent.getExtras();
        mAddress= intent.getStringExtra("Address");
        mCity= intent.getStringExtra("City");
        mState= intent.getStringExtra("State");


        // Formatted Address
        completeAddress= mAddress+",+"+mCity+",+"+mState;

        //Find views
        formattedAddress= findViewById(R.id.formatted_address);
        coordinates=findViewById(R.id.coordinates);
        distance=findViewById(R.id.distance);

        // Set Address text
        formattedAddress.setText((mAddress+","+mCity+","+mState).replace("+"," "));




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap= googleMap;


        // Get string url and pass to background task
        String url = getUrl(completeAddress);
        FetchAddress fetchAddress= new FetchAddress();
        fetchAddress.execute(url);

        //Get current location
        getCurrentLocation();

    }

    /*-------------------------------------------------------------------------------
    * Method to calculate distance between to points: jxk16230
    * -------------------------------------------------------------------------------*/
    public static double distanceBetLocation(double lat1, double lon1, double lat2, double lon2){
        lat1= Math.toRadians(lat1);
        lon1= Math.toRadians(lon1);
        lat2= Math.toRadians(lat2);
        lon2= Math.toRadians(lon2);

        double earthRad = 6371;
        double result= Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2));

        return earthRad*result;
    }

    // This method takes in coordinates of current location
    public void calculateDistance(double lat2, double lng2){
        try{

            // Get coordinates from input address
            String currentLocationLatLng= addAddressMarker();
            String[] locationLL= currentLocationLatLng.split(",");
            String mlat1 =locationLL[0];
            String mlng1 =locationLL[1];

            double lat1 = Double.parseDouble(mlat1);
            double lng1 = Double.parseDouble(mlng1);

            // Calculate distance between two points on the surface of earth
            double distanceResult= distanceBetLocation(lat1, lng1, lat2, lng2);

            // Round to one decimal place and set UI
            String result = String.valueOf(Math.round(distanceResult*100.00)/100.00);
            distance.setText("Distance: "+ result+" miles");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /*----------------------------------------------------------------------------------------
     * ALL Methods below associates with getting address longitude and latitude
     * Get Location of Address input: jxk161230
     * ----------------------------------------------------------------------------------------*/
    private String getUrl(String url){
        return "https://maps.googleapis.com/maps/api/geocode/json?address="+url
                +"&key=AIzaSyBe15WHwZOEc0D0ixXvE2w5rlxCGgFtbdw";

    }

    // I was not sure how to add markers from asyncTask to the maps, so I used geocoder to get long, lat to add marker
    public String addAddressMarker() throws IOException {
        String addrLocation = formattedAddress.getText().toString();
        Geocoder gc = new Geocoder(this, Locale.ENGLISH);

        List<Address> latLngList = gc.getFromLocationName(addrLocation, 1 );
        Address address= latLngList.get(0);

        moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 15);

        LatLng currentLatLng = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentLatLng).title(addrLocation));

        String lat1= String.valueOf(address.getLatitude());
        String lng1= String.valueOf(address.getLongitude());

        return lat1+","+lng1;

    }


    /*----------------------------------------------------------------------------------------
     * ALL Methods below associates with grabbing current location & location permissions
     * Get Current Location of the User: jxk161230
     * ----------------------------------------------------------------------------------------*/
    private void getCurrentLocation() {
        client = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        if(checkPermissions()){
            if(isLocationOn()){
                Task location = client.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location currentLocation = task.getResult();

                        //moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),15);

                        // Add Marker to the current location
                        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(currentLatLng).title(" Your Current Location "));

                        Log.d("Current Location", "lat: "+currentLocation.getLatitude()+" lng: "+ currentLocation.getLongitude());

                        double lat2= currentLocation.getLatitude();
                        double lng2= currentLocation.getLongitude();

                        // Set coordinates of current location
                        calculateDistance(lat2,lng2);


                    }

                });

            }
        }else{
            requestPermissions();
        }

    }


    // Move the camera view to current location
    public void moveCamera(LatLng latlng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    // Check if location is enabled
    private boolean isLocationOn(){
        LocationManager service= (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return enabled;

    }

    // Check for permission
    private boolean checkPermissions(){
        if(ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    // Request for permission
    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    // Called after request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_ID){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
}
