package com.example.e146215e.project;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by E146215E on 16/03/16.
 */
public class MonAdapteur extends ArrayAdapter<Toilette>{
    public MonAdapteur(Activity context, ArrayList<Toilette> items) {
        super (context, R.layout.affichage_toilette, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.affichage_toilette, null);

        TextView adresse = (TextView) row.findViewById(R.id.adresse);
        adresse.setText(getItem(position).getAdresse());

        TextView commune = (TextView) row.findViewById(R.id.commune);
        commune.setText(getItem(position).getCommune());

        TextView horaires = (TextView) row.findViewById(R.id.horaire);
        horaires.setText(getItem(position).getHoraire());

        return (row);
    }
}
