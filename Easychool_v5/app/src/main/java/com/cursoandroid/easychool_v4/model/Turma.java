package com.cursoandroid.easychool_v4.model;

import java.io.Serializable;
import java.util.List;

public class Turma implements Serializable {
    private String serie;
    private int vagasDisponiveis;
    private int vagasTotal;
    private int vagasOcupadas;
    private String periodo;
    private String nivelEducacao;
    private String ultimaAlteracao;

    public Turma() {
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    private void setVagasDisponiveis() {
        vagasDisponiveis = getVagasTotal() - getVagasOcupadas();
    }

    public int getVagasTotal() {
        return vagasTotal;
    }

    public void setVagasTotal(int vagasTotal) {
        this.vagasTotal = vagasTotal;
    }

    public int getVagasOcupadas() {
        return vagasOcupadas;
    }

    public void setVagasOcupadas(int vagasOcupadas) {
        this.vagasOcupadas = vagasOcupadas;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNivelEducacao() {
        return nivelEducacao;
    }

    private void setNivelEducacao() {
        NivelEducacao nivel = new NivelEducacao();
        AnosEscolares anos = new AnosEscolares();

        List<String> nivelEd = nivel.getNiveis();
        List<String> anosEd = anos.getAnos();

        for(int i = 0; i < anosEd.size(); i++){
            if(anosEd.get(i).equals(getSerie())){
                if(i < 6){
                    nivelEducacao = nivelEd.get(0);
                }
                else if(i < 11){
                    nivelEducacao = nivelEd.get(1);
                }
                else if(i < 15){
                    nivelEducacao = nivelEd.get(2);
                }
                else{
                    nivelEducacao = nivelEd.get(3);
                }
            }
        }
    }

    private void setNivelEducacao(String nivel){
        NivelEducacao niveis = new NivelEducacao();
        List<String> nivelEd = niveis.getNiveis();

        for(int i = 0; i < nivelEd.size(); i++) {
            if (nivel.equals(nivelEd.get(i))){
                if(i == 4){
                    nivelEducacao = nivelEd.get(4);
                }
                else if(i == 5){
                    nivelEducacao = nivelEd.get(5);
                }
            }
        }
    }

    public String getUltimaAlteracao() {
        return ultimaAlteracao;
    }

    public void setUltimaAlteracao(String ultimaAlteracao) {
        this.ultimaAlteracao = ultimaAlteracao;
    }
}