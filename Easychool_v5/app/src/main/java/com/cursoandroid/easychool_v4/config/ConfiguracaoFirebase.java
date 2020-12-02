package com.cursoandroid.easychool_v4.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {
    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;
    private static StorageReference storage;

    //retorna a intancida do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        autenticacao = FirebaseAuth.getInstance();

        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    //retorna a intancia do Firebase
    public static DatabaseReference getFirebaseDatabase(){
        if(firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

    //retorna a instancia do Storage
    public static StorageReference getStorage(){
        if(storage == null){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }
}