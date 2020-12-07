package com.cursoandroid.easychool_v4.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.adapter.AdapterTelefones;
import com.cursoandroid.easychool_v4.adapter.AdapterTurmas;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.config.RecyclerItemClickListener;
import com.cursoandroid.easychool_v4.model.Escola;
import com.cursoandroid.easychool_v4.model.Turma;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PerfilEscolaActivity extends AppCompatActivity {
    private AdapterTelefones adapter;
    private AdapterTurmas adapterTurmas;
    private RecyclerView recyclerView;
    private RecyclerView recyclerTurmas;
    private List<Long> listaTelefone = new ArrayList<Long>();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference telefoneRef;
    private ValueEventListener valueEventListenerTelefone;
    private DatabaseReference turmaRef;
    private String idEscola;
    private Escola escolaAtual;
    private TextView txtNome, txtEndereco, txtEstadoCidade, txtEmail;
    private Long telefoneSelecionado;
    private List<Turma> listaTurmas = new ArrayList<>();

    @SuppressLint("WrongViewCast")
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
        recyclerTurmas = findViewById(R.id.recyclerTurmas);

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

        //Evento de click
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //recuperando telefone selecionado
                telefoneSelecionado = listaTelefone.get(position);

                //Log.i("teste", "telefone: " +telefoneSelecionado);
                Uri uri = Uri.parse("tel:"+telefoneSelecionado);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

        recuperarTurmas();

        //Configurar Adapter
        adapterTurmas = new AdapterTurmas(listaTurmas);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManagerTurmas = new LinearLayoutManager(this);
        recyclerTurmas.setLayoutManager(layoutManagerTurmas);
        recyclerTurmas.setHasFixedSize(true);
        recyclerTurmas.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerTurmas.setAdapter(adapterTurmas);
    }

    @Override
    public void onBackPressed() {
        finish();
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
                    Long telefone = dados.getValue(Long.class);

                    //telefone.setId(dados.getKey());

                    listaTelefone.add(telefone);

                    //Log.i("teste", "telefone: "+telefone);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recuperarTurmas(){
        turmaRef = firebaseRef.child("Turmas").child(Base64Custom.codificarBase64(txtEmail.getText().toString()));
        Log.i("teste", Base64Custom.codificarBase64(txtEmail.getText().toString()));

        turmaRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaTurmas.clear();
                //Log.i("teste", "Série: ");

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Turma turma = dados.getValue(Turma.class);

                    turma.setId(dados.getKey());
                    turma.setVagasTotal(Integer.parseInt((String) Objects.requireNonNull(dados.child("total_vagas").getValue())));
                    turma.setVagasOcupadas(Integer.parseInt((String) Objects.requireNonNull(dados.child("vagas_ocupadas").getValue())));
                    turma.setUltimaAlteracao((String) Objects.requireNonNull(dados.child("data_hora").getValue()));
                    //turma.set

                    //Log.i("teste", "Vagas: " +turma.getUltimaAlteracao());
                    listaTurmas.add(turma);
                }

                adapterTurmas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}