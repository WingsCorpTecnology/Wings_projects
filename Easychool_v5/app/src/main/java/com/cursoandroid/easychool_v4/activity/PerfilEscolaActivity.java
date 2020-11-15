package com.cursoandroid.easychool_v4.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

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
import com.cursoandroid.easychool_v4.model.TelefoneEscola;
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
    private List<TelefoneEscola> listaTelefone = new ArrayList<>();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference telefoneRef;
    private String emailEscola = autenticacao.getCurrentUser().getEmail();
    private String idEscola = Base64Custom.codificarBase64(emailEscola);
    private ValueEventListener valueEventListenerTelefone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_escola);

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
    }

    public void recuperarTelefones(){
        telefoneRef = firebaseRef.child("TelefoneEscola");

        valueEventListenerTelefone = telefoneRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaTelefone.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    TelefoneEscola telefone = dados.getValue(TelefoneEscola.class);

                    listaTelefone.add(telefone);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}