package com.example.uber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sighnup extends AppCompatActivity {
EditText e1_name,e2_email,e3_password,phone;
FirebaseAuth auth;
ProgressDialog dialog;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighnup);
        auth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
        e1_name=findViewById(R.id.editText);
        e2_email=findViewById(R.id.editText2);
        e3_password=findViewById(R.id.editText3);
        phone=findViewById(R.id.phone);
    }
    public  void signupUser(View v) {
        if (e1_name.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "check the name", Toast.LENGTH_SHORT).show();
        } else if (e2_email.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "enter email", Toast.LENGTH_SHORT).show();
        } else if (!e2_email.getText().toString().contains("@")) {
            Toast.makeText(this, "email must contain @", Toast.LENGTH_SHORT).show();
        } else if (!phone.getText().toString().startsWith("254")) {
            Toast.makeText(this, "phone number must start with 2547....", Toast.LENGTH_SHORT).show();
        } else {
            dialog.setMessage("Registering .Please wait.....");
            dialog.show();
            String name = e1_name.getText().toString();
            String email = e2_email.getText().toString();
            String password = e3_password.getText().toString();
            if (name.equals("") || email.equals("") && password.equals("")) {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                dialog.hide();
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.hide();
                            Toast.makeText(sighnup.this, "user registered succesfully", Toast.LENGTH_SHORT).show();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                            users Users = new users(e1_name.getText().toString(), e2_email.getText().toString(), e3_password.getText().toString(), phone.getText().toString());
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            databaseReference.child(firebaseUser.getUid()).setValue(Users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(sighnup.this, "user data saved", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(sighnup.this, "user data could not be saved", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                            Intent i = new Intent(sighnup.this,SighnIn.class);
                            startActivity(i);
                            finish();
                        } else {
                            dialog.hide();
                            Toast.makeText(sighnup.this, "user could not be registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
