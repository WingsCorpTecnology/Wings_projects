package com.cursoandroid.easychool_v4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.model.TelefoneEscola;

import java.util.List;

public class AdapterTelefones extends RecyclerView.Adapter<AdapterTelefones.MyViewHolder> {

    private List<TelefoneEscola> telefones;

    public AdapterTelefones(List<TelefoneEscola> telefones) {
        this.telefones = telefones;
    }

    @Override
    public AdapterTelefones.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_telefones, parent, false);
        return new AdapterTelefones.MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(AdapterTelefones.MyViewHolder holder, int position) {
        TelefoneEscola telefoneEscola = telefones.get(position);

        holder.telefone.setText(telefoneEscola.getTelefone());
    }


    @Override
    public int getItemCount() {
        return telefones.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView telefone;

        public MyViewHolder(View itemView) {
            super(itemView);

            telefone = itemView.findViewById(R.id.txt_telefone);
        }
    }
}