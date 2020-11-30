package com.cursoandroid.easychool_v4.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.activity.*;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class ConfigurationFragment extends Fragment {
    private TextView btnPerfil, btnFiltrosPesquisa, btnEndereco, btnLogout;
    private FirebaseAuth autenticacao;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_configuracoes, container, false);
        btnPerfil = root.findViewById(R.id.btn_config_perfil);
        btnFiltrosPesquisa = root.findViewById(R.id.btn_config_filtros);
        btnEndereco = root.findViewById(R.id.btn_config_endereco);
        btnLogout = root.findViewById(R.id.btn_logout);

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configurarPerfilAbrir();
            }
        });

        btnFiltrosPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configurarFiltrosPesquisaAbrir();
            }
        });

        btnEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configurarEnderecoAbrir();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarLogout();
            }
        });

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        return root;
    }

    public void configurarPerfilAbrir(){
        startActivity(new Intent(getActivity(), ConfigPerfilActivity.class));
    }

    public void configurarFiltrosPesquisaAbrir(){
        startActivity(new Intent(getActivity(), ConfigFiltrosPesquisaActivity.class));
    }

    public void configurarEnderecoAbrir(){
        startActivity(new Intent(getActivity(), ConfigPreferenciasActivity.class));
    }

    public void logout(){
        autenticacao.signOut();

        getActivity().finish();
    }

    public void confirmarLogout(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        //Configurar AlertDialog
        alertDialog.setTitle("Sair do sistema");
        alertDialog.setMessage("Você tem certeza que deseja sair da sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logout();

                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        alertDialog.setNegativeButton("Ficar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}