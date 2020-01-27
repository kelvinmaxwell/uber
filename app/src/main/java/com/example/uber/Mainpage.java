package com.example.uber;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;




import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import com.google.android.libraries.places.api.Places;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Mainpage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks ,GoogleApiClient.OnConnectionFailedListener, LocationListener,TaskLoadedCallback {
FirebaseAuth auth;
TextView t1,t2;
    private GoogleMap mMap;
    GoogleApiClient client;
    LocationRequest request;
    LatLng startlatlng,endlatlong ,linestart,linestop;
    Marker currentmarker,destinationmarker,Currentmarker1;
    Button b4_source,b5destination;
    EditText returntime;
    TextView current;
 public   int AUTOCOMPLETE_REQUEST_CODE = 1;
    public   int AUTOCOMPLETE_REQUEST_CODE2 = 2;
    private Polyline currentPolyline;
    String url3= "http://192.168.43.78/www/html/trevormoha/MpesaTesting/initiate.php";
    String url7= "http://192.168.43.78/www/html/trevormoha/return_bike.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

current=findViewById(R.id.current);
        Places.initialize(getApplicationContext(), "AIzaSyD-diriaQMFUArVpjTpwIhVPK-0QkrrRVA");

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
b4_source=findViewById(R.id.startpoint);
b5destination=findViewById(R.id.enddest);
returntime=findViewById(R.id.returntime);
        auth=FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String user1=user.getUid();
        Toast.makeText(this,  user1, Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot mainSnapshot) {

                String name=mainSnapshot.child("name").getValue(String.class);
                String email=mainSnapshot.child("email").getValue(String.class);
                t1=findViewById(R.id.nametext);
                t2=findViewById(R.id.emailtext);
                t1.setText(name);
                t2.setText(email);

            }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    });

        b4_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .build(getApplicationContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.



                        if(place.getLatLng()!=null){
                            MarkerOptions options1= new MarkerOptions();

                            b4_source.setText(place.getName());
                            options1.position(place.getLatLng());
                            currentmarker=mMap.addMarker(options1);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),15 ));
                        }
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                       // Log.i(TAG, "An error occurred: " + status);
                    }
                });


            }
        });

        b5destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .build(getApplicationContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE2);
                AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.



                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.

                        System.out.println("maxi"+ "An error occurred: " + status);
                    }
                });


            }
        });





}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i=new Intent(Mainpage.this,addimages.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            Intent i=new Intent(Mainpage.this,itmesdisplay.class);
            i.putExtra("action","available");
            startActivity(i);


            String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");

        } else if (id == R.id.nav_tools) {
            Intent i=new Intent(Mainpage.this,itmesdisplay.class);
            i.putExtra("action","taken");
            startActivity(i);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.logout) {
            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
                auth.signOut();
                Intent i=new Intent(Mainpage.this,MainActivity.class);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(this, "user is already sihned out", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
client=new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
client.connect();
        // Add a marker in Sydney and move the camera


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
request=new LocationRequest().create();
request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
request.setInterval(500);
if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
    return;
}
LocationServices.FusedLocationApi.requestLocationUpdates(client,request,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LocationServices.FusedLocationApi.removeLocationUpdates(client,this);

        if(location==null)
        {
            Toast.makeText(this, "Location could not be found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            startlatlng=new LatLng(location.getLatitude(),location.getLongitude());
            Geocoder geocoder=new Geocoder(this, Locale.getDefault());

            try{
                List<Address> myaddresses=geocoder.getFromLocation(startlatlng.latitude,startlatlng.longitude,1);
                String address=myaddresses.get(0).getAddressLine(0);
                String city=myaddresses.get(0).getLocality();
                b4_source.setText(address+""+city);
            }catch (IOException e)
            {
                e.printStackTrace();
            }

            if(currentmarker==null){
            MarkerOptions options=new MarkerOptions();
            options.position(startlatlng);
            options.title("start position");
         currentmarker=mMap.addMarker(options);

           mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startlatlng,15 ));
        }

else{
    currentmarker.setPosition(startlatlng);
            }
        }

        LocationServices.FusedLocationApi.removeLocationUpdates(client,this);

        if(location==null)
        {
            Toast.makeText(this, "Location could not be found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            linestart=new LatLng(location.getLatitude(),location.getLongitude());
            Geocoder geocoder=new Geocoder(this, Locale.getDefault());

            try{
                List<Address> myaddresses=geocoder.getFromLocation(linestart.latitude,linestart.longitude,1);
                String address=myaddresses.get(0).getAddressLine(0);
                String city=myaddresses.get(0).getLocality();
                current.setText(address+""+city);
            }catch (IOException e)
            {
                e.printStackTrace();
            }

            if(Currentmarker1==null){
                MarkerOptions options=new MarkerOptions();
                options.position(linestart);
                options.title("Current Position");
                Currentmarker1=mMap.addMarker(options);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(linestart,15 ));
            }

            else{

                Currentmarker1.setPosition(linestart);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {


            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                   b4_source.setText(place.getName());
                    if(currentmarker==null){
                        MarkerOptions options=new MarkerOptions();
                        options.position(startlatlng);
                        options.title("Startlocation");
                        currentmarker=mMap.addMarker(options);}
                    else{
startlatlng=place.getLatLng();
                        currentmarker.setPosition(place.getLatLng());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),15 ));
                    }

                    } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    Status status = Autocomplete.getStatusFromIntent(data);
                    System.out.println ( status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
            if(requestCode == AUTOCOMPLETE_REQUEST_CODE2){
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    b5destination.setText(place.getName());
                    if(destinationmarker==null){
                        MarkerOptions options=new MarkerOptions();
                        options.position(startlatlng);
                        options.title("endlocation");
                        destinationmarker=mMap.addMarker(options);}
                    else{
                        float result[]=new float[10];


endlatlong=place.getLatLng();
                        destinationmarker.setPosition(place.getLatLng());
                        LatLng markerLatLng = currentmarker.getPosition();
                        Location markerLocation = new Location("");

                        Double startlat=markerLatLng.latitude;
                        Double startlong=markerLatLng.longitude;


                                LatLng markerLatLng2 = destinationmarker.getPosition();
                        Location markerLocation2 = new Location("");

                        //markerLocation2.setLongitude(markerLatLng2.longitude);
                        Double endlat=markerLatLng2.latitude;
                        Double endlon=markerLatLng2.longitude;

                        Location.distanceBetween(startlat,startlong,endlat,endlon,result);

Float d=result[0];
sendpaymentdata(d);
                        Toast.makeText(this,"" +d, Toast.LENGTH_LONG).show();
                   String url=geturl(startlatlng,endlatlong,"driving");
                     new FetchURL(Mainpage.this).execute(url,"driving");
                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(endlatlong,15 ));
                    }

                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    Status status = Autocomplete.getStatusFromIntent(data);
                    System.out.println ( status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }

        }


    }

    private String geturl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyD-diriaQMFUArVpjTpwIhVPK-0QkrrRVA";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }



    public void   sendpaymentdata(final float totals){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.d(TAG, response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error while reading googl", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();




                String orderno="1025";
                String account_no="2000";
                Intent intent = getIntent();
                String  price =  intent.getStringExtra("price");

               // int cash=Integer.parseInt(price)*Integer.parseInt(amount.getText().toString());
               // phone=intent.getStringExtra("phone");
               // String payamount= String.valueOf(cash);
                String time_cash=returntime.getText().toString();
                String text=time_cash;
                String sub_text=text.substring(6);
                int pay=Integer.parseInt(sub_text);

                int toto=(int)totals;
                int d_in_km=toto/1000;
                int total_payable=d_in_km*pay;


     //09:30-300



                String tt=String.valueOf(total_payable);

System.out.println("maxicash"+sub_text);
                params.put("phone", "254729312006");
                params.put("amount", tt);
                params.put("orderno", orderno);
                params.put("quantity", account_no);


                return params;

            }

        };
        return_bike();
        MySingleton.getInstance(Mainpage .this).addToRequestQueue(stringRequest);



    }

    public void   return_bike(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url7, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.d(TAG, response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error while reading googl", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                Intent intent = getIntent();


                String id =  intent.getStringExtra("id_bike");




                params.put("id_cart", id);
                params.put("action", "available");


                return params;
            }
        };
        MySingleton.getInstance(Mainpage .this).addToRequestQueue(stringRequest);
        Toast.makeText(Mainpage.this, " added succesfully to cart", Toast.LENGTH_SHORT).show();






    }


}

