package com.marcosviniciusferreira.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marcosviniciusferreira.ifood.R;
import com.marcosviniciusferreira.ifood.adapter.AdapterProduto;
import com.marcosviniciusferreira.ifood.helper.ConfiguracaoFirebase;
import com.marcosviniciusferreira.ifood.helper.UsuarioFirebase;
import com.marcosviniciusferreira.ifood.listener.RecyclerItemClickListener;
import com.marcosviniciusferreira.ifood.model.Empresa;
import com.marcosviniciusferreira.ifood.model.ItemPedido;
import com.marcosviniciusferreira.ifood.model.Pedido;
import com.marcosviniciusferreira.ifood.model.Produto;
import com.marcosviniciusferreira.ifood.model.Usuario;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
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
    private DatabaseReference firebaseRef;

    private List<Produto> produtos = new ArrayList<>();
    private List<ItemPedido> itensCarrinho = new ArrayList<>();

    private Pedido pedidoRecuperado;

    private AlertDialog dialog;
    private Usuario usuario;
    private String idUsuarioLogado;
    private TextView textCarrinhoQtd, textCarrinhoTotal;
    private int qtdItensCarrinho;
    private Double totalCarrinho;

    private int metodoPagamento;

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

        //Configurar evento de clique
        recyclerProdutosCardapio.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerProdutosCardapio,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        confirmarQuantidade(position);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        //Recuperar produtos da empresa
        recuperarCardapio();

        //Recuperar dados do usuario
        recuperarDadosUsuario();


    }

    private void confirmarQuantidade(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantidade");
        builder.setMessage("Digite a quantidade");

        final EditText editQuantidade = new EditText(this);
        editQuantidade.setInputType(InputType.TYPE_CLASS_NUMBER);
        editQuantidade.setText("1");

        builder.setView(editQuantidade);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Produto produtoSelecionado = produtos.get(position);
                String quantidade = editQuantidade.getText().toString();

                if (Integer.parseInt(quantidade) < 1) {
                    Toast.makeText(CardapioActivity.this, "Quantidade incorreta!", Toast.LENGTH_SHORT);

                } else {

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setIdProduto(produtoSelecionado.getIdProduto());
                    itemPedido.setNomeProduto(produtoSelecionado.getNome());
                    itemPedido.setPreco(produtoSelecionado.getPreco());
                    itemPedido.setQuantidade(Integer.parseInt(quantidade));
                    itensCarrinho.add(itemPedido);

                    if (pedidoRecuperado == null) {
                        pedidoRecuperado = new Pedido(idUsuarioLogado, idEmpresa);
                    }
                    pedidoRecuperado.setNome(usuario.getNome());
                    pedidoRecuperado.setEndereco(usuario.getEndereco());
                    pedidoRecuperado.setItens(itensCarrinho);
                    pedidoRecuperado.salvar();

                }


            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


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

        DatabaseReference pedidoRef = firebaseRef.child("pedidos_usuario")
                .child(idEmpresa)
                .child(idUsuarioLogado);

        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                itensCarrinho = new ArrayList<>();
                qtdItensCarrinho = 0;
                totalCarrinho = 0.0;

                if (dataSnapshot.getValue() != null) {

                    pedidoRecuperado = dataSnapshot.getValue(Pedido.class);

                    itensCarrinho = pedidoRecuperado.getItens();


                    for (ItemPedido itemPedido : itensCarrinho) {

                        int qtde = itemPedido.getQuantidade();
                        Double preco = itemPedido.getPreco();

                        totalCarrinho += (qtde * preco);
                        qtdItensCarrinho += qtde;

                    }

                }

                DecimalFormat df = new DecimalFormat("0.00");

                textCarrinhoQtd.setText("qtd: " + qtdItensCarrinho);
                textCarrinhoTotal.setText("R$ " + df.format(totalCarrinho));

                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
            confirmarPedido();

        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmarPedido() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione um método de pagamento");


        CharSequence[] itens = new CharSequence[]{
                "Máquina de Cartão", "Dinheiro", "Pix"
        };
        builder.setSingleChoiceItems(itens, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Opções do checked item:
                // 0 -> máquina de cartão
                // 1 -> dinheiro
                // 2 -> pix

                metodoPagamento = which;

            }
        });

        final EditText editObservacao = new EditText(this);
        editObservacao.setHint("Digite uma observação");
        builder.setView(editObservacao);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String observacao = editObservacao.getText().toString();
                pedidoRecuperado.setMetodoPagamento(metodoPagamento);
                pedidoRecuperado.setObservacao(observacao);
                pedidoRecuperado.setStatus("confirmado");
                pedidoRecuperado.confirmar();

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void inicializarComponentes() {

        recyclerProdutosCardapio = findViewById(R.id.recyclerProdutoCardapio);
        imagemEmpresaCardapio = findViewById(R.id.imageEmpresaCardapio);
        textNomeEmpresaCardapio = findViewById(R.id.textNomeEmpresaCardapio);

        textCarrinhoQtd = findViewById(R.id.textCarrinhoQtd);
        textCarrinhoTotal = findViewById(R.id.textCarrinhoTotal);


    }


}
