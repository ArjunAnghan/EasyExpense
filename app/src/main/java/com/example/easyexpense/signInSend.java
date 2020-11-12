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

public class signInSend extends AsyncTask<String, Void, String> {

    Context ctx;
    private SweetAlertDialog pDialog;

    // This is the constructor
    signInSend(Context ctx) {

        this.ctx = ctx;
        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This will run before data will be sent
    @Override
    protected void onPreExecute() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will send data and receive a response
    @Override
    protected String doInBackground(final String... strings) {

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        final String email = strings[0];
        final String password = strings[1];

        StringRequest request = new StringRequest(Request.Method.POST, AttributeData.getUrl()+"/signin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.cancel();

                String[] responses = response.split(":");

                if(responses[0].equals("exist"))
                {
                    if(responses[1].equals("1"))
                    {

                        // It will store used login data
                        SharedPreferences sharedPreferences = ctx.getApplicationContext().getSharedPreferences("EasyExpense",0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("unique_id",responses[2]);
                        editor.apply();

                        Intent intent = new Intent(ctx,afterLogin.class);
                        ctx.startActivity(intent);
                    }
                    else
                    {
                        DialogBox.errorMessage(ctx,"Wrong username or password!");
                    }
                }
                else
                {
                    DialogBox.errorMessage(ctx,"Bad Parameters");
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
                parameters.put("password", password);

                return parameters;
            }
        };

        requestQueue.add(request);
        return null;
    }


    // ----------------------------------------------------------------------------------------------------------------------//
    // This  function will be called if something occured during process
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    // ----------------------------------------------------------------------------------------------------------------------//
    // This function will be executed after process will be finished
    @Override
    protected void onPostExecute(String result) {

    }
}
