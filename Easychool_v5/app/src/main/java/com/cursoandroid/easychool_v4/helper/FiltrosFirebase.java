package com.cursoandroid.easychool_v4.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cursoandroid.easychool_v4.config.ConfiguracaoFirebase;
import com.cursoandroid.easychool_v4.model.FiltrosPesquisa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiltrosFirebase {
    private static String idResponsavel = ResponsavelFirebase.getIdentificadorResponsavel();
    private static DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private static DatabaseReference filtrosRef = firebaseRef.child("FiltrosPesquisa").child(idResponsavel);
    private static List<Integer> filtroDistancia = new ArrayList<>();
    private static FiltrosPesquisa filtros;

    public static void setFiltrosDistancia(){
        final int[] valor = new int[1];

        filtrosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String distancia = (String) dataSnapshot.child("Distancia").getValue();

                String[] distanciaQuebrada = distancia.split(" ");
                String[] teste = distanciaQuebrada[1].split(" ");

                try {
                    filtroDistancia.add(Integer.parseInt(distanciaQuebrada[1]));

                    if(filtroDistancia.get(0) != 0) {
                        filtros = new FiltrosPesquisa(filtroDistancia);
                    }
                }
                catch (Exception e){
                    filtroDistancia.add(Integer.parseInt(teste[0]));

                    valor[0] = filtroDistancia.get(0);

                    if(filtroDistancia.get(0) != 0) {
                        filtros = new FiltrosPesquisa(filtroDistancia);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}