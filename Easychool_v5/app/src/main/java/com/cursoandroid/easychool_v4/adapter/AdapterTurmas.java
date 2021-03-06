package com.cursoandroid.easychool_v4.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.model.Turma;

import java.util.List;

public class AdapterTurmas extends RecyclerView.Adapter<AdapterTurmas.MyViewHolder>{
    private List<Turma> turmas;

    public AdapterTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem_turmas_vagas, parent, false);
        return new MyViewHolder(itemLista);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Turma turma = turmas.get(position);
        int vagas = turma.getVagasTotal() - turma.getVagasOcupadas();

        //Log.i("teste", "ultima alteração: "+turma.getUltimaAlteracao());

        holder.turma.setText(turma.getSerie());
        holder.vagas.setText(vagas + " vagas de " + turma.getVagasTotal());
        holder.ultimaAlteracao.setText(turma.getUltimaAlteracao());
        holder.periodo.setText(turma.getPeriodo());

        if (turma.getPeriodo().equals("Noite")) {
            holder.periodo.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_brightness_3_24, 0);
        }
    }

    @Override
    public int getItemCount() {
        return turmas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView turma, vagas, ultimaAlteracao, periodo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            turma = itemView.findViewById(R.id.txt_ano_turma);
            vagas = itemView.findViewById(R.id.txt_qtd_vagas);
            ultimaAlteracao = itemView.findViewById(R.id.txt_data_hora_ultima_alteracao);
            periodo = itemView.findViewById(R.id.txt_periodo);
        }
    }
}
