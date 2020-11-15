package com.cursoandroid.easychool_v4.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.adapter.AdapterTelefones;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.model.Escola;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PerfilEscolaActivity extends AppCompatActivity {
    private AdapterTelefones adapter;
    private RecyclerView recyclerView;
    private List<String> listaTelefone = new ArrayList<String>();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference telefoneRef;
    private DatabaseReference escolaRef;
    private String emailEscola = autenticacao.getCurrentUser().getEmail();
    private String idEscola = Base64Custom.codificarBase64(emailEscola);
    private ValueEventListener valueEventListenerTelefone;
    private Escola escolaAtual;
    private TextView txtNome, txtEndereco, txtEstadoCidade, txtEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_escola);

        escolaAtual = (Escola) getIntent().getSerializableExtra("escolaSelecionada");

        txtNome = findViewById(R.id.txt_nome_escola);
        txtEndereco = findViewById(R.id.txt_rua_escola);
        txtEstadoCidade = findViewById(R.id.txt_cidade_estado_escola);
        txtEmail = findViewById(R.id.txt_email_escola);

        //Configurar escola (mostrar os dados)
        preencherCampos();

        recyclerView = findViewById(R.id.recyclerTelefone);

        recuperarTelefones();

        //Configurar Adapter
        adapter = new AdapterTelefones(listaTelefone);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        //escolaRef = firebaseRef.child("Escola").child();
    }

    private void preencherCampos() {
        txtNome.setText(escolaAtual.getNome());
        txtEndereco.setText(escolaAtual.getRua()+ ", " +escolaAtual.getNumero()+ " - " +escolaAtual.getBairro());
        txtEstadoCidade.setText(escolaAtual.getCidade()+ " - " +escolaAtual.getUf());
        txtEmail.setText(Base64Custom.decodificarBase64(escolaAtual.getId()));

        Log.i("Email", txtEmail.getText().toString());
    }

    public void recuperarTelefones(){
        telefoneRef = firebaseRef.child("TelefoneEscola").child(Base64Custom.codificarBase64(txtEmail.getText().toString()));

        valueEventListenerTelefone = telefoneRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaTelefone.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    String telefone = dados.getValue(String.class);

                    //telefone.setId(dados.getKey());

                    listaTelefone.add(telefone);

                    Log.i("telefones", telefone);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}