package com.cursoandroid.easychool_v4.model;

import java.util.ArrayList;
import java.util.List;

public class NivelEducacao {
    private List<String> niveis = new ArrayList<>();

    public NivelEducacao(){
        setNiveis();
    }

    public void setNiveis() {
        List<String> niveis = new ArrayList<>();

        niveis.add("Educação Infantil");
        niveis.add("Ensino Fundamental I");
        niveis.add("Ensino Fundamental II");
        niveis.add("Ensino Médio");
        niveis.add("Educação Especial");
        niveis.add("EJA");

        this.niveis = niveis;
    }

    public List<String> getNiveis() {
        return niveis;
    }
}