package com.cursoandroid.easychool_v4.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FiltrosPesquisa implements Serializable {
    private List<Integer> filtrosDistancia = new ArrayList<>();

    public FiltrosPesquisa() {
    }

    public FiltrosPesquisa(List<Integer> filtros) {
        setFiltrosDistancia(filtros);
    }

    public Integer getFiltrosDistancia() {
        return filtrosDistancia.get(0);
    }

    public void setFiltrosDistancia(List<Integer> filtrosDistancia) {
        this.filtrosDistancia = filtrosDistancia;

        Log.i("teste", "valor: "+filtrosDistancia.get(0));
    }
}