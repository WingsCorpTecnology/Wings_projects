package com.cursoandroid.easychool_v4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.easychool_v4.R;

import java.util.List;

public class AdapterTelefones extends RecyclerView.Adapter<AdapterTelefones.MyViewHolder> {

    private List<String> telefones;

    public AdapterTelefones(List<String> telefones) {
        this.telefones = telefones;
    }

    @Override
    public AdapterTelefones.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_telefones, parent, false);
        return new AdapterTelefones.MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(AdapterTelefones.MyViewHolder holder, int position) {
        String telefoneEscola = telefones.get(position);

        if(telefones.size() != 0) {
            holder.telefone.setText(telefoneEscola);
        }
        else{
            holder.telefone.setText("Nenhum telefone cadastrado");
        }
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