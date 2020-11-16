package com.cursoandroid.easychool_v4.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Geocoding {
    private Geocoder geocoder;
    private String cep;
    private Double latitude;
    private Double longitude;

    public Geocoding(Context context, String enderecoCompleto) {
        geocoder = new Geocoder(context, Locale.getDefault());

        try{
            List<Address> listaEndereco = geocoder.getFromLocationName(enderecoCompleto, 1);

            if(listaEndereco != null && listaEndereco.size() > 0){
                Address endereco = listaEndereco.get(0);

                setCep(endereco.getPostalCode());
                setLatitude(endereco.getLatitude());
                setLongitude(endereco.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
