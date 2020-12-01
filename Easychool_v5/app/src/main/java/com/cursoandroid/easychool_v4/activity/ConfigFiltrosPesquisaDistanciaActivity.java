package com.cursoandroid.easychool_v4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.cursoandroid.easychool_v4.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ConfigFiltrosPesquisaDistanciaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerDistancia;
    private ImageView preview;
    private List<String> filtrosNiveis = new ArrayList<>();
    private List<Boolean> filtrosNiveisBool = new ArrayList<>();
    private Button salvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_filtros_pesquisa_distancia);

        spinnerDistancia = findViewById(R.id.spinner_distancias);
        preview = findViewById(R.id.img_preview);
        salvar = findViewById(R.id.btn_salvar_filtros);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.distancias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistancia.setAdapter(adapter);
        spinnerDistancia.setOnItemSelectedListener(this);

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConfigFiltrosPesquisaAnosActivity.class));
                finish();
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperarFiltros();
            }
        });

        getSupportActionBar().hide();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void recuperarFiltros(){
        for(int i = 0; i < 6; i++) {
            filtrosNiveis.add((String) getIntent().getSerializableExtra("listaFiltros" +i));

            filtrosNiveisBool.add((Boolean) getIntent().getSerializableExtra("listaFiltrosBool" +i));
        }

        for(int i = 0; i < filtrosNiveis.size(); i++) {
            Log.d("teste", filtrosNiveis.get(i));
            Log.d("teste", String.valueOf(filtrosNiveisBool.get(i)));
        }
    }
}