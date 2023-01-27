package com.marcosviniciusferreira.ifood.model;

import com.google.firebase.database.DatabaseReference;
import com.marcosviniciusferreira.ifood.helper.ConfiguracaoFirebase;

public class Produto {

    private String idEmpresa;
    private String nome;
    private String descricao;
    private Double preco;

    public Produto() {
    }

    public Produto(String idEmpresa, String nome, String descricao, Double preco) {
        this.idEmpresa = idEmpresa;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference produtoRef = firebaseRef.child("produtos")
                .child(getIdEmpresa());
        produtoRef.setValue(this);

    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
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
}
