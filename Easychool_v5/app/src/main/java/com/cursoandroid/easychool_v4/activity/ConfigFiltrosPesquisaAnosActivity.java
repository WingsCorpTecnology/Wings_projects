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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.model.AnosEscolares;
import com.cursoandroid.easychool_v4.model.NivelEducacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigFiltrosPesquisaAnosActivity extends AppCompatActivity {
    private ImageView next, preview;
    private CheckBox cbBercario, cbBercario2, cbMaternal, cbMaternal2, cbPre, cbPre2, cb1Ano, cb2Ano, cb3Ano, cb4Ano, cb5Ano, cb6Ano, cb7Ano, cb8Ano, cb9Ano, cb1Medio, cb2Medio, cb3Medio;
    private List<String> filtros = new ArrayList<>();
    private List<Boolean> filtrosBool = new ArrayList<>();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference filtrosRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);
    private Button salvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_filtros_pesquisa_anos);

        getSupportActionBar().hide();

        next = findViewById(R.id.img_next);
        preview = findViewById(R.id.img_preview);

        cbBercario = findViewById(R.id.cb_ber_1);
        cbBercario2 = findViewById(R.id.cb_ber_2);
        cbMaternal = findViewById(R.id.cb_mat_1);
        cbMaternal2 = findViewById(R.id.cb_mat_2);
        cbPre = findViewById(R.id.cb_pre_1);
        cbPre2 = findViewById(R.id.cb_pre_2);
        cb1Ano = findViewById(R.id.cb_ano_1);
        cb2Ano = findViewById(R.id.cb_ano_2);
        cb3Ano = findViewById(R.id.cb_ano_3);
        cb4Ano = findViewById(R.id.cb_ano_4);
        cb5Ano = findViewById(R.id.cb_ano_5);
        cb6Ano = findViewById(R.id.cb_ano_6);
        cb7Ano = findViewById(R.id.cb_ano_7);
        cb8Ano = findViewById(R.id.cb_ano_8);
        cb9Ano = findViewById(R.id.cb_ano_9);
        cb1Medio = findViewById(R.id.cb_medio_1);
        cb2Medio = findViewById(R.id.cb_medio_2);
        cb3Medio = findViewById(R.id.cb_medio_3);
        salvar = findViewById(R.id.btn_salvar_filtros);

        filtrosRef = firebaseRef.child("FiltrosPesquisa").child(idResponsavel);

        preencherCombo();

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

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarCombo();

                if(filtros.size() != 0){
                    salvarFiltros();
                }
                else{
                    finish();
                }
            }
        });
    }

    public void preencherCombo() {
        final AnosEscolares anos = new AnosEscolares();
        final String[] combo = new String[1];

        filtrosRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int position = 0; position < anos.getAnos().size(); position++) {
                    if (Objects.equals(dataSnapshot.child(anos.getAnos().get(position)).getValue(), true)) {
                        combo[0] = dataSnapshot.child(anos.getAnos().get(position)).getKey();

                        if (cbBercario.getText().equals(combo[0])) {
                            cbBercario.setChecked(true);
                        }
                        if (cbBercario2.getText().equals(combo[0])) {
                            cbBercario2.setChecked(true);
                        }
                        if (cbMaternal.getText().equals(combo[0])) {
                            cbMaternal.setChecked(true);
                        }
                        if (cbMaternal2.getText().equals(combo[0])) {
                            cbMaternal2.setChecked(true);
                        }
                        if (cbPre.getText().equals(combo[0])) {
                            cbPre.setChecked(true);
                        }
                        if (cbPre2.getText().equals(combo[0])) {
                            cbPre2.setChecked(true);
                        }
                        if (cb1Ano.getText().equals(combo[0])) {
                            cb1Ano.setChecked(true);
                        }
                        if (cb2Ano.getText().equals(combo[0])) {
                            cb2Ano.setChecked(true);
                        }
                        if (cb3Ano.getText().equals(combo[0])) {
                            cb3Ano.setChecked(true);
                        }
                        if (cb4Ano.getText().equals(combo[0])) {
                            cb4Ano.setChecked(true);
                        }
                        if (cb5Ano.getText().equals(combo[0])) {
                            cb5Ano.setChecked(true);
                        }
                        if (cb6Ano.getText().equals(combo[0])) {
                            cb6Ano.setChecked(true);
                        }
                        if (cb7Ano.getText().equals(combo[0])) {
                            cb7Ano.setChecked(true);
                        }
                        if (cb8Ano.getText().equals(combo[0])) {
                            cb8Ano.setChecked(true);
                        }
                        if (cb9Ano.getText().equals(combo[0])) {
                            cb9Ano.setChecked(true);
                        }
                        if (cb1Medio.getText().equals(combo[0])) {
                            cb1Medio.setChecked(true);
                        }
                        if (cb2Medio.getText().equals(combo[0])) {
                            cb2Medio.setChecked(true);
                        }
                        if (cb3Medio.getText().equals(combo[0])) {
                            cb3Medio.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void verificarCombo(){
        filtros.add("Berçário 1");
        filtrosBool.add(cbBercario.isChecked());

        filtros.add("Berçário 2");
        filtrosBool.add(cbBercario2.isChecked());

        filtros.add("Maternal 1");
        filtrosBool.add(cbMaternal.isChecked());

        filtros.add("Maternal 2");
        filtrosBool.add(cbMaternal.isChecked());

        filtros.add("Pré 1");
        filtrosBool.add(cbPre.isChecked());

        filtros.add("Pré 2");
        filtrosBool.add(cbPre2.isChecked());

        filtros.add("1° ano");
        filtrosBool.add(cb1Ano.isChecked());

        filtros.add("2° ano");
        filtrosBool.add(cb2Ano.isChecked());

        filtros.add("3° ano");
        filtrosBool.add(cb3Ano.isChecked());

        filtros.add("4° ano");
        filtrosBool.add(cb4Ano.isChecked());

        filtros.add("5° ano");
        filtrosBool.add(cb5Ano.isChecked());

        filtros.add("6° ano");
        filtrosBool.add(cb6Ano.isChecked());

        filtros.add("7° ano");
        filtrosBool.add(cb7Ano.isChecked());

        filtros.add("8° ano");
        filtrosBool.add(cb8Ano.isChecked());

        filtros.add("9° ano");
        filtrosBool.add(cb9Ano.isChecked());

        filtros.add("1° Médio");
        filtrosBool.add(cb1Medio.isChecked());

        filtros.add("2° Médio");
        filtrosBool.add(cb2Medio.isChecked());

        filtros.add("3° Médio");
        filtrosBool.add(cb3Medio.isChecked());
    }

    public void salvarFiltros(){
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

                    finish();
                }
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
}