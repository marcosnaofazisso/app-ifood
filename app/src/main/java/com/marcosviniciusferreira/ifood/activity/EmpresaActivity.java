package com.marcosviniciusferreira.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.marcosviniciusferreira.ifood.R;
import com.marcosviniciusferreira.ifood.helper.ConfiguracaoFirebase;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        auth = ConfiguracaoFirebase.getFirebaseAuth();

        //Configuracoes Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - Empresa");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuSair) {
            deslogarUsuario();
        } else if (item.getItemId() == R.id.menuConfiguracoes) {
            abrirConfiguracoes();
        } else if (item.getItemId() == R.id.menuNovoProduto) {
            abrirNovoProduto();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        try {

            auth.signOut();

        } catch (Exception e) {
            Log.d("ERROR", "Logout Exception: " + e.getMessage());

        }

    }

    private void abrirConfiguracoes() {

        startActivity(new Intent(EmpresaActivity.this, ConfiguracoesEmpresaActivity.class));

    }

    private void abrirNovoProduto() {
        startActivity(new Intent(EmpresaActivity.this, NovoProdutoEmpresaActivity.class));

    }
}
