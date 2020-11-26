package com.cursoandroid.easychool_v4.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.model.NivelEducacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigFiltrosPesquisaActivity extends AppCompatActivity {
    private CheckBox cbEdInfant, cbEnsFund1, cbEnsFund2, cbEnsMedio, cbEdEspe, cbEja;
    private Button btnSalvar;
    private ImageView next;
    private List<String> filtros = new ArrayList<>();
    private List<Boolean> filtrosBool = new ArrayList<>();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference filtrosRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_filtros_pesquisa);

        getSupportActionBar().hide();

        cbEdInfant = findViewById(R.id.cb_ed_infant);
        cbEnsFund1 = findViewById(R.id.cb_fund_1);
        cbEnsFund2 = findViewById(R.id.cb_fund_2);
        cbEnsMedio = findViewById(R.id.cb_ens_med);
        cbEdEspe = findViewById(R.id.cb_ed_espe);
        cbEja = findViewById(R.id.cb_eja);

        btnSalvar = findViewById(R.id.btn_salvar_filtros);

        next = findViewById(R.id.img_next);

        filtrosRef = firebaseRef.child("FiltrosPesquisa").child(idResponsavel);

        preencherCombo();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConfigFiltrosPesquisaAnosActivity.class));
                finish();
            }
        });
    }

    public void clickBotao(View view){
        verificarCombo();

        if(filtros.size() != 0){
            salvarFiltros();
        }
        else{
            finish();
        }
    }

    public void preencherCombo(){
        final NivelEducacao nivel = new NivelEducacao();
        final String[] combo = new String[1];

        filtrosRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int position = 0; position < nivel.getNiveis().size(); position++) {
                    if(Objects.equals(dataSnapshot.child(nivel.getNiveis().get(position)).getValue(), true)){
                        combo[0] = dataSnapshot.child(nivel.getNiveis().get(position)).getKey();

                        if(cbEdInfant.getText().equals(combo[0])){
                            cbEdInfant.setChecked(true);
                        }
                        if(cbEnsFund1.getText().equals(combo[0])){
                            cbEnsFund1.setChecked(true);
                        }
                        if(cbEnsFund2.getText().equals(combo[0])){
                            cbEnsFund2.setChecked(true);
                        }
                        if(cbEnsMedio.getText().equals(combo[0])){
                            cbEnsMedio.setChecked(true);
                        }
                        if(cbEdEspe.getText().equals(combo[0])){
                            cbEdEspe.setChecked(true);
                        }
                        if(cbEja.getText().equals(combo[0])){
                            cbEja.setChecked(true);
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
        filtros.add("Educação Infantil");
        filtrosBool.add(cbEdInfant.isChecked());

        filtros.add("Ensino Fundamental I");
        filtrosBool.add(cbEnsFund1.isChecked());

        filtros.add("Ensino Fundamental II");
        filtrosBool.add(cbEnsFund2.isChecked());

        filtros.add("Ensino Médio");
        filtrosBool.add(cbEnsMedio.isChecked());

        filtros.add("Educação Especial");
        filtrosBool.add(cbEdEspe.isChecked());

        filtros.add("EJA");
        filtrosBool.add(cbEja.isChecked());
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