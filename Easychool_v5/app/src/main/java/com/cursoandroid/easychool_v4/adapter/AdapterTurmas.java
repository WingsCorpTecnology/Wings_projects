package com.cursoandroid.easychool_v4.adapter;

import android.os.Build;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Turma turma = turmas.get(position);

        holder.turma.setText(turma.getSerie());
        holder.vagas.setText(turma.getVagasDisponiveis());
        holder.ultimaAlteracao.setText(turma.getUltimaAlteracao());
        holder.periodo.setText(turma.getPeriodo());
        holder.nivel.setText(turma.getNivelEducacao());

        if(turma.getPeriodo().equals("Noturno")){
            holder.periodo.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_brightness_3_24, 0);
        }
    }

    @Override
    public int getItemCount() {
        return turmas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView turma, vagas, ultimaAlteracao, periodo, nivel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            turma = itemView.findViewById(R.id.txt_ano_turma);
            vagas = itemView.findViewById(R.id.txt_qtd_vagas);
            ultimaAlteracao = itemView.findViewById(R.id.txt_periodo);
            periodo = itemView.findViewById(R.id.txt_periodo);
            nivel = itemView.findViewById(R.id.txt_nivel_educacao);
        }
    }
}
