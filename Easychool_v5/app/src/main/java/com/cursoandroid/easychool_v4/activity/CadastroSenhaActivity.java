package com.cursoandroid.easychool_v4.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroSenhaActivity extends AppCompatActivity {
    private TextView txtConfirmarSenha, txtSenha;
    private CheckBox cbCondicaoUso;
    private String nome, email, rg, cpf, telefone;
    private ResponsavelAluno responsavelAluno;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_senha);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Amiko-Regular.ttf");

        TextView textView = findViewById(R.id.txt_title_cadastro_escola);
        TextView textView2 = findViewById(R.id.txt_desc_cadastro_escola);
        txtConfirmarSenha = findViewById(R.id.txt_confirmar_senha_escola);
        txtSenha = findViewById(R.id.txt_senha_escola);
        cbCondicaoUso = findViewById(R.id.cbCondicaoUso2);
        Button btn = findViewById(R.id.btn_cadastrar);

        textView.setTypeface(typeface);
        textView2.setTypeface(typeface);
        txtConfirmarSenha.setTypeface(typeface);
        txtSenha.setTypeface(typeface);
        cbCondicaoUso.setTypeface(typeface);
        btn.setTypeface(typeface);
        responsavelAluno = new ResponsavelAluno();

        //Esconder a ActionBar
        getSupportActionBar().hide();
    }

    public void login(View view){
        if(verificarText()) {
            if(senhasIguais()) {
                setarUser();

                cadastrarResponsavel();
            }
            else mensagemSenhasDiferentes();
        }
        else mensagemCampoVazio();
    }

    public boolean verificarText(){
        boolean preenchido;

        if(txtConfirmarSenha.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else if(txtSenha.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else if(cbCondicaoUso.isChecked() == false){
            preenchido = false;
        }
        else{
            preenchido = true;
        }
        return preenchido;
    }

    public boolean senhasIguais(){
        if(txtSenha.getText().toString().equals(txtConfirmarSenha.getText().toString())){
            return true;
        }
        else{
            return false;
        }
    }

    public void mensagemCampoVazio(){
        Toast.makeText(this, "Preencha os campos vazios para continuar", Toast.LENGTH_SHORT).show();
    }

    public void mensagemSenhasDiferentes(){
        Toast.makeText(this, "As senhas estão diferentes, deixe-as iguais para continuar", Toast.LENGTH_SHORT).show();
    }

    public void setarUser(){
        //recuperar os dados da tela anterior
        nome = (String) getIntent().getSerializableExtra("nome");
        email = (String) getIntent().getSerializableExtra("email");
        rg = (String) getIntent().getSerializableExtra("rg");
        cpf = (String) getIntent().getSerializableExtra("cpf");
        telefone = (String) getIntent().getSerializableExtra("telefone");

        responsavelAluno.setNome(nome);
        responsavelAluno.setRg(rg);
        responsavelAluno.setCpf(cpf);
        responsavelAluno.setEmail(email);
        responsavelAluno.setTelefone(telefone);
        responsavelAluno.setSenha(txtSenha.getText().toString());
    }

    public void cadastrarResponsavel(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(responsavelAluno.getEmail(), responsavelAluno.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String idResponsavel = Base64Custom.codificarBase64(responsavelAluno.getEmail());
                    responsavelAluno.setId(idResponsavel);
                    responsavelAluno.salvar();

                    abrirMainActivity();
                }
                else{
                    msgCadastroErro(excessoes(task)).show();
                }
            }
        });
    }

    public String excessoes(@NonNull Task<AuthResult> task){
        String excecao = "";
        try{
            throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            excecao = "Digite uma senha mais forte!";
        } catch (FirebaseAuthInvalidCredentialsException e){
            excecao = "Por favor, digite um e-mail valido!";
        } catch (FirebaseAuthUserCollisionException e){
            excecao = "Essa conta já foi cadastrada";
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return excecao;
    }

    public Toast msgCadastroErro(String erro){
        return Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_SHORT);
    }

    public void abrirMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}