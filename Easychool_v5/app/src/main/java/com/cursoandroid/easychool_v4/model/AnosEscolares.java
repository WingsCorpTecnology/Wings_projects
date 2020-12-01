package com.cursoandroid.easychool_v4.model;

import java.util.ArrayList;
import java.util.List;

public class AnosEscolares {
    private List<String> anos = new ArrayList<>();

    public AnosEscolares(){
        setAnos();
    }

    public List<String> getAnos() {
        return anos;
    }

    public void setAnos() {
        List<String> anos = new ArrayList<>();

        anos.add("Berçário 1");
        anos.add("Berçário 2");
        anos.add("Maternal 1");
        anos.add("Maternal 2");
        anos.add("Pré 1");
        anos.add("Pré 2");
        anos.add("1° ano");
        anos.add("2° ano");
        anos.add("3° ano");
        anos.add("4° ano");
        anos.add("5° ano");
        anos.add("6° ano");
        anos.add("7° ano");
        anos.add("8° ano");
        anos.add("9° ano");
        anos.add("1° Médio");
        anos.add("2° Médio");
        anos.add("3° Médio");

        this.anos = anos;
    }
}
