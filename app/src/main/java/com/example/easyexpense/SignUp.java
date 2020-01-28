package com.example.easyexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Random;

public class SignUp extends AppCompatActivity {

    EditText name = null;
    EditText user_id = null;
    EditText password = null;
    EditText phone_number = null;
    Button signupbutton = null;
    TextView verify = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        user_id = findViewById(R.id.user_id);
        password = findViewById(R.id.password);
        phone_number = findViewById(R.id.contact_number);
        signupbutton = findViewById(R.id.signupbutton);
        TextView verify = findViewById(R.id.verify);

        signupbutton.setEnabled(false);

        verify.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                String User_id = user_id.getText().toString();

                VerifyUser verify_class= new VerifyUser(SignUp.this);
                verify_class.execute(User_id);
            }
        });
    }

    public void sendData(View view) {

        Random rand = new Random();
        String Name = name.getText().toString().toUpperCase();
        String Password = password.getText().toString();
        String User_id = user_id.getText().toString();
        String Phone_number = phone_number.getText().toString();
        Integer RN = rand.nextInt(100000);
        String Unique_id = RN.toString();

        Name = Name.replace(" ","_");

        if(passwordChecker.calculateStrength(Password).getValue() < passwordChecker.STRONG.getValue())
        {
              AlertDialog.Builder adb = new AlertDialog.Builder(SignUp.this);
            adb.setTitle("Alert");
            adb.setMessage("Password should contain min of 6 characters and at least 1 lowercase, 1 uppercase, 1 numeric value and 1 special " +
                    "character");
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            adb.show();
        }
        else {
            SignUpSend signUpSend = new SignUpSend(SignUp.this);
            signUpSend.execute(Name, User_id, Password, Phone_number, Unique_id);
        }
    }
}
