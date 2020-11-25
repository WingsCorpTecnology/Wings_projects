package com.cursoandroid.easychool_v4.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ConfigFiltrosPesquisaActivity extends AppCompatActivity {
    private CheckBox cbEdInfant, cbEnsFund1, cbEnsFund2, cbEnsMedio, cbEdEspe, cbEja;
    private Button btnSalvar;
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
        cbEnsFund1 = findViewById(R.id.cb_ens_fund_i);
        cbEnsFund2 = findViewById(R.id.cb_ens_fund_ii);
        cbEnsMedio = findViewById(R.id.cb_ens_medi);
        cbEdEspe = findViewById(R.id.cb_ed_esp);
        cbEja = findViewById(R.id.cb_eja);
        btnSalvar = findViewById(R.id.btnSalvar);

        filtrosRef = firebaseRef.child("FiltrosPesquisa").child(idResponsavel);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
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

    public void verificarCombo(){
        if(cbEdInfant.isChecked()){
            filtros.add("Educação Infantil");
            filtrosBool.add(cbEdInfant.isChecked());
        }
        if(cbEnsFund1.isChecked()){
            filtros.add("Ensino Fundamental I");
            filtrosBool.add(cbEnsFund1.isChecked());
        }
        if(cbEnsFund2.isChecked()){
            filtros.add("Ensino Fundamental II");
            filtrosBool.add(cbEnsFund2.isChecked());
        }
        if(cbEnsMedio.isChecked()){
            filtros.add("Ensino Médio");
            filtrosBool.add(cbEnsMedio.isChecked());
        }
        if(cbEdEspe.isChecked()){
            filtros.add("Educação Especial");
            filtrosBool.add(cbEdEspe.isChecked());
        }
        if(cbEja.isChecked()){
            filtros.add("EJA");
            filtrosBool.add(cbEja.isChecked());
        }
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

                    mensagemAlteracoesSucesso();

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

    public void mensagemAlteracoesSucesso(){
        Toast.makeText(this, "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
    }
}