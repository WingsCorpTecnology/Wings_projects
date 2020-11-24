package com.cursoandroid.easychool_v4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cursoandroid.easychool_v4.R;

public class ConfigFiltrosPesquisaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_filtros_pesquisa);

        getSupportActionBar().hide();
    }
}