package com.cursoandroid.easychool_v4.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.activity.PerfilEscolaActivity;
import com.cursoandroid.easychool_v4.adapter.Adapter;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.config.RecyclerItemClickListener;
import com.cursoandroid.easychool_v4.helper.Base64Custom;
import com.cursoandroid.easychool_v4.helper.CalcularDistancia;
import com.cursoandroid.easychool_v4.helper.Geocoding;
import com.cursoandroid.easychool_v4.model.Escola;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PesquisaFrament extends Fragment {
    private Adapter adapter;
    private RecyclerView recyclerView;
    private List<Escola> listaEscolas = new ArrayList<>();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference escolaRef;
    private DatabaseReference responsavelRef;
    private DatabaseReference turmaRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);
    private DatabaseReference filtrosRef;
    private Escola escolaSelecionada;
    private EditText edtPesquisa;
    private Geocoding geocoding;
    private Double latitude, longitude;
    private BigDecimal bd;
    private List<Double> distancia = new ArrayList<>();
    private List<Integer> filtroDistancia = new ArrayList<>();
    private List<ImageView> perfilEscola = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        recyclerView = root.findViewById(R.id.recyclerEscolas);
        edtPesquisa = root.findViewById(R.id.txt_pesquisa);

        escolaRef = firebaseRef.child("Escola");
        filtrosRef = firebaseRef.child("FiltrosPesquisa").child(idResponsavel);
        responsavelRef = firebaseRef.child("ResponsavelAluno").child(idResponsavel);
        //turmaRef = firebaseRef.child("Turmas").child()

        recuperarEscolas();
        pesquisa();
        recuperarDadosUser();

        //Configurar Adapter
        adapter = new Adapter(listaEscolas, distancia);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        //Evento de Click
        /*recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView,
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
                }));*/

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //recuperarEscolas();
    }

    @Override
    public void onStart() {
        super.onStart();
        //recuperarEscolas();
    }

    public void recuperarEscolas(){
        escolaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaEscolas.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Escola escola = dados.getValue(Escola.class);

                    escola.setId(dados.getKey());

                    listaEscolas.add(escola);
                }
                adapter.notifyDataSetChanged();

                escolasFiltros();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void escolasFiltros(){
        final List<Escola> listaEscolasFiltro = new ArrayList<>();

        try{
            for(Escola escola : listaEscolas){
                String enderecoEscola = "Rua: " +escola.getRua()+ ", " +String.valueOf(escola.getNumero())+ ", " +escola.getBairro()+ ", " +escola.getCidade()+ ", " +escola.getUf().toLowerCase();

                geocoding = new Geocoding(getActivity(), enderecoEscola);

                escola.setLatitude(geocoding.getLatitude());
                escola.setLongitude(geocoding.getLongitude());

                Double km = CalcularDistancia.CalcularDistancia(latitude, longitude, escola.getLatitude(), escola.getLongitude());

                bd = new BigDecimal(km).setScale(2, RoundingMode.HALF_EVEN);

                escola.setDistancia(bd.doubleValue());

                listaEscolasFiltro.add(escola);
                distancia.add(escola.getDistancia());
            }
            Collections.sort(listaEscolasFiltro);
            Collections.sort(distancia);

            adapter = new Adapter(listaEscolasFiltro, distancia);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            //Evento de Click
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        }

                        @Override
                        public void onItemClick(View view, int position) {
                            //Recuperando escola selecionada
                            escolaSelecionada = listaEscolasFiltro.get(position);

                            //Envia a escola para a tela de perfil da escola
                            Intent intent = new Intent(getActivity(), PerfilEscolaActivity.class);
                            intent.putExtra("escolaSelecionada", escolaSelecionada);

                            startActivity(intent);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));

            filtroDistancia();
        } catch (Exception e) {

        }
    }

    public void pesquisa(){
        edtPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                List<Escola> listaEscolaBusca = new ArrayList<>();
                String pesquisa = charSequence.toString().toLowerCase();

                for(Escola escola : listaEscolas){
                    String nomeEscola = escola.getNome().toLowerCase();
                    String enderecoEscola = "Rua: " +escola.getRua()+ ", " +String.valueOf(escola.getNumero())+ ", " +escola.getBairro()+ ", " +escola.getCidade()+ ", " +escola.getUf().toLowerCase();

                    if(nomeEscola.contains(pesquisa) || enderecoEscola.contains(pesquisa)){
                        listaEscolaBusca.add(escola);
                    }
                }

                adapter = new Adapter(listaEscolaBusca, distancia);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void recuperarDadosUser(){
        responsavelRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    latitude = (Double) dataSnapshot.child("latitude").getValue();
                    longitude = (Double) dataSnapshot.child("longitude").getValue();
                } catch (Exception e) {
                    alertaSemEndereco();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void alertaSemEndereco(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        //Configurar AlertDialog
        alertDialog.setTitle("Endereço não cadastrado");
        alertDialog.setMessage("Você quer realizar a pesquisa sem o seu endereço?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                escolasFiltros();
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recuperarEscolas();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void filtroDistancia(){
        final List<Escola> escolasFiltroDistancia = new ArrayList<>();

        //Log.i("teste", "CHEGUEI");

        filtrosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String distanciaFire = (String) dataSnapshot.child("Distancia").getValue();

                if(distanciaFire != null) {
                    String[] distanciaQuebrada = distanciaFire.split(" ");

                    try {
                        filtroDistancia.add(Integer.parseInt(distanciaQuebrada[1]));

                        if(filtroDistancia.get(0) != 0) {
                            if(filtroDistancia.get(0) == 5){
                                for(Escola escola : listaEscolas){
                                    if(escola.getDistancia() <= 5.0){
                                        escolasFiltroDistancia.add(escola);
                                    }
                                }
                            }
                            else if(filtroDistancia.get(0) == 10){
                                for(Escola escola : listaEscolas){
                                    if(escola.getDistancia() <= 10.0){
                                        escolasFiltroDistancia.add(escola);
                                    }
                                }
                            }
                            else if(filtroDistancia.get(0) == 15){
                                for(Escola escola : listaEscolas){
                                    if(escola.getDistancia() <= 15.0){
                                        escolasFiltroDistancia.add(escola);
                                    }
                                }
                            }
                            //Log.i("teste", "filtro: " +filtroDistancia.get(0));
                        }
                        Collections.sort(escolasFiltroDistancia);
                        Collections.sort(distancia);

                        adapter = new Adapter(escolasFiltroDistancia, distancia);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        filtroDistancia.add(Integer.parseInt(distanciaQuebrada[0]));

                        if(filtroDistancia.get(0) != 0) {
                            //Log.i("teste", "filtro: " +filtroDistancia.get(0));

                            escolasFiltroDistancia.addAll(listaEscolas);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void filtroTipoEscola(final List<Escola> escolas){
        final List<Escola> escolasFiltroTipo = new ArrayList<>();

        filtrosRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> tipoFire = new ArrayList<>();

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(Objects.equals(data.child("Escolas Estaduais").getValue(), true)){
                        tipoFire.add("Escola Estadual");
                    }
                    if(Objects.equals(data.child("Escolas Federais").getValue(), true)){
                        tipoFire.add("Escola Federal");
                    }
                    if(Objects.equals(data.child("Escolas Municipais").getValue(), true)){
                        tipoFire.add("Escola Municipal");
                    }
                }

                for(int i = 0; i < escolas.size(); i++){
                    //Fazer um if, se o perfil da escola for igual aos tipos selecionados pelo usuario listar e colocar no recycleView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}