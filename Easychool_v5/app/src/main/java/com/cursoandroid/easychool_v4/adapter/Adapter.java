package com.cursoandroid.easychool_v4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.model.Escola;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Escola> escolas;

    public Adapter(List<Escola> escolas) {
        this.escolas = escolas;
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
        holder.endereco.setText("Rua: " +escola.getRua()+ ", " +String.valueOf(escola.getNumero())+ ", " +escola.getBairro()+ ", " +escola.getCidade()+ ", " +escola.getUf());
        //holder.distancia.setText("Calculando...");
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