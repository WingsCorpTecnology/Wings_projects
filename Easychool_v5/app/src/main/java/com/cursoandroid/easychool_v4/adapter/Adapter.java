package com.cursoandroid.easychool_v4.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.model.Escola;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Escola> escolas;
    private List<Double> distancia;
    //private List<Uri> perfil;

    public Adapter(List<Escola> escolas, List<Double> distancia/*, List<Uri> perfil*/) {
        this.escolas = escolas;
        this.distancia = distancia;
        //this.perfil = perfil;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem_escolas, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Escola escola = escolas.get(position);

        holder.nome.setText(escola.getNome());
        holder.endereco.setText("Rua: " + escola.getRua() + ", " + String.valueOf(escola.getNumero()) + ", " + escola.getBairro() + ", " + escola.getCidade() + ", " + escola.getUf());

        if(distancia.size() != 0) {
            Double distancias = distancia.get(position);

            holder.distancia.setText(distancias + " Km");
        }
        else{
            holder.distancia.setText("Não foi possível calcular");
        }
    }


    @Override
    public int getItemCount() {
        return escolas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome, endereco, distancia;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txt_nome_escola);
            endereco = itemView.findViewById(R.id.txt_endereco_escola);
            distancia = itemView.findViewById(R.id.txt_distancia);
        }
    }
}