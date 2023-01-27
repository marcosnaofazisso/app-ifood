package com.marcosviniciusferreira.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.marcosviniciusferreira.ifood.R;
import com.marcosviniciusferreira.ifood.helper.UsuarioFirebase;
import com.marcosviniciusferreira.ifood.model.Empresa;
import com.marcosviniciusferreira.ifood.model.Produto;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private EditText editProdutoNome, editProdutoDescricao, editProdutoPreco;
    private Button buttonCadastrarProduto;
    private String idEmpresaLogada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);

        inicializarComponentes();

        idEmpresaLogada = UsuarioFirebase.getIdUsuario();

        //Configuracoes Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void validarDadosProduto(View view) {

        String nome = editProdutoNome.getText().toString();
        String descricao = editProdutoDescricao.getText().toString();
        String preco = editProdutoPreco.getText().toString();


        if (!nome.isEmpty()) {
            if (!descricao.isEmpty()) {
                if (!preco.isEmpty()) {

                    Produto produto = new Produto(idEmpresaLogada, nome, descricao, Double.parseDouble(preco));
                    produto.salvar();


                } else {
                    exibirMensagemErro("Preencha o campo preço!");
                }

            } else {
                exibirMensagemErro("Preencha o campo descrição!");
            }

        } else {
            exibirMensagemErro("Preencha o campo nome!");
        }

    }

    private void inicializarComponentes() {
        editProdutoNome = findViewById(R.id.editProdutoNome);
        editProdutoDescricao = findViewById(R.id.editProdutoDescricao);
        editProdutoPreco = findViewById(R.id.editProdutoPreco);
        buttonCadastrarProduto = findViewById(R.id.buttonCadastrarProduto);

    }

    private void exibirMensagemErro(String mensagem) {
        Toast.makeText(NovoProdutoEmpresaActivity.this, mensagem, Toast.LENGTH_SHORT);

    }
}
