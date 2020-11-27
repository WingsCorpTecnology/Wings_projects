package com.cursoandroid.easychool_v4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.cursoandroid.easychool_v4.R;

public class ConfigFiltrosPesquisaDistanciaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinnerDistancia;
    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_filtros_pesquisa_distancia);

        spinnerDistancia = findViewById(R.id.spinner_distancias);
        preview = findViewById(R.id.img_preview);

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
        
        getSupportActionBar().hide();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}