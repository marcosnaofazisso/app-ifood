package com.marcosviniciusferreira.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marcosviniciusferreira.ifood.R;
import com.marcosviniciusferreira.ifood.adapter.AdapterProduto;
import com.marcosviniciusferreira.ifood.helper.ConfiguracaoFirebase;
import com.marcosviniciusferreira.ifood.helper.UsuarioFirebase;
import com.marcosviniciusferreira.ifood.listener.RecyclerItemClickListener;
import com.marcosviniciusferreira.ifood.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private DatabaseReference firebaseRef;
    private List<Produto> produtos = new ArrayList<>();

    private String idEmpresaLogada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        inicializarComponentes();

        auth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idEmpresaLogada = UsuarioFirebase.getIdUsuario();

        //Configuracoes Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - Empresa");
        setSupportActionBar(toolbar);

        //Configurar adapter
        adapterProduto = new AdapterProduto(produtos, this);

        //Configurar recyclerview
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        recyclerProdutos.setAdapter(adapterProduto);

        //Recupera produtos para a empresa
        recuperarProdutos();

        //Adicionar evento de clique no recyclerview
        recyclerProdutos.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerProdutos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        Produto produtoSelecionado = produtos.get(position);

                        produtoSelecionado.remover();

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));


    }

    private void inicializarComponentes() {
        recyclerProdutos = findViewById(R.id.recyclerProdutos);
    }

    private void recuperarProdutos() {

        final DatabaseReference produtosRef = firebaseRef.child("produtos")
                .child(idEmpresaLogada);

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    produtos.add(ds.getValue(Produto.class));
                }

                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        } else if (item.getItemId() == R.id.menuPedidos) {
            abrirPedidos();
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirPedidos() {

        startActivity(new Intent(EmpresaActivity.this, PedidosActivity.class));

    }

    private void deslogarUsuario() {
        try {

            auth.signOut();
            finish();

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
