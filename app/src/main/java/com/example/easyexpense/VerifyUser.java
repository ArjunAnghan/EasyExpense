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

public class VerifyUser extends AsyncTask<String, Void, String> {

    Context ctx;
    private ProgressDialog dialog;

    VerifyUser(Context ctx) {
        this.ctx = ctx;
        dialog = new ProgressDialog(ctx);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Verifying...");
        dialog.show();
    }


    @Override
    protected String doInBackground(final String... strings) {

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        final String user_id = strings[0];

        StringRequest request = new StringRequest(Request.Method.POST, AttributeData.getUrl()+"/verifyuser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dialog.cancel();

                String[] strings = response.split(":");
                if(strings[0].equals("exist"))
                {
                    if(strings[1].equals("0"))
                    {
                        TextView verify = (TextView) ((Activity)ctx).findViewById(R.id.verify);
                        verify.setText("Verified");
                        verify.setTextColor(Color.GREEN);

                        EditText user_id = (EditText) ((Activity)ctx).findViewById(R.id.user_id);
                        user_id.setEnabled(false);

                        Button signupbutton = (Button) ((Activity)ctx).findViewById(R.id.signupbutton);
                        signupbutton.setEnabled(true);
                    }
                    else
                    {
                        AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
                        adb.setTitle("Alert");
                        adb.setMessage("User name already exists");
                        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        adb.show();
                    }
                }
                else
                {
                    Toast.makeText(ctx, "Bad Parameters", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id", user_id);

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
        // Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }
}
