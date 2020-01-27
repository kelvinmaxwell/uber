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

public class SighnIn extends AppCompatActivity {
EditText el_email,e2_password;
FirebaseAuth auth;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighn_in);
        el_email=findViewById(R.id.signintxt1);
        e2_password=findViewById(R.id.sighnintxt2);
        auth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
    }


    public void signinuser(View v){
        dialog.setMessage("Signing in.Please Wait....");
        dialog.show();

        if(el_email.getText().toString().equals("") && e2_password.getText().toString().equals("")){
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }

        else{
            auth.signInWithEmailAndPassword(el_email.getText().toString(),e2_password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
if(task.isSuccessful()){
    dialog.hide();
    Toast.makeText(SighnIn.this, "user signed in sussesfully", Toast.LENGTH_SHORT).show();
    Intent i=new Intent(SighnIn.this,Mainpage.class);
    startActivity(i);
    finish();
}
else {
    dialog.hide();
    Toast.makeText(SighnIn.this, "User not found", Toast.LENGTH_SHORT).show();
}
                        }
                    });
        }
    }

}
