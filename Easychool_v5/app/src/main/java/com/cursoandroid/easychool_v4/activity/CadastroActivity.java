package com.cursoandroid.easychool_v4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.validar.DefinirTamanhoText;
import com.cursoandroid.easychool_v4.validar.ValidarCpf;

public class CadastroActivity extends AppCompatActivity {
    EditText txtNome, txtEmail, txtTelefone, txtRg, txtCpf;
    String CPF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Amiko-Regular.ttf");

        TextView textView = findViewById(R.id.txt_cadastro);
        TextView textView2 = findViewById(R.id.txt_desc_cadastro);
        txtNome = findViewById(R.id.txt_nome);
        txtEmail = findViewById(R.id.txt_email);
        txtTelefone = findViewById(R.id.txt_telefone);
        txtRg = findViewById(R.id.txt_rg);
        txtCpf = findViewById(R.id.txt_cpf);
        Button btn = findViewById(R.id.btnCadastrar);

        textView.setTypeface(typeface);
        textView2.setTypeface(typeface);
        txtNome.setTypeface(typeface);
        txtEmail.setTypeface(typeface);
        txtTelefone.setTypeface(typeface);
        txtRg.setTypeface(typeface);
        txtCpf.setTypeface(typeface);
        btn.setTypeface(typeface);

        //Esconder a ActionBar
        getSupportActionBar().hide();

        txtCpf.addTextChangedListener(DefinirTamanhoText.insert(txtCpf));

    }

    public void telaIncial(View view){
        if(verificarText()){
            if(validarFormatoEmail(txtEmail.getText().toString())) {
                int cpf = Integer.parseInt(txtCpf.getText().toString().substring(0, 3).trim());
                int cpf2 = Integer.parseInt(txtCpf.getText().toString().substring(4, 7));
                int cpf3 = Integer.parseInt(txtCpf.getText().toString().substring(8, 11));
                int cpf4 = Integer.parseInt(txtCpf.getText().toString().substring(12).trim());

                CPF = Integer.toString(cpf) + Integer.toString(cpf2) + Integer.toString(cpf3) + Integer.toString(cpf4);

                Log.i("INFO", CPF);

                if(ValidarCpf.isCPF(CPF)) {
                    Intent intent = new Intent(this, CadastroSenhaActivity.class);

                    intent.putExtra("nome", txtNome.getText().toString());
                    intent.putExtra("email", txtEmail.getText().toString());
                    intent.putExtra("rg", txtRg.getText().toString());
                    intent.putExtra("cpf", txtCpf.getText().toString());
                    intent.putExtra("telefone", txtTelefone.getText().toString());

                    startActivity(intent);

                    finish();
                }
                else mensagemCpfInvalido();
            }
            else mensagemEmailInvalido();
        }
        else mensagemCampoVazio();
    }

    public boolean verificarText(){
        boolean preenchido;

        if(txtRg.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else if(txtCpf.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else if(txtEmail.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else if(txtTelefone.getText().toString().trim().equals("")){
            preenchido = false;
        }
        else if(txtNome.getText().toString().trim().equals("")){
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

    public void mensagemEmailInvalido(){
        Toast.makeText(this, "Digite um email válido", Toast.LENGTH_SHORT).show();
    }

    public void mensagemCpfInvalido(){
        Toast.makeText(this, "Digite um CPF válido", Toast.LENGTH_SHORT).show();
    }

    public boolean validarFormatoEmail(final String email){
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }
        else return false;
    }
}