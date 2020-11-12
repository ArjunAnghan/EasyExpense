package com.example.easyexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUp extends AppCompatActivity {

    // ----------------------------------------------------------------------------------------------------------------------//
    // Getting GUI Parameters
    EditText name = null;
    EditText email = null;
    EditText password = null;
    EditText phone_number = null;
    Button signupbutton = null;
    TextView verify = null;
    boolean verify_flag = false;
    String profilePhoto = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone_number = findViewById(R.id.contact_number);
        signupbutton = findViewById(R.id.signupbutton);
        final TextView verify = findViewById(R.id.verify);

        // ----------------------------------------------------------------------------------------------------------------------//
        // This portion will be used to select a profilePhoto
        try {
            profilePhoto = getIntent().getExtras().getString("profilePhoto");
        }
        catch(Exception E)
        {
          // Handle Exception
        }

        // This will disable signup button, because user should not click it befre verify username
        signupbutton.setEnabled(false);

        email.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if(isEmailValid(s.toString()) == true)
                {
                    verify_flag = true;
                }
                else
                {
                    verify_flag  = false;
                }
            }
        });

        // ----------------------------------------------------------------------------------------------------------------------//
        // This portion will be called when verify text will be clicked
        verify.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(verify_flag == true) {
                    String User_id = email.getText().toString();

                    VerifyUser verify_class = new VerifyUser(SignUp.this);
                    verify_class.execute(User_id);
                }
                else
                {
                    DialogBox.errorMessage(SignUp.this,"Please Enter valid email address!");
                }
            }
        });
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This is for back key control
    @Override
    public void onBackPressed() {
        DialogBox.backButton(SignUp.this);
    }

    // This function will called when signup button will be pressed

    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will be called when signUP button will be clicked
    public void sendData(View view) {

        // Generating random userid
        Random rand = new Random();

        String Name = name.getText().toString().toUpperCase();
        String Password = password.getText().toString();
        String Email = email.getText().toString();
        String Phone_number = phone_number.getText().toString();
        Integer RN = rand.nextInt(100000);
        String Unique_id = RN.toString();


        Name = Name.replace(" ","_");

        if(passwordChecker.calculateStrength(Password).getValue() < passwordChecker.STRONG.getValue())
        {
            DialogBox.errorMessage(SignUp.this,"Password should contain min of 6 characters" +
                    " and at least 1 lowercase, 1 uppercase, " +
                    "1 numeric value and 1 special character!");
        }
        else if(Phone_number.length()!=10)
        {
            DialogBox.errorMessage(SignUp.this,"Enter valid Phone Number.\n" +
                                                "Enter phone number without country code.!");

        }
        else if(profilePhoto == null)
        {
            DialogBox.errorMessage(SignUp.this,"Please select a profile photo");
        }
        else {
            SignUpSend signUpSend = new SignUpSend(SignUp.this);
            signUpSend.execute(Name, Email, Password, Phone_number, Unique_id, profilePhoto);
        }
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This function is checking whether input is valid email id or not?
    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
     //   Toast.makeText(this, matcher.matches()., Toast.LENGTH_SHORT).show();
        return matcher.matches();
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will be called when 'upload profile photo' button will be clicked
    public void selectProfilePhoto(View view)
    {
        Intent captureProfilePhotoIntent = new Intent(this, ProfilePhoto.class);
        startActivity(captureProfilePhotoIntent);
    }
}
