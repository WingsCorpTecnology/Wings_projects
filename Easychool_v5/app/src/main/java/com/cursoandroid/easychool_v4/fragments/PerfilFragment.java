package com.cursoandroid.easychool_v4.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.activity.ConfigFiltrosPesquisaActivity;
import com.cursoandroid.easychool_v4.activity.ConfigPerfilActivity;
import com.cursoandroid.easychool_v4.adapter.Adapter;
import com.cursoandroid.easychool_v4.adapter.AdapterFiltros;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.helper.Geocoding;
import com.cursoandroid.easychool_v4.helper.ResponsavelFirebase;
import com.cursoandroid.easychool_v4.model.AnosEscolares;
import com.cursoandroid.easychool_v4.model.Escola;
import com.cursoandroid.easychool_v4.model.NivelEducacao;
import com.cursoandroid.easychool_v4.model.ResponsavelAluno;
import com.cursoandroid.easychool_v4.model.TipoEscola;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PerfilFragment extends Fragment {
    private TextView edtNome, edtTelefone, edtCpf, edtRg, edtEmail, edtEndereco;
    private Button btnUpdate, btnAddFiltros;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private DatabaseReference filtrosRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);
    private ResponsavelAluno responsavel;
    private Geocoding geocoding;
    private String espacamento = "  ";
    private AdapterFiltros adapter;
    private List<String> filtros = new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageView imgFoto;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);

        edtNome = root.findViewById(R.id.nomeUser);
        edtTelefone = root.findViewById(R.id.telefoneUser);
        edtCpf = root.findViewById(R.id.cpfUser);
        edtRg = root.findViewById(R.id.rgUser);
        edtEmail = root.findViewById(R.id.emailUser);
        edtEndereco = root.findViewById(R.id.enderecoUser);
        btnUpdate = root.findViewById(R.id.btnAlterar);
        btnAddFiltros = root.findViewById(R.id.btnAddFiltros);
        recyclerView = root.findViewById(R.id.recyclerView);
        imgFoto = root.findViewById(R.id.imgPerfil);

        usuarioRef = firebaseRef.child("ResponsavelAluno").child(idResponsavel);
        filtrosRef = firebaseRef.child("FiltrosPesquisa").child(idResponsavel);

        responsavel = new ResponsavelAluno();

        recuperarDadosUser();
        recuperarFiltrosUser();

        //Configurar Adapter
        adapter = new AdapterFiltros(filtros);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ConfigPerfilActivity.class));
            }
        });

        btnAddFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ConfigFiltrosPesquisaActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperarFiltrosUser();
    }

    public void recuperarDadosUser(){
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                responsavel.setNome((String) dataSnapshot.child("nome").getValue());
                responsavel.setTelefone((String) dataSnapshot.child("telefone").getValue());
                responsavel.setCpf((String) dataSnapshot.child("cpf").getValue());
                responsavel.setRg((String) dataSnapshot.child("rg").getValue());

                Double latitude = (Double) dataSnapshot.child("latitude").getValue();
                Double longitude = (Double) dataSnapshot.child("longitude").getValue();

                if(latitude == null || longitude == null){
                    edtEndereco.setText("Nenhum endereço cadastrado");
                }
                else{
                    geocoding = new Geocoding(getActivity(), latitude, longitude);

                    edtEndereco.setText(espacamento + geocoding.getEndereco());
                }

                edtNome.setText(espacamento + responsavel.getNome());
                edtTelefone.setText(espacamento + responsavel.getTelefone());
                edtCpf.setText(espacamento + responsavel.getCpf());
                edtRg.setText(espacamento + responsavel.getRg());
                edtEmail.setText(espacamento + emailResponsavel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Recuperando dados do Usuário
        FirebaseUser usuario = ResponsavelFirebase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();

        if(url != null){
            Glide.with(getActivity()).load(url).into(imgFoto);
        }
        else{
            imgFoto.setImageResource(R.drawable.perfil);
        }
    }

    public void recuperarFiltrosUser(){

        filtrosRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                filtros.clear();

                NivelEducacao nivel = new NivelEducacao();
                
                for(int i = 0; i < nivel.getNiveis().size(); i++) {
                    if (Objects.equals(dataSnapshot.child(nivel.getNiveis().get(i)).getValue(), true)) {
                        filtros.add(dataSnapshot.child(nivel.getNiveis().get(i)).getKey());
                    }
                }

                AnosEscolares anos = new AnosEscolares();

                for(int i = 0; i < anos.getAnos().size(); i++){
                    if(Objects.equals(dataSnapshot.child(anos.getAnos().get(i)).getValue(), true)){
                        filtros.add(dataSnapshot.child(anos.getAnos().get(i)).getKey());
                    }
                }

                TipoEscola tipo = new TipoEscola();

                for(int i = 0; i < tipo.getTipos().size(); i++){
                    if(Objects.equals(dataSnapshot.child(tipo.getTipos().get(i)).getValue(), true)){
                        filtros.add(dataSnapshot.child(tipo.getTipos().get(i)).getKey());
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}