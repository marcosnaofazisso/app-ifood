package com.marcosviniciusferreira.ifood.activity;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosviniciusferreira.ifood.R;
import com.marcosviniciusferreira.ifood.adapter.AdapterPedido;
import com.marcosviniciusferreira.ifood.helper.ConfiguracaoFirebase;
import com.marcosviniciusferreira.ifood.helper.UsuarioFirebase;
import com.marcosviniciusferreira.ifood.listener.RecyclerItemClickListener;
import com.marcosviniciusferreira.ifood.model.Pedido;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private AdapterPedido adapter;
    private List<Pedido> pedidos = new ArrayList<>();
    private AlertDialog dialog;

    private DatabaseReference firebaseRef;
    private String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);


        //Configuracoes Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerPedidos = findViewById(R.id.recyclerPedidos);

        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idEmpresa = UsuarioFirebase.getIdUsuario();

        adapter = new AdapterPedido(pedidos);

        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setHasFixedSize(true);
        recyclerPedidos.setAdapter(adapter);

        recuperarPedidos();

        recyclerPedidos.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerPedidos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        Pedido pedido = pedidos.get(position);
                        pedido.setStatus("finalizado");
                        pedido.atualizarStatus();

                        Toast.makeText(PedidosActivity.this, "Pedido finalizado com sucesso!", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));


    }

    private void recuperarPedidos() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando...")
                .setCancelable(false)
                .build();

        dialog.show();

        final DatabaseReference pedidoRef = firebaseRef.child("pedidos")
                .child(idEmpresa);

        Query pesquisaPedido = pedidoRef.orderByChild("status")
                .equalTo("confirmado");

        pesquisaPedido.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pedidos.clear();
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        Pedido pedido = ds.getValue(Pedido.class);
                        pedidos.add(pedido);


                    }

                    adapter.notifyDataSetChanged();

                }
                if (pedidos.size() < 1) {
                    TextView textView = new TextView(PedidosActivity.this);
                    textView.setText("Não há pedidos atualmente");
                    textView.setTextSize(20);
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setPadding(20, 30, 20, 20);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
                    PedidosActivity.super.setContentView(textView);


                }
                dialog.dismiss();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
