package com.example.e146215e.project;

/**
 * Created by E146215E on 16/03/16.
 */
public class Toilette {
    private String adresse;
    private String commune;
    private String horaire;
    private String access;
    private Double latitude;
    private Double longitude;

    public Toilette(String adresse, String commune, String horaire, String access, Double latitude, Double longitude) {
        this.adresse = adresse;
        this.commune = commune;
        this.horaire = horaire;
        this.access = access;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }


    public String getHoraire() {
        return horaire;
    }

    public String getAccess() {
        return access;
    }

    public String getCommune() {
        return commune;
    }

    public String getAdresse() {
        return adresse;
    }


    @Override
    public String toString() {
        return "Toilette{" +
                "adresse='" + adresse + '\'' +
                ", commune='" + commune + '\'' +
                ", horaire='" + horaire + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
