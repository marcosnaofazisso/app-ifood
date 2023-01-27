package com.marcosviniciusferreira.ifood.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import com.marcosviniciusferreira.ifood.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

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
