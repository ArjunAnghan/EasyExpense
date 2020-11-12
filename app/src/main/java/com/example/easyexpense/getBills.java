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

public class getBills extends AsyncTask<String, Void, String> {

    Context ctx;
    private SweetAlertDialog pDialog;

    // This is the constructor
    getBills(Context ctx) {

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
        final String unique_id = strings[0]; // response is JSON object

        StringRequest request = new StringRequest(Request.Method.POST, AttributeData.getUrl()+"/getBills.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.cancel();

                Intent intent = new Intent(ctx,showBiils.class);
                intent.putExtra("response",response);
                ctx.startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("unique_id", unique_id);

                return parameters;
            }
        };

        requestQueue.add(request);
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
