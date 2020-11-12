package com.example.easyexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class showBiils extends AppCompatActivity {

    private ListView lvStatus;
    private String[] name;
    private String[] status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_biils);

        lvStatus = findViewById(R.id.lvStatus);

        Bundle extras = getIntent().getExtras();
        String response = extras.getString("response");

        try {

            // Converting JSON string to JSON object
            JSONObject jsonObject = new JSONObject(response);

            // Getting JSON keys
            JSONArray keys = jsonObject.names();

            name = new String[jsonObject.length()];
            status = new String[jsonObject.length()];

            for(int i=0; i<jsonObject.length(); i++){

                // Getting inner JSON object
                JSONObject tempObj = jsonObject.getJSONObject(keys.getString(i));

                // This is the name and status list
                name[i] = tempObj.getString("name");
                status[i] = tempObj.getString("status");
            }

            StatusAdapter statusAdapter = new StatusAdapter(showBiils.this, name, status);
            lvStatus.setAdapter(statusAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

class StatusAdapter extends BaseAdapter{

    private LayoutInflater inflater;

    private Context context;
    private String[] name;
    private String[] status;

    // This is the constrctor
    public StatusAdapter(Context context, String[] name, String[] status){
        this.context = context;
        this.name = name;
        this.status = status;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // This function is for GUI
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null){
            row = inflater.inflate(R.layout.item_status_row, parent, false);
        }else{
            row = convertView;
        }

        ((TextView)row.findViewById(R.id.tvName)).setText(name[position]);
        ((TextView)row.findViewById(R.id.tvStatus)).setText(status[position]);

        // Showing text with color
        if(status[position].equals("rejected")){
            ((TextView)row.findViewById(R.id.tvStatus)).setTextColor(Color.parseColor("#FF0000"));
        }else if(status[position].equals("pending")){
            ((TextView)row.findViewById(R.id.tvStatus)).setTextColor(Color.parseColor("#E4CD05"));
        }else {
            ((TextView)row.findViewById(R.id.tvStatus)).setTextColor(Color.parseColor("#00FF00"));
        }

        return row;
    }
}
