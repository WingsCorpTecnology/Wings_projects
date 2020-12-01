package com.cursoandroid.easychool_v4.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.helper.Geocoding;
import com.cursoandroid.easychool_v4.model.ResponsavelAluno;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
    private ResponsavelAluno responsavel;

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
        responsavel = new ResponsavelAluno();

        recuperarEndereco();

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

    @Override
    public void onBackPressed() {
        //alertCancelar();
        if(verificarCampos()){
            alertCancelar();
        }
        else{
            super.onBackPressed();
        }
    }

    public void recuperarEndereco(){
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cep;
                String enderecoCompleto;

                Double latitude = (Double) dataSnapshot.child("latitude").getValue();
                Double longitude = (Double) dataSnapshot.child("longitude").getValue();

                if(latitude != null || longitude != null) {
                    geocoding = new Geocoding(getApplicationContext(), latitude, longitude);
                    enderecoCompleto = geocoding.getEndereco();
                    cep = geocoding.getCep();

                    String[] rua = enderecoCompleto.split(",");

                    edtRua.setText(rua[0]);

                    String[] numero = rua[1].split("-");

                    edtNumero.setText(numero[0].trim());
                    edtBairro.setText(numero[1].trim());

                    String[] outroEnd = enderecoCompleto.split(rua[1]);
                    String[] outroEnd2 = outroEnd[1].split(",");
                    String[] cidade = outroEnd2[1].split("-");

                    edtCidade.setText(cidade[0].trim());
                    edtEstado.setText(cidade[1].trim());

                    edtCep.setText(cep);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean verificarCampos(){
        if(!edtCep.getText().toString().isEmpty()){
            return true;
        }
        if(!edtEstado.getText().toString().isEmpty()){
            return true;
        }
        if(!edtCidade.getText().toString().isEmpty()){
            return true;
        }
        if(!edtBairro.getText().toString().isEmpty()){
            return true;
        }
        if(!edtRua.getText().toString().isEmpty()){
            return true;
        }
        if(!edtNumero.getText().toString().isEmpty()){
            return true;
        }

        return false;
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

    public void alertCancelar(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertDialog
        alertDialog.setTitle("Voltar sem salvar");
        alertDialog.setMessage("Você deseja realmente voltar? As informações digitadas poderão ser perdidas");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}