package com.example.easyexpense;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUpSend extends AsyncTask<String, Void, String> {

    Context ctx;
    private SweetAlertDialog pDialog;

    // ----------------------------------------------------------------------------------------------------------------------//
    // This is the constructer
    SignUpSend(Context ctx) {
        this.ctx = ctx;
        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will be called before the execution
    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This is the main function
    @Override
    protected String doInBackground(final String... strings) {

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        final String name = strings[0];
        final String email = strings[1];
        final String password = strings[2];
        final String phone_number = strings[3];
        final String unique_id = strings[4];
        final String profilePhoto = strings[5];

        StringRequest request = new StringRequest(Request.Method.POST, AttributeData.getUrl()+"/user_insert.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.cancel();

                String[] responses = response.split(":");

                if(responses[0].equals("insert"))
                {
                    if(responses[1].equals("1"))
                    {
                        new SweetAlertDialog(ctx,SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Signup Successfully")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent signinIntent = new Intent(ctx, MainActivity.class);
                                        ctx.startActivity(signinIntent);
                                    }
                                })
                                .show();
                    }
                    else
                    {
                        DialogBox.errorMessage(ctx,"Please enter valid data!");
                    }
                }
                else
                {
                    DialogBox.errorMessage(ctx, "Bad Parameters!");
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
                parameters.put("name", name);
                parameters.put("email", email);
                parameters.put("password", password);
                parameters.put("phone_number", phone_number);
                parameters.put("unique_id", unique_id);
                parameters.put("profilePhoto",profilePhoto);

                return parameters;
            }
        };

        requestQueue.add(request);
        return null;
    }


    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will be called if there will be some changes during the process
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will be called after process finished
    @Override
    protected void onPostExecute(String result) {
    }
}
