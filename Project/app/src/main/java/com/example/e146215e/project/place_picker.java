package com.example.e146215e.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class place_picker extends Activity {
    private static final int PLACE_PICKER_REQUEST = 1;
    private TextView mName;
    private TextView mAdresse;
    private TextView mPosition;

    private static double latN = 47.2172500;
    private static double lngN = -01.5533600;
    private static double taille = 0.005;


    private static final LatLngBounds BOUNDS_MOUTAINS_VIEW = new LatLngBounds(
            new LatLng(latN-taille, lngN-taille), new LatLng(latN+taille, lngN+taille));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        mName = (TextView) findViewById(R.id.textView);
        mAdresse = (TextView) findViewById(R.id.textView2);
        mPosition = (TextView) findViewById(R.id.textView3);
        Button pickerButton = (Button) findViewById(R.id.pickerButton);

        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUTAINS_VIEW);
                    Intent intent = intentBuilder.build(place_picker.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int resquestCode,
                                    int resultCode, Intent data) {
        if(resquestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {
            final Place place = PlacePicker.getPlace(data, this);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String position = (String) place.getLatLng().toString();
            if(position == null){
                position = "";
            }

            mName.setText(name);
            mAdresse.setText(address);
            mPosition.setText(Html.fromHtml(position));

        } else {
            super.onActivityResult(resquestCode, resultCode, data);
        }
    }
}
