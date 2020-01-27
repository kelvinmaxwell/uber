package com.example.uber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class makepurchase extends AppCompatActivity {
    TextView listdata;
    ImageView imageView;

    private static final

    String url3= "http://192.168.43.78/www/html/trevormoha/MpesaTesting/initiate.php";

    String url7= "http://192.168.43.78/www/html/trevormoha/return_bike.php";


    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomlistAdapter2 adapter;
    public EditText location,amount;
    public String user,urlmain;
    private ArrayList<product3> statuscheckArrayList;
    public ListView lvProduct;
    public Button addcart,pay,viewcart;
    final String TAG=this.getClass().getSimpleName();

SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makepurchase);
        lvProduct=findViewById(R.id.listview);




        listdata = findViewById(R.id.listdata);
        imageView = findViewById(R.id.thumbnail);
        Intent intent = getIntent();
        String mName,mEmail,mprevelage,mlocation,price;
        adapter = new CustomlistAdapter2(this, movieList);
        pay=findViewById(R.id.pay);
        sessionManager=new SessionManager(this);
        //sessionManager.checkLogin();

   String receivedName =  intent.getStringExtra("name");
        String id =  intent.getStringExtra("id");
        Toast.makeText(makepurchase.this, id, Toast.LENGTH_SHORT).show();
       //revelage =  intent.getStringExtra("previlage");




                //
        user =  intent.getStringExtra("user");
        String urlimage="http://192.168.43.78/www/html/trevormoha/";
        String suburl=intent.getStringExtra("image");
        String Combined=urlimage.concat(suburl);
       Picasso.get().load(Combined).error(R.mipmap.ic_launcher).into(imageView);

        listdata.setText(receivedName);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return_bike();
            }
        });



      //  inflateimage(Combined);

        //enable back Button
      // getSupportActionBar().setDisplayHomeAsUpEnabled(true);





    }
    //getting back to listview
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                params.put("action", "return");


                return params;
            }
        };
        MySingleton.getInstance(makepurchase .this).addToRequestQueue(stringRequest);
        Toast.makeText(makepurchase.this, " added succesfully to cart", Toast.LENGTH_SHORT).show();






                }



















    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }




}
