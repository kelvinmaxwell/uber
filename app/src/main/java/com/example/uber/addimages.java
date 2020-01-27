package com.example.uber;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class addimages extends AppCompatActivity {
    public Button upload,choose;
    public ImageView img;
    final int CODE_GALLERY_REQUEST=999;
    Bitmap bitmap;
    String urlupload="http://192.168.43.78/www/html/trevormoha/upload.php";
    ProgressBar progressbar;
    public EditText descriptiontxt,sourcetxt,pricetxt,quantitytxt,productnametxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addimages);
        upload=findViewById(R.id.upload);
        choose=findViewById(R.id.choose);
        img=findViewById(R.id.imageupload);
        descriptiontxt=findViewById(R.id.description);
        sourcetxt=findViewById(R.id.source);
        pricetxt=findViewById(R.id.price);
        quantitytxt=findViewById(R.id.quantity);
        productnametxt=findViewById(R.id.productname);
        progressbar=findViewById(R.id.progressBar2);
        progressbar.setVisibility(View.GONE);


choose.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ActivityCompat.requestPermissions(addimages.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODE_GALLERY_REQUEST);
    }
});
upload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        progressbar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              progressbar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"SAVED", Toast.LENGTH_SHORT).show();
                System.out.println("maxi"+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error" + error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

String imageData=imageToString(bitmap);
                String descriptionstr,sourcestr,pricestr,quantitystr,productnamestr;
                descriptionstr=descriptiontxt.getText().toString();
                sourcestr=sourcetxt.getText().toString();
                pricestr=pricetxt.getText().toString();
                quantitystr=quantitytxt.getText().toString();
                productnamestr=productnametxt.getText().toString();
                params.put("image", imageData);
                params.put("description", descriptionstr);
                params.put("source", sourcestr);
                params.put("price", pricestr);
                params.put("quantity", quantitystr);
                params.put("name",productnamestr);

                return params;
            }
        };
        MySingleton.getInstance(addimages .this).addToRequestQueue(stringRequest);
    }
});
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         if(requestCode==CODE_GALLERY_REQUEST){
             if(grantResults.length> 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                 Intent intent=new Intent(Intent.ACTION_PICK);
                 intent.setType("image/*");
                 startActivityForResult(Intent.createChooser(intent,"select imaege"),CODE_GALLERY_REQUEST);

             }
             else{
                 Toast.makeText(getApplicationContext(),"you have no permission", Toast.LENGTH_LONG).show();
             }
         return;
         }

             super.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if(requestCode==CODE_GALLERY_REQUEST && resultCode== RESULT_OK && data != null){
           Uri filePath=data.getData();

           try{
               InputStream inputStream=getContentResolver().openInputStream(filePath);
               bitmap= BitmapFactory.decodeStream(inputStream);
               img.setImageBitmap(bitmap);

           } catch (FileNotFoundException e){
               e.printStackTrace();
           }

       }
       super.onActivityResult(requestCode,resultCode,data);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes=outputStream.toByteArray();


                String encodeImage= Base64.encodeToString(imageBytes, Base64.DEFAULT);
                 return encodeImage;
    }
}
