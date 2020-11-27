package com.cursoandroid.easychool_v4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cursoandroid.easychool_v4.R;

public class ConfigFiltrosPesquisaAnosActivity extends AppCompatActivity {
    private ImageView next, preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_filtros_pesquisa_anos);

        getSupportActionBar().hide();

        next = findViewById(R.id.img_next);
        preview = findViewById(R.id.img_preview);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConfigFiltrosPesquisaDistanciaActivity.class));
                finish();
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConfigFiltrosPesquisaActivity.class));
                finish();
            }
        });
    }
}