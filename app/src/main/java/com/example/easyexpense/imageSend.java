package com.example.easyexpense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

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

public class imageSend extends AsyncTask<String, Void, String> {

    Context ctx;
    private SweetAlertDialog pDialog;
    String unique_id = null;

    // ----------------------------------------------------------------------------------------------------------------------//
    // This is the constructor
    imageSend(Context ctx) {

        // Getting unique id
        SharedPreferences sharedPreferences = ctx.getApplicationContext().getSharedPreferences("EasyExpense",0);
        unique_id = sharedPreferences.getString("unique_id",null);

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

        final String encodedImage = strings[0];
        final String name = strings[1];
        final String date = strings[2];
        final String total = strings[3];

        StringRequest request = new StringRequest(Request.Method.POST, AttributeData.getUrl()+"/insertImage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.cancel();

                String[] responses = response.split(":");

                Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();
                if(responses[0].equals("upload"))
                {
                    if(responses[1].equals("1"))
                    {
                        new SweetAlertDialog(ctx,SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Upload Successfully")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(ctx, OCR_Text.class);
                                        ctx.startActivity(intent);
                                    }
                                })
                                .show();
                    }
                    else
                    {
                        DialogBox.errorMessage(ctx,"There is some error. Please contact the admin!");
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

                parameters.put("image",encodedImage);
                parameters.put("unique_id",unique_id);
                parameters.put("name",name);
                parameters.put("date",date);
                parameters.put("total",total);

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
        // Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }
}
