package com.cursoandroid.easychool_v4.activity;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.helper.CalcularDistancia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ConfigPreferenciasActivity extends AppCompatActivity {
    private Button btnSalvar;
    private EditText edtCep;
    private TextView txtCep;
    private SharedPreferences preferences;
    private final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_preferencias);

        btnSalvar = findViewById(R.id.btnSalvar);
        edtCep = findViewById(R.id.edtCep);
        txtCep = findViewById(R.id.txtCEP);

        preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        usuarioRef = firebaseRef.child("ResponsavelAluno").child(idResponsavel);

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

                    geocoding();
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

    public void geocoding(){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> listaEndereco = geocoder.getFromLocationName("Rua Bertolínia, 272 - Jardim Soares, São Paulo - SP", 1);
            //List<Address> listaEndereco = geocoder.getFromLocation(-23.5495244, -46.40220360000001, 1);
            List<Address> listaEnderecoTeste = geocoder.getFromLocation(-23.5926719, -46.6647561, 1);

            if(listaEndereco != null && listaEndereco.size() > 0){
                Address endereco = listaEndereco.get(0);
                Address endereco2 = listaEnderecoTeste.get(0);

                Log.d("Local", " "+endereco.toString());
                Log.d("Local", " "+endereco.getLatitude());
                Log.d("Local", " "+endereco.getLongitude());

                Double distancia = CalcularDistancia.CalcularDistancia(endereco.getLatitude(), endereco.getLongitude(), endereco2.getLatitude(), endereco2.getLongitude());
                Log.d("Local", " "+distancia);

                usuarioRef.child("latitude").setValue(endereco.getLatitude());
                usuarioRef.child("longitude").setValue(endereco.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}