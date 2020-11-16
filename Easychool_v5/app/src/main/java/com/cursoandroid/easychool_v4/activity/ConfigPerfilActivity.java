package com.cursoandroid.easychool_v4.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.model.ResponsavelAluno;
import com.cursoandroid.easychool_v4.validar.DefinirTamanhoText;
import com.cursoandroid.easychool_v4.validar.ValidarCpf;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ConfigPerfilActivity extends AppCompatActivity {
    private EditText edtNome, edtTelefone, edtCpf, edtNewSenha, edtRg;
    private Button btnSalvar;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private FirebaseUser responsavelUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);
    private ResponsavelAluno responsavel;
    private String CPF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_perfil);

        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtCpf = findViewById(R.id.edtCpf);
        edtRg = findViewById(R.id.edtRg);
        //edtEmail = findViewById(R.id.edtEmail);
        edtNewSenha = findViewById(R.id.edtSenhaNova);
        edtRg = findViewById(R.id.edtRg);
        btnSalvar = findViewById(R.id.btnSalvar);

        usuarioRef = firebaseRef.child("ResponsavelAluno").child(idResponsavel);
        responsavel = new ResponsavelAluno();

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                responsavel.setNome((String) dataSnapshot.child("nome").getValue());
                responsavel.setTelefone((String) dataSnapshot.child("telefone").getValue());
                responsavel.setCpf((String) dataSnapshot.child("cpf").getValue());
                responsavel.setRg((String) dataSnapshot.child("rg").getValue());

                Log.i("nome", responsavel.getNome());

                edtNome.setText(responsavel.getNome());
                edtTelefone.setText(responsavel.getTelefone());
                edtCpf.setText(responsavel.getCpf());
                edtRg.setText(responsavel.getRg());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportActionBar().hide();

        edtCpf.addTextChangedListener(DefinirTamanhoText.insert(edtCpf));

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarAlteracoes();
            }
        });
    }

    public boolean validarCpf(){
        int cpf = Integer.parseInt(edtCpf.getText().toString().substring(0, 3).trim());
        int cpf2 = Integer.parseInt(edtCpf.getText().toString().substring(4, 7));
        int cpf3 = Integer.parseInt(edtCpf.getText().toString().substring(8, 11));
        int cpf4 = Integer.parseInt(edtCpf.getText().toString().substring(12).trim());

        CPF = Integer.toString(cpf) + Integer.toString(cpf2) + Integer.toString(cpf3) + Integer.toString(cpf4);

        Log.i("INFO", CPF);

        if(ValidarCpf.isCPF(CPF)) {
            return true;
        }
        else{
            return false;
        }
    }

    public void mensagemCpfInvalido(){
        Toast.makeText(this, "Digite um CPF válido", Toast.LENGTH_SHORT).show();
    }

    public void mensagemAlteracoesSucesso(){
        Toast.makeText(this, "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
    }

    public void camposAlterar(){
        if(!edtNome.getText().toString().isEmpty()){
            alterarNome(edtNome.getText().toString().trim());
        }
        if(!edtTelefone.getText().toString().isEmpty() && !edtTelefone.getText().toString().equals("(  )     -")){
            alterarTelefone(edtTelefone.getText().toString().trim());
        }
        if(!edtCpf.getText().toString().isEmpty()){
            if(validarCpf()) {
                alterarCpf(edtCpf.getText().toString().trim());
            }
            else{
                mensagemCpfInvalido();
            }
        }
        if(!edtNewSenha.getText().toString().isEmpty()){
            alterarSenha(edtNewSenha.getText().toString().trim());
        }
        if(!edtRg.getText().toString().isEmpty()){
            alterarRg(edtRg.getText().toString().trim());
        }
    }

    private void alterarRg(String rg) {
        usuarioRef.child("rg").setValue(rg);
    }

    public void alterarNome(String nome){
        usuarioRef.child("nome").setValue(nome);
    }

    public void alterarTelefone(String telefone){
        usuarioRef.child("telefone").setValue(telefone);
    }

    public void alterarCpf(String cpf){
        usuarioRef.child("cpf").setValue(cpf);
    }

    /*public void alterarEmail(String email){
        responsavelUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Log.i("alteraEmail", "Email alterado com sucesso");
                }
            }
        });
    }*/

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

    public void confirmarAlteracoes(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertDialog
        alertDialog.setTitle("Confirmar alterações");
        alertDialog.setMessage("Você tem certeza que deseja alterar essas informações da sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                camposAlterar();

                //Log.i("alteraSenha", "Senha: " + edtNewSenha.getText().toString());

                mensagemAlteracoesSucesso();

                finish();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}