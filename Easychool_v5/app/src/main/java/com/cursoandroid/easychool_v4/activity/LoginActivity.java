package com.cursoandroid.easychool_v4.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.model.ResponsavelAluno;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail, txtSenha;
    private CheckBox cbManterConectado;
    private FirebaseAuth autenticacao;
    private ResponsavelAluno responsavel;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference responsavelRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Amiko-Regular.ttf");

        txtEmail = findViewById(R.id.txt_email);
        txtSenha = findViewById(R.id.txtSenha);
        TextView textView6 = findViewById(R.id.txt_desc_login);
        TextView textView7 = findViewById(R.id.txt_title_login);
        cbManterConectado = findViewById(R.id.cb_manter_conectado);
        Button btn = findViewById(R.id.btn_login);

        txtEmail.setTypeface(typeface);
        txtSenha.setTypeface(typeface);
        textView6.setTypeface(typeface);
        textView7.setTypeface(typeface);
        cbManterConectado.setTypeface(typeface);
        btn.setTypeface(typeface);

        responsavel = new ResponsavelAluno();

        //Esconder a ActionBar
        getSupportActionBar().hide();
    }

    public void telaInicial(View view){
        if(verificarText()) {
            responsavel.setEmail(txtEmail.getText().toString());
            responsavel.setSenha(txtSenha.getText().toString());

            validarLogin();
        }
        else mensagemCampoVazio();
    }

    public boolean verificarText(){
        boolean preenchido;

        if(txtEmail.getText().toString().isEmpty()){
            preenchido = false;
        }
        else if(txtSenha.getText().toString().isEmpty()){
            preenchido = false;
        }
        else{
            preenchido = true;
        }
        return preenchido;
    }

    public void mensagemCampoVazio(){
        Toast.makeText(this, "Preencha os campos vazios para continuar", Toast.LENGTH_SHORT).show();
    }

    public void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        responsavelRef = firebaseRef.child("ResponsavelAluno").child(Base64Custom.codificarBase64(responsavel.getEmail()));

        responsavelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Log.e("erro", "usuario é um responsavel por aluno");

                    autenticacao.signInWithEmailAndPassword(responsavel.getEmail(), responsavel.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //msgLoginSucesso().show();

                                abrirTelaPrincipal();
                            }
                            else{
                                msgLoginErro(excessoes(task)).show();
                            }
                        }
                    });
                }
                else{
                    //Log.e("erro", "usuario não é um responsavel por aluno");

                    msgLoginUserNaoResp().show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Toast msgLoginErro(String erro){
        return Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_SHORT);
    }

    public String excessoes(@NonNull Task<AuthResult> task){
        String excecao = "";
        try{
            throw task.getException();
        } catch (FirebaseAuthInvalidUserException e) {
            excecao = "Usuário não cadastrado";
        } catch (FirebaseAuthInvalidCredentialsException e){
            excecao = "E-mail e senha não correspondem a um usuário cadastrado";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return excecao;
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

    public Toast msgLoginUserNaoResp(){
        return Toast.makeText(getApplicationContext(), "Esse usuário não é um responsável por aluno", Toast.LENGTH_LONG);
    }
}