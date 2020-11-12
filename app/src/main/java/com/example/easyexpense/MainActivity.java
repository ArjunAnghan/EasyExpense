package com.example.easyexpense;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // This is for SignIn Layout Parameters
    TextView textView = null;
    EditText email = null;
    EditText password = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is for SignIn Layout Parameters
        textView = findViewById(R.id.sup);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // Check Permission on starting of application
        checkPermissions();


     // ----------------------------------------------------------------------------------------------------------------------//
        // This will be called when Signup Text will be clicked
        // SignUp activity is for signup layout
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }


    // ----------------------------------------------------------------------------------------------------------------------//
    // Back Press Event
    @Override
    public void onBackPressed() {
        DialogBox.backButton(MainActivity.this);
    }


    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will be called on SignIn Submit Button
    // SignIn send will check whether email id and password are correct or not
    public void sendData(View view) {
        signInSend signinSend = new signInSend(MainActivity.this);
        signinSend.execute(email.getText().toString(), password.getText().toString());
    }


    // ----------------------------------------------------------------------------------------------------------------------//
    // This portion is for permission management
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (checkSelfPermission(Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1000);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                checkPermissions();
            }
            return;
        }
    }

    // ----------------------------------------------------------------------------------------------------------------------//
}

