package com.example.easyexpense;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VerifyUser extends AsyncTask<String, Void, String> {


    Context ctx;
    private SweetAlertDialog pDialog;

    // ----------------------------------------------------------------------------------------------------------------------//
    // This is the constructer
    VerifyUser(Context ctx) {
        this.ctx = ctx;
        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will be called before the execution
    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Verifying...");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This is the main function
    @Override
    protected String doInBackground(final String... strings) {

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        final String email = strings[0];

        StringRequest request = new StringRequest(Request.Method.POST, AttributeData.getUrl()+"/verifyuser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pDialog.cancel();

                String[] strings = response.split(":");
                if(strings[0].equals("exist"))
                {
                    if(strings[1].equals("0"))
                    {
                        new SweetAlertDialog(ctx,SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Verified")
                                .show();

                        TextView verify = (TextView) ((Activity)ctx).findViewById(R.id.verify);
                        verify.setText("Verified");
                        verify.setTextColor(Color.GREEN);

                        // This will disable username field after verified
                        EditText email = (EditText) ((Activity)ctx).findViewById(R.id.email);
                        email.setEnabled(false);

                        // Enable signup button
                        Button signupbutton = (Button) ((Activity)ctx).findViewById(R.id.signupbutton);
                        signupbutton.setEnabled(true);
                    }
                    else
                    {
                        new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Email Already Registered")
                                .show();
                    }
                }
                else
                {
                    new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Bad Parameters. Please contact a developer")
                            .show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", email);

                return parameters;
            }
        };

        requestQueue.add(request);
        return null;
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will be called if some changes will occur during the process
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will be called after process finished
    @Override
    protected void onPostExecute(String result) {
        // Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }
}
