package com.marcosviniciusferreira.ifood.activity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import com.marcosviniciusferreira.ifood.model.Pedido;
import com.marcosviniciusferreira.ifood.model.Usuario;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private AdapterPedido adapter;
    private List<Pedido> pedidos;
    private AlertDialog dialog;

    private DatabaseReference firebaseRef;
    private String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);


        //Configuracoes Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
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
                    dialog.dismiss();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
