package com.cursoandroid.easychool_v4.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ResponsavelFirebase {
    private static FirebaseAuth responsavel = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private static String email = responsavel.getCurrentUser().getEmail();

    public static String getIdentificadorResponsavel(){
        String idResponsavel = Base64Custom.codificarBase64(email);

        return idResponsavel;
    }

    public static FirebaseUser getUsuarioAtual(){
        return responsavel.getCurrentUser();
    }

    public static boolean atualizarFotoUsuario(Uri url){
        FirebaseUser user = getUsuarioAtual();
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();

        try{
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar foto usuário");
                    }
                }
            });
            return true;
        }
        catch (Exception e){
            e.printStackTrace();

            return false;
        }
    }
}