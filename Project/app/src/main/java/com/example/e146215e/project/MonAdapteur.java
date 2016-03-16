package com.example.e146215e.project;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by E146215E on 16/03/16.
 */
public class MonAdapteur extends ArrayAdapter<Toilette>{
    public MonAdapteur(Activity context, ArrayList<Toilette> items) {
        super (context, R.layout.affichage_toilette, items);
    }
}
