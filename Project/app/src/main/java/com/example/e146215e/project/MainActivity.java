package com.example.e146215e.project;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    private ArrayList<Toilette> list = new ArrayList<Toilette>();

    private static final int PLACE_PICKER_REQUEST = 1;
    private static double latN = 47.2172500;
    private static double lngN = -01.5533600;
    private static double taille = 0.005;
    private static double distance = 0.02;

    private boolean pmrAccess = false;


    private static LatLngBounds BOUNDS_MOUTAINS_VIEW = new LatLngBounds(
            new LatLng(latN-taille, lngN-taille), new LatLng(latN+taille, lngN+taille));

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


        Button pickerButton = (Button) findViewById(R.id.map);
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUTAINS_VIEW);
                    Intent intent = intentBuilder.build(MainActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        final Switch pmr = (Switch) findViewById(R.id.pmr);

        final Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                if(pmr.isChecked()){
                    pmrAccess = true;
                } else {
                    pmrAccess = false;
                }
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

    public void setList(String adresse, String commune, String horaires, String access, Double lat, Double lng){
        this.list.add(new Toilette(adresse, commune, horaires, access, lat, lng));
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
                                    int cpt = 0;
                                    int i = 0;
                                    while(cpt < nbTl){
                                        JSONObject d = data.getJSONObject(i);

                                        String adresse = d.optString("ADRESSE");
                                        String commune = d.optString("COMMUNE");
                                        String horaires = d.optString("INFOS_HORAIRES");
                                        String access = d.optString("ACCESSIBILITE_PMR");
                                        String coords = d.optString("_l");

                                        coords = coords.substring(1, coords.length()-1);
                                        String[] latlng = coords.split(",");

                                        Double lat = Double.parseDouble(latlng[0]);
                                        Double lng = Double.parseDouble(latlng[1]);

                                        Double dlat = Math.abs(latN - lat);
                                        Double dlng = Math.abs(lngN - lng);

                                        if(!pmrAccess){
                                            if(dlat < distance && dlng < distance){
                                                if(horaires.equals("null")) {
                                                    horaires = "";
                                                }
                                                setList(adresse, commune, horaires, access, lat, lng);
                                                cpt ++;
                                            }
                                        } else {
                                            if(dlat < distance && dlng < distance && access.equals("oui")){
                                                if(horaires.equals("null")) {
                                                    horaires = "";
                                                }
                                                Log.e("onResponse: ", access);
                                                setList(adresse, commune, horaires, access, lat, lng);
                                                cpt ++;
                                            }
                                        }

                                        i ++;

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
    protected void onActivityResult(int resquestCode,
                                    int resultCode, Intent data) {
        if(resquestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {
            final Place place = PlacePicker.getPlace(data, this);

            latN = place.getLatLng().latitude;
            lngN = place.getLatLng().longitude;
            BOUNDS_MOUTAINS_VIEW = new LatLngBounds(
                    new LatLng(latN-taille, lngN-taille), new LatLng(latN+taille, lngN+taille));
        } else {
            super.onActivityResult(resquestCode, resultCode, data);
        }
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
