package com.cursoandroid.easychool_v4.model;

import java.util.ArrayList;
import java.util.List;

public class TipoEscola {
    private List<String> tipos = new ArrayList<>();

    public TipoEscola() {
        setTipos();
    }

    public List<String> getTipos() {
        return tipos;
    }

    public void setTipos() {
        List<String> tipos = new ArrayList<>();

        tipos.add("Escolas Municipais");
        tipos.add("Escolas Federais");
        tipos.add("Escolas Estaduais");

        this.tipos = tipos;
    }
}
