package com.cursoandroid.easychool_v4.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.model.ResponsavelAluno;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class ConfigPerfilActivity extends AppCompatActivity {
    private EditText edtNome, edtTelefone, edtCpf, edtEmail, edtNewSenha;
    private Button btnSalvar;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private FirebaseUser responsavelUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);
    private ResponsavelAluno responsavel = new ResponsavelAluno();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_perfil);

        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtCpf = findViewById(R.id.edtCpf);
        edtEmail = findViewById(R.id.edtEmail);
        edtNewSenha = findViewById(R.id.edtSenhaNova);

        usuarioRef = firebaseRef.child("ResponsavelAluno").child(idResponsavel);

        getSupportActionBar().hide();
    }

    public void alterarNome(String nome){
        usuarioRef.child("nome").setValue(nome);
    }

    public void alterarTelefone(String nome){
        usuarioRef.child("nome").setValue(nome);
    }

    public void alterarCpf(String nome){
        usuarioRef.child("nome").setValue(nome);
    }

    public void alterarEmail(String email){
        responsavelUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                     

                    Log.i("alteraEmail", "Email alterado com sucesso");
                }
            }
        });
    }

    public void alterarSenha(String senha){
        responsavelUser.updatePassword(senha).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("alteraSenha", "Senha alterada com sucesso");
                }
            }
        });
    }
}