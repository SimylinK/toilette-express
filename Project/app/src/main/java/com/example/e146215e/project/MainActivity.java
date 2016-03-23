package com.example.e146215e.project;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) findViewById(R.id.listView);
        final ArrayList<Toilette> lt = new ArrayList<Toilette>();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =
                new StringRequest(
                        Request.Method.GET,
                        "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00170/Toilettes_publiques_nm_STBL/content",
                        new Response.Listener<String>() {
                            public void onResponse(String response) {

                                try {
                                    JSONObject reader = new JSONObject(response);
                                    JSONArray data = reader.optJSONArray("data");

                                    // PROBLEME //
                                    for(int i = 0; i < 15; i++){
                                        JSONObject d = data.getJSONObject(i);

                                        String adresse = d.optString("ADRESSE").toString();
                                        String commune = d.optString("COMMUNE").toString();
                                        String horaires = d.optString("INFOS_HORAIRES").toString();
//                                    Double[] latlng = repObj.getJSONObject("_l");

                                        //Ajoutez a lt
                                        lt.add(new Toilette(adresse, commune, horaires, 1.0, 1.0));
                                    }

                                } catch (JSONException je) {
                                    Log.e("Une erreur est survenu", je.getMessage());
                                }


                            }},
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.getMessage());
                            }})
                {
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        return params;
                    }
                };
        queue.add(stringRequest);

        lv.setAdapter(new MonAdapteur(this, lt));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
