package com.cursoandroid.easychool_v4.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.activity.ConfigFiltrosPesquisaActivity;
import com.cursoandroid.easychool_v4.activity.ConfigPerfilActivity;
import com.cursoandroid.easychool_v4.adapter.AdapterFiltros;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.helper.Geocoding;
import com.cursoandroid.easychool_v4.model.ResponsavelAluno;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {
    private TextView edtNome, edtTelefone, edtCpf, edtRg, edtEmail, edtEndereco;
    private Button btnUpdate, btnAddFiltros;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);
    private ResponsavelAluno responsavel;
    private Geocoding geocoding;
    private String espacamento = "  ";

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

        usuarioRef = firebaseRef.child("ResponsavelAluno").child(idResponsavel);
        responsavel = new ResponsavelAluno();

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
}