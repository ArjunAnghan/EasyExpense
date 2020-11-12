package com.example.easyexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OCR_Text extends AppCompatActivity {

    TextView textView = null;
    TextView selectDate = null;
    int mYear,mMonth,mDay;
    EditText nametext = null;
    EditText totaltext = null;
    String encodedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr__text);

        encodedImage = getIntent().getExtras().getString("encodedImage","defaultKey");

        selectDate = (TextView) findViewById(R.id.dateText);

        nametext = (EditText)findViewById(R.id.nameText);
        nametext.setText("EASY MART");

        totaltext = (EditText)findViewById(R.id.totalText);
        totaltext.setText("1710");

        selectDate.setText("08-07-2017");

    }


    // ----------------------------------------------------------------------------------------------------------------------//
    // This funtion is for showing the calender
    public void selectdate(View view)
    {
        final Calendar c = Calendar.getInstance();
        mYear = 2016;
        mMonth = 01;
        mDay = 03;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        selectDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    // This is for backpress handling
    @Override
    public void onBackPressed() {
        DialogBox.backButton(OCR_Text.this);
    }

    public void submit(View view) {
        String name = nametext.getText().toString();
        String date = selectDate.getText().toString();
        String total = totaltext.getText().toString();
        imageSend imagesend = new imageSend(OCR_Text.this);
        imagesend.execute(encodedImage,name,date,total);
    }
}