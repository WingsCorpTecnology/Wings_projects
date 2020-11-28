package com.cursoandroid.easychool_v4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cursoandroid.easychool_v4.R;

public class SplashActivity extends AppCompatActivity implements Runnable{
    Thread thread;
    Handler handler;
    int tempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        thread = new Thread(this);

        thread.start();

        getSupportActionBar().hide();
    }

    @Override
    public void run() {
        tempo = 1;

        try{
            while (tempo<100){
                Thread.sleep(15);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tempo++;
                    }
                });
            }
        }catch (Exception e){

        }

        startActivity(new Intent(this, MainActivity.class));
    }
}