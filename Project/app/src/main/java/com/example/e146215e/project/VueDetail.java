package com.example.e146215e.project;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VueDetail extends FragmentActivity implements OnMapReadyCallback {

    private double latN;
    private double lngN;
    private double tl_lat;
    private double tl_lng;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue_detail);

        TextView adresse = (TextView) findViewById(R.id.adresse);
        TextView commune = (TextView) findViewById(R.id.commune);
        TextView disponibilite = (TextView) findViewById(R.id.disponibilite);
        TextView access = (TextView) findViewById(R.id.access);


        //paramètres importés
        Bundle b = getIntent().getExtras();

        adresse.setText(b.getString("adresse").replace("-", System.getProperty("line.separator")));
        commune.setText(b.getString("commune"));
        disponibilite.setText(b.getString("horaire"));
        access.setText(b.getString("access"));
        tl_lat = b.getDouble("latitude");
        tl_lng = b.getDouble("longitude");
        latN = b.getDouble("latN");
        lngN= b.getDouble("lngN");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApiIfAvailable(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //marker toilette
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(tl_lat, tl_lng))
                .title("Toilette")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        //marker notre position
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latN, lngN))
                .title("Vous")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //position map
        Double latPosMap = (tl_lat + latN) / 2;
        Double lngPosMap = (tl_lng + lngN) / 2;
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latPosMap, lngPosMap)));
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "VueDetail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.e146215e.project/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "VueDetail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.e146215e.project/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
