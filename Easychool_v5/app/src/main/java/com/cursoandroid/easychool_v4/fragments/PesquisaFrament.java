package com.cursoandroid.easychool_v4.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.activity.PerfilEscolaActivity;
import com.cursoandroid.easychool_v4.adapter.Adapter;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.config.RecyclerItemClickListener;
import com.cursoandroid.easychool_v4.helper.Base64Custom;
import com.cursoandroid.easychool_v4.model.Escola;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PesquisaFrament extends Fragment {
    private Adapter adapter;
    private RecyclerView recyclerView;
    private List<Escola> listaEscolas = new ArrayList<>();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference escolaRef;
    private String emailEscola = autenticacao.getCurrentUser().getEmail();
    private String idEscola = Base64Custom.codificarBase64(emailEscola);
    private ValueEventListener valueEventListenerEscola;
    private Escola escolaSelecionada;
    //private ValueEventListener valueEventListenerMovimentacao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        recyclerView = root.findViewById(R.id.recyclerEscolas);

        recuperarEscolas();

        //Configurar Adapter
        adapter = new Adapter(listaEscolas);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        //Evento de Click
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                    @Override
                    public void onItemClick(View view, int position) {
                        //Recuperando escola selecionada
                        escolaSelecionada = listaEscolas.get(position);

                        //Envia a escola para a tela de perfil da escola
                        Intent intent = new Intent(getActivity(), PerfilEscolaActivity.class);
                        intent.putExtra("escolaSelecionada", escolaSelecionada);

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recuperarEscolas();
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarEscolas();
    }

    public void recuperarEscolas(){
        escolaRef = firebaseRef.child("Escola");

        valueEventListenerEscola = escolaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaEscolas.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Escola escola = dados.getValue(Escola.class);

                    escola.setId(dados.getKey());

                    listaEscolas.add(escola);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}