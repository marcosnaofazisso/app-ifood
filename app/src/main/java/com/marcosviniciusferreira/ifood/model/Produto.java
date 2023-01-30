package com.marcosviniciusferreira.ifood.model;

import com.google.firebase.database.DatabaseReference;
import com.marcosviniciusferreira.ifood.helper.ConfiguracaoFirebase;

public class Produto {

    private String idEmpresa;
    private String idProduto;
    private String nome;
    private String descricao;
    private String urlImagem;
    private Double preco;

    public Produto() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference produtoRef = firebaseRef.child("produtos");
        setIdProduto(produtoRef.push().getKey());
    }


    public void salvar() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos")
                .child(getIdEmpresa())
                .child(getIdProduto());
        produtoRef.setValue(this);

    }

    public void remover() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos")
                .child(getIdEmpresa())
                .child(getIdProduto());
        produtoRef.removeValue();
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }
}
