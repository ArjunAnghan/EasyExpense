package com.example.easyexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class afterLogin extends AppCompatActivity {

    // Getting GUI parameters
    String unique_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
    }

    // This function will be called when 'UPLOAD' button will be clicked
    public void uploadBills(View view) {
        Intent intent = new Intent(afterLogin.this, CaptureImage.class);
        startActivity(intent);
    }

    // This function will be called when 'STATUS' button will be clicked
    public void status(View view) {

        // Getting the unique id
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("EasyExpense",0);
        unique_id = sharedPreferences.getString("unique_id",null);

        getBills GetBills = new getBills(afterLogin.this);
        GetBills.execute(unique_id);
    }
}
