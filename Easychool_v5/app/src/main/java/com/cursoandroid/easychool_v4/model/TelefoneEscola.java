package com.cursoandroid.easychool_v4.model;

import java.io.Serializable;

public class TelefoneEscola implements Serializable {
    private String id;
    private String telefone;
    private String idEscola;

    public TelefoneEscola() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getIdEscola() {
        return idEscola;
    }

    public void setIdEscola(String idEscola) {
        this.idEscola = idEscola;
    }
}