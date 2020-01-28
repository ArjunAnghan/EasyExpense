package com.example.easyexpense;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class SignUpSend extends AsyncTask<String, Void, String> {

    Context ctx;
    private ProgressDialog dialog;

    SignUpSend(Context ctx) {

        this.ctx = ctx;
        dialog = new ProgressDialog(ctx);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Please wait...");
        dialog.show();
    }


    @Override
    protected String doInBackground(final String... strings) {

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        final String name = strings[0];
        final String user_id = strings[1];
        final String password = strings[2];
        final String phone_number = strings[3];
        final String occupation = "student";
        final String unique_id = strings[4];

        StringRequest request = new StringRequest(Request.Method.POST, AttributeData.getUrl()+"/user_insert.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();

                String[] responses = response.split(":");

                if(responses[0].equals("insert"))
                {
                    if(responses[1].equals("1"))
                    {
                        AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
                        adb.setMessage("SignUp Successfully");
                        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent signinIntent = new Intent(ctx, MainActivity.class);
                                ctx.startActivity(signinIntent);
                            }
                        });
                        adb.show();
                    }
                    else
                    {
                        final AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
                        adb.setTitle("Error");
                        adb.setMessage("There is error.\n Please check data.");
                        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        adb.show();
                    }
                }
                else
                {
                    AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
                    adb.setTitle("Alert");
                    adb.setMessage("Bad Parameters");
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
                    adb.show();
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
                parameters.put("user_id", user_id);
                parameters.put("password", password);
                parameters.put("phone_number", phone_number);
                parameters.put("occupation", occupation);
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
        // Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }
}
