package com.marcosviniciusferreira.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marcosviniciusferreira.ifood.R;
import com.marcosviniciusferreira.ifood.adapter.AdapterProduto;
import com.marcosviniciusferreira.ifood.helper.ConfiguracaoFirebase;
import com.marcosviniciusferreira.ifood.helper.UsuarioFirebase;
import com.marcosviniciusferreira.ifood.model.Empresa;
import com.marcosviniciusferreira.ifood.model.Produto;
import com.marcosviniciusferreira.ifood.model.Usuario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CardapioActivity extends AppCompatActivity {

    private RecyclerView recyclerProdutosCardapio;
    private AdapterProduto adapterProduto;
    private ImageView imagemEmpresaCardapio;
    private TextView textNomeEmpresaCardapio;
    private Empresa empresaSelecionada;
    private String idEmpresa;
    private List<Produto> produtos = new ArrayList<>();
    private DatabaseReference firebaseRef;

    private AlertDialog dialog;
    private Usuario usuario;
    private String idUsuarioLogado;
    private TextView textCarrinhoQtd, textCarrinhoTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        inicializarComponentes();

        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //Recuperar empresa selecionada
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            empresaSelecionada = (Empresa) bundle.getSerializable("empresa");

            textNomeEmpresaCardapio.setText(empresaSelecionada.getNome());
            idEmpresa = empresaSelecionada.getIdUsuario();

            String url = empresaSelecionada.getUrlImagem();
            Picasso.get().load(url).into(imagemEmpresaCardapio);
        }

        //Configuracoes Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cardápio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Configurar adapter
        adapterProduto = new AdapterProduto(produtos, this);

        //Configurar recyclerview
        recyclerProdutosCardapio.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutosCardapio.setHasFixedSize(true);
        recyclerProdutosCardapio.setAdapter(adapterProduto);

        recuperarCardapio();

        recuperarDadosUsuario();


    }

    private void recuperarDadosUsuario() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando...")
                .setCancelable(false)
                .build();

        dialog.show();

        final DatabaseReference usuariosRef = firebaseRef.child("usuarios")
                .child(idUsuarioLogado);

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    usuario = dataSnapshot.getValue(Usuario.class);
                }

                recuperarPedido();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void recuperarPedido() {

        dialog.dismiss();

    }

    private void recuperarCardapio() {
        DatabaseReference produtosRef = firebaseRef.child("produtos")
                .child(idEmpresa);

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
        inflater.inflate(R.menu.menu_cardapio, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuPedido) {
            finalizarPedido();

        }
        
        return super.onOptionsItemSelected(item);
    }

    private void finalizarPedido() {
    }

    private void inicializarComponentes() {

        recyclerProdutosCardapio = findViewById(R.id.recyclerProdutoCardapio);
        imagemEmpresaCardapio = findViewById(R.id.imageEmpresaCardapio);
        textNomeEmpresaCardapio = findViewById(R.id.textNomeEmpresaCardapio);


    }


}
