package com.example.e146215e.project;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
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

    private ArrayList<Toilette> list = new ArrayList<Toilette>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // affichage initial
        getToilettes();

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        ArrayList<Integer> intList = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            intList.add(i+1);
        }
        ArrayAdapter<Integer> spinAdapteur = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,intList);
        spinAdapteur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinAdapteur);







        final Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                getToilettes();

                refresh.setEnabled(false);
                refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setEnabled(true);
                    }
                }, 1000);
            }
        });

    }

    public void setList(String adresse, String commune, String horaires){
        this.list.add(new Toilette(adresse, commune, horaires));
    }

    public void addAdapteur(){
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new MonAdapteur(this, list));
    }

    public void getToilettes(){
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

                                    Spinner spin = (Spinner) findViewById(R.id.spinner);

                                    int nbTl = Integer.parseInt(spin.getSelectedItem().toString());

                                    // PROBLEME //
                                    for(int i = 0; i < nbTl; i++){
                                        JSONObject d = data.getJSONObject(i);

                                        String adresse = d.optString("ADRESSE");
                                        String commune = d.optString("COMMUNE");
                                        String horaires = d.optString("INFOS_HORAIRES");
//                                    Double[] latlng = repObj.getJSONObject("_l");

                                        if(horaires.equals("null")){
                                            horaires = "";
                                        }

                                        //Ajoutez a lt
                                        setList(adresse, commune, horaires);
                                    }

                                    addAdapteur();

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
