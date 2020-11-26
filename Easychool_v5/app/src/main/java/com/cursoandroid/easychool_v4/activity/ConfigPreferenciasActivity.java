package com.cursoandroid.easychool_v4.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.helper.Geocoding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ConfigPreferenciasActivity extends AppCompatActivity {
    private Button btnSalvar;
    private EditText edtCep, edtRua, edtNumero, edtEstado, edtCidade, edtBairro;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);
    private String enderecoCompleto, estado, rua, numero, cidade, bairro;
    private Double latitude, longitude;
    private Geocoding geocoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_preferencias);

        btnSalvar = findViewById(R.id.btn_salvar_filtros);
        edtCep = findViewById(R.id.edtCep);
        edtRua = findViewById(R.id.edtRua);
        edtNumero = findViewById(R.id.edtNumero);
        edtEstado = findViewById(R.id.edtEstado);
        edtCidade = findViewById(R.id.edtCidade);
        edtBairro = findViewById(R.id.edtBairro);

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

                enderecoCompleto = rua+ ", " +numero+ " - " +bairro+ ", " +cidade+ " - " +estado;

                geocoding = new Geocoding(getApplicationContext(), enderecoCompleto);

                edtCep.setText(geocoding.getCep());
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude = geocoding.getLatitude();
                longitude = geocoding.getLongitude();

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