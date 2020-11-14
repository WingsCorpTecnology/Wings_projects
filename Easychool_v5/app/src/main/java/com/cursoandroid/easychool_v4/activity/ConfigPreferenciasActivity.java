package com.cursoandroid.easychool_v4.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ConfigPreferenciasActivity extends AppCompatActivity {
    private Button btnSalvar;
    private EditText edtCep, edtRua, edtNumero, edtEstado, edtCidade, edtBairro;
    private SharedPreferences preferences;
    private final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);
    private String enderecoCompleto, estado, rua, numero, cidade, bairro;
    private Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_preferencias);

        btnSalvar = findViewById(R.id.btnSalvar);
        edtCep = findViewById(R.id.edtCep);
        edtRua = findViewById(R.id.edtRua);
        edtNumero = findViewById(R.id.edtNumero);
        edtEstado = findViewById(R.id.edtEstado);
        edtCidade = findViewById(R.id.edtCidade);
        edtBairro = findViewById(R.id.edtBairro);

        preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        usuarioRef = firebaseRef.child("ResponsavelAluno").child(idResponsavel);

        edtEstado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                estado = edtEstado.getText().toString();
            }
        });

        edtCidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                cidade = edtCidade.getText().toString();
            }
        });

        edtBairro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                bairro = edtBairro.getText().toString();
            }
        });

        edtRua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                rua = edtRua.getText().toString();
            }
        });

        edtNumero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                numero = edtNumero.getText().toString();

                geocoding();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geocoding();

                confirmarEndereco();
            }
        });

        getSupportActionBar().hide();
    }

    /*public void verificarCampos(){
        if(!edtEstado.getText().toString().isEmpty()){

        }
        if(!edtCidade.getText().toString().isEmpty()){

        }
        if(!edtBairro.getText().toString().isEmpty()){

        }
        if(!edtRua.getText().toString().isEmpty()){

        }
        if(!edtNumero.getText().toString().isEmpty()){

        }
    }*/

    public void geocoding(){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        enderecoCompleto = rua+ ", " +numero+ " - " +bairro+ ", " +cidade+ " - " +estado;

        try {
            //List<Address> listaEndereco = geocoder.getFromLocationName("Rua Bertolínia, 272 - Jardim Soares, São Paulo - SP", 1);
            List<Address> listaEndereco = geocoder.getFromLocationName(enderecoCompleto, 1);
            //List<Address> listaEndereco = geocoder.getFromLocation(-23.5495244, -46.40220360000001, 1);

            if(listaEndereco != null && listaEndereco.size() > 0){
                Address endereco = listaEndereco.get(0);
               // Address endereco2 = listaEnderecoTeste.get(0);

                Log.d("Local", " "+endereco.toString());
                Log.d("Local", " "+endereco.getLatitude());
                Log.d("Local", " "+endereco.getLongitude());

                //Setar os textos com base no cep
                edtCep.setText(endereco.getPostalCode());

                //Double distancia = CalcularDistancia.CalcularDistancia(endereco.getLatitude(), endereco.getLongitude(), endereco2.getLatitude(), endereco2.getLongitude());
                //Log.d("Local", " "+distancia);

                latitude = endereco.getLatitude();
                longitude = endereco.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void confirmarEndereco(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertDialog
        alertDialog.setTitle("Confirmar endereço");
        alertDialog.setMessage("Você quer confirmar o salvamento deste endereço na sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                usuarioRef.child("latitude").setValue(latitude);
                usuarioRef.child("longitude").setValue(longitude);

                mensagemAlteracoesSucesso();

                finish();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void mensagemAlteracoesSucesso(){
        Toast.makeText(this, "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
    }
}