package com.cursoandroid.easychool_v4.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Amiko-Regular.ttf");

        TextView textView = findViewById(R.id.txt_title_criar_conta);
        TextView textView2 = findViewById(R.id.txt_possui_conta);
        Button btn = findViewById(R.id.btn_responsavel_aluno);
        Button btn2 = findViewById(R.id.btn_login);

        textView.setTypeface(typeface);
        textView2.setTypeface(typeface);
        btn.setTypeface(typeface);
        btn2.setTypeface(typeface);

        //Esconder a ActionBar
        getSupportActionBar().hide();
    }

    @Override
    protected void onStart() {
        super.onStart();

        verificarUsuarioLogado();
    }

    public void Login(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

    public void CadastroResponsavelAluno(View view){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);

        finish();
    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
    }
}