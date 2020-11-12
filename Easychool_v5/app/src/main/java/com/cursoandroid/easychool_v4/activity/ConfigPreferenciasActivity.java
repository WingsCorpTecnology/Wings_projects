package com.cursoandroid.easychool_v4.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.easychool_v4.R;

public class ConfigPreferenciasActivity extends AppCompatActivity {
    private Button btnSalvar;
    private EditText edtCep;
    private TextView txtCep;
    private SharedPreferences preferences;
    private final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_preferencias);

        btnSalvar = findViewById(R.id.btnSalvar);
        edtCep = findViewById(R.id.edtCep);
        txtCep = findViewById(R.id.txtCEP);

        preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();

                //Validar o cep
                if(edtCep.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Preencha o CEP", Toast.LENGTH_SHORT).show();
                }
                else{
                    String cep = edtCep.getText().toString();
                    editor.putString("cep", cep);
                    editor.commit();

                    txtCep.setText("CEP: " +cep);
                }
            }
        });

        //Valida se temos o nome em preferencias
        if(preferences.contains("cep")){
            String cep = preferences.getString("cep", "usuário não definido");
            txtCep.setText("CEP: " +cep);
        }
        else{
            txtCep.setText("Nenhum CEP salvo");
        }

        getSupportActionBar().hide();
    }
}