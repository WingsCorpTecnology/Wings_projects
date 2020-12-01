package com.cursoandroid.easychool_v4.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.model.NivelEducacao;
import com.cursoandroid.easychool_v4.model.TipoEscola;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigFiltrosPesquisaDistanciaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerDistancia;
    private ImageView preview;
    private Button salvar;
    private CheckBox cbEscolaMunicipal, cbEscolaEstadual, cbEscolaFederal;
    private List<String> filtros = new ArrayList<>();
    private List<Boolean> filtrosBool = new ArrayList<>();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference filtrosRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);
    private String distancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_filtros_pesquisa_distancia);

        spinnerDistancia = findViewById(R.id.spinner_distancias);
        preview = findViewById(R.id.img_preview);
        salvar = findViewById(R.id.btn_salvar_filtros);
        cbEscolaEstadual = findViewById(R.id.cb_escola_esta);
        cbEscolaFederal = findViewById(R.id.cb_escola_fed);
        cbEscolaMunicipal = findViewById(R.id.cb_escola_muni);

        filtrosRef = firebaseRef.child("FiltrosPesquisa").child(idResponsavel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.distancias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistancia.setAdapter(adapter);
        spinnerDistancia.setOnItemSelectedListener(this);

        preencherCombo();

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConfigFiltrosPesquisaAnosActivity.class));
                finish();
            }
        });

        spinnerDistancia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                distancia = spinnerDistancia.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //distancia = spinnerDistancia.getSelectedItem().toString();

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vericarCombo();

                if(filtros.size() != 0){
                    salvarFiltros();
                }
                else{
                    finish();
                }
            }
        });

        getSupportActionBar().hide();
    }

    private void salvarFiltros() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertDialog
        alertDialog.setTitle("Confirmar filtros");
        alertDialog.setMessage("Você quer confirmar o salvamento destes filtros na sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int position = 0; position < filtros.size(); position++) {
                    filtrosRef.child(filtros.get(position)).setValue(filtrosBool.get(position));
                }
                filtrosRef.child("Distancia").setValue(distancia);

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

    private void preencherCombo() {
        final TipoEscola tipo = new TipoEscola();
        final String[] combo = new String[1];

        filtrosRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int position = 0; position < tipo.getTipos().size(); position++) {
                    if(Objects.equals(dataSnapshot.child(tipo.getTipos().get(position)).getValue(), true)){
                        combo[0] = dataSnapshot.child(tipo.getTipos().get(position)).getKey();

                        if(cbEscolaEstadual.getText().equals(combo[0])){
                            cbEscolaEstadual.setChecked(true);
                        }
                        if(cbEscolaFederal.getText().equals(combo[0])){
                            cbEscolaFederal.setChecked(true);
                        }
                        if(cbEscolaMunicipal.getText().equals(combo[0])){
                            cbEscolaMunicipal.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void vericarCombo() {
        filtros.add("Escolas Municipais");
        filtrosBool.add(cbEscolaMunicipal.isChecked());

        filtros.add("Escolas Estaduais");
        filtrosBool.add(cbEscolaEstadual.isChecked());

        filtros.add("Escolas Federais");
        filtrosBool.add(cbEscolaFederal.isChecked());

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}