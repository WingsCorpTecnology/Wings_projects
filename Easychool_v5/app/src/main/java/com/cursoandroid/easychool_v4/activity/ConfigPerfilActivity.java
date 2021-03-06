package com.cursoandroid.easychool_v4.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cursoandroid.easychool_v4.Base64Custom;
import com.cursoandroid.easychool_v4.R;
import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.helper.Permissao;
import com.cursoandroid.easychool_v4.helper.ResponsavelFirebase;
import com.cursoandroid.easychool_v4.model.ResponsavelAluno;
import com.cursoandroid.easychool_v4.validar.DefinirTamanhoText;
import com.cursoandroid.easychool_v4.validar.ValidarCpf;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ConfigPerfilActivity extends AppCompatActivity {
    private EditText edtNome, edtTelefone, edtCpf, edtNewSenha, edtRg;
    private Button btnSalvar;
    private ImageView imgFoto, imgAddFoto;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private FirebaseUser responsavelUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private String emailResponsavel = autenticacao.getCurrentUser().getEmail();
    private String idResponsavel = Base64Custom.codificarBase64(emailResponsavel);
    private ResponsavelAluno responsavel;
    private String CPF;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageRef = ConfiguracaoFirebase.getStorage();
    private StorageReference pastaFotoPerfil = storageRef.child("imagensResponsaveisAluno");
    //Nome da imagem
    private String nomeArquivo = idResponsavel;
    private final StorageReference imagemRef = pastaFotoPerfil.child(nomeArquivo+ ".png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_perfil);

        //Valida permissões
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtCpf = findViewById(R.id.edtCpf);
        edtRg = findViewById(R.id.edtRg);
        //edtEmail = findViewById(R.id.edtEmail);
        edtNewSenha = findViewById(R.id.edtSenhaNova);
        btnSalvar = findViewById(R.id.btn_salvar_filtros);
        imgAddFoto = findViewById(R.id.imgAddImgPerfil);
        imgFoto = findViewById(R.id.imgPerfil);

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

        imgAddFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertCameraGaleria();
            }
        });

        //Recuperando dados do Usuário
        FirebaseUser usuario = ResponsavelFirebase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();

        if(url != null){
            Glide.with(ConfigPerfilActivity.this).load(url).into(imgFoto);
        }
        else{
            imgFoto.setImageResource(R.drawable.perfil);
        }
    }

    @Override
    public void onBackPressed() {
        //alertCancelar();
        if(verificarCampos()){
            alertCancelar();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;

            try{
                switch(requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");

                        break;

                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();

                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                        break;
                }

                if(imagem != null){
                    imgFoto.setImageBitmap(imagem);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void salvarImgBanco(){
        //Configura para imagem ser salva em memória
        imgFoto.setDrawingCacheEnabled(true);
        imgFoto.buildDrawingCache();

        //Recupera bitmap da imagem (imagem a ser carregada)
        Bitmap bitmap = imgFoto.getDrawingCache();

        //Comprimo bitmap para um formato png/jpg
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos);

        //converte o baos para pixel brutos em uma matriz de bytes
        //(dados da imagem)
        byte[] dadosImagem = baos.toByteArray();

        //Retorna objeto que irá controlar o upload
        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

        uploadTask.addOnFailureListener(ConfigPerfilActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConfigPerfilActivity.this, "Upload da imagem falhou: " +e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(ConfigPerfilActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Uri url = taskSnapshot.getDownloadUrl();

                Toast.makeText(ConfigPerfilActivity.this, "Sucesso ao fazer upload", Toast.LENGTH_LONG).show();

                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();

                        Log.i("teste", "url"+url);

                        atualizaFotoUsuario(url);
                    }
                });

                finish();
            }
        });
    }

    private void atualizaFotoUsuario(Uri url) {
        ResponsavelFirebase.atualizarFotoUsuario(url);
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertDialog
        alertDialog.setTitle("Permissões Negadas");
        alertDialog.setMessage("Para utilizar o app é necessário aceitar as permissões");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public boolean verificarCampos(){
        if(!edtNome.getText().toString().isEmpty()){
            return true;
        }
        if(!edtTelefone.getText().toString().isEmpty()){
            return true;
        }
        if(!edtCpf.getText().toString().isEmpty()){
            return true;
        }
        if(!edtRg.getText().toString().isEmpty()){
            return true;
        }
        if(!edtNewSenha.getText().toString().isEmpty()){
            return true;
        }

        return false;
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
        //Toast.makeText(this, "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
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
                salvarImgBanco();

                //Log.i("alteraSenha", "Senha: " + edtNewSenha.getText().toString());

                mensagemAlteracoesSucesso();


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

    public void alertCancelar(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertDialog
        alertDialog.setTitle("Voltar sem salvar");
        alertDialog.setMessage("Você deseja realmente voltar? As informações digitadas poderão ser perdidas");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void alertCameraGaleria(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertDialog
        alertDialog.setTitle("Selecionar foto de perfil");
        alertDialog.setMessage("Escolha uma das opções abaixo para escolher uma foto de perfil");
        alertDialog.setCancelable(true);

        alertDialog.setPositiveButton("Câmera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_CAMERA);
                }
            }
        });

        alertDialog.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}