package com.marcosviniciusferreira.ifood.activity;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.marcosviniciusferreira.ifood.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                abrirAutenticacao();

            }
        }, 3000);
    }



    private void abrirAutenticacao() {
        Intent i = new Intent(SplashActivity.this,
                AutenticacaoActivity.class);
        startActivity(i);
        finish();
    }
}
