package com.cursoandroid.easychool_v4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.model.Escola;
import com.cursoandroid.easychool_v4.model.NivelEducacao;

import java.util.List;

public class AdapterFiltros extends RecyclerView.Adapter<AdapterFiltros.MyViewHolder>{
    private List<String> niveis;

    public AdapterFiltros() {
        NivelEducacao nivelEducacao = new NivelEducacao();

        this.niveis = nivelEducacao.getNiveis();
    }

    @Override
    public AdapterFiltros.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filtros_pesquisa, parent, false);
        return new AdapterFiltros.MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(AdapterFiltros.MyViewHolder holder, int position) {
        holder.nivel.setText(niveis.get(position));
        holder.imgNivel.setImageResource(R.drawable.all_mountain);
    }


    @Override
    public int getItemCount() {
        return niveis.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nivel;
        ImageView imgNivel;

        public MyViewHolder(View itemView) {
            super(itemView);

            nivel = itemView.findViewById(R.id.txtNivelEnsino);
            imgNivel = itemView.findViewById(R.id.imgNivel);
        }
    }
}
