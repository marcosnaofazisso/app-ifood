package com.marcosviniciusferreira.ifood.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marcosviniciusferreira.ifood.R;
import com.marcosviniciusferreira.ifood.helper.ConfiguracaoFirebase;
import com.marcosviniciusferreira.ifood.helper.UsuarioFirebase;
import com.marcosviniciusferreira.ifood.model.Empresa;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesEmpresaActivity extends AppCompatActivity {

    private EditText editEmpresaNome, editEmpresaCategoria, editEmpresaTempo, editEmpresaTaxa;
    private CircleImageView imagePerfilEmpresa;
    private StorageReference storageReference;
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;
    private String urlImagemSelecionada;
    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_empresa);

        inicializarComponentes();

        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //Configuracoes Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        imagePerfilEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );

                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }


            }
        });

        recuperarDadosEmpresa();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images
                                .Media
                                .getBitmap(
                                        getContentResolver(),
                                        localImagem
                                );
                        break;
                }
                if (imagem != null) {

                    imagePerfilEmpresa.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("empresas")
                            .child(idUsuarioLogado + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR", "Upload Photo Exception: " + e.getMessage());
                            Toast.makeText(ConfiguracoesEmpresaActivity.this, "Erro ao fazer upload de foto", Toast.LENGTH_SHORT);


                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            urlImagemSelecionada = taskSnapshot.getDownloadUrl().toString();

                            Toast.makeText(ConfiguracoesEmpresaActivity.this, "Foto atualizada com sucesso!", Toast.LENGTH_SHORT);
                        }
                    });

                }

            } catch (Exception e) {
                Log.d("ERROR", "Logout Exception: " + e.getMessage());


            }

        }
    }

    private void recuperarDadosEmpresa() {
        final DatabaseReference empresaRef = firebaseRef
                .child("empresas")
                .child(idUsuarioLogado);

        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    Empresa empresa = dataSnapshot.getValue(Empresa.class);

                    editEmpresaNome.setText(empresa.getNome());
                    editEmpresaCategoria.setText(empresa.getCategoria());
                    editEmpresaTaxa.setText(empresa.getPrecoEntrega().toString());
                    editEmpresaTempo.setText(empresa.getTempo());

                    urlImagemSelecionada = empresa.getUrlImagem();
                    if (urlImagemSelecionada != "") {
                        Picasso.get()
                                .load(urlImagemSelecionada)
                                .into(imagePerfilEmpresa);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void validarDadosEmpresa(View view) {

        String nome = editEmpresaNome.getText().toString();
        String categoria = editEmpresaCategoria.getText().toString();
        String tempo = editEmpresaTempo.getText().toString();
        String taxa = editEmpresaTaxa.getText().toString();

        if (!nome.isEmpty()) {
            if (!categoria.isEmpty()) {
                if (!tempo.isEmpty()) {
                    if (!taxa.isEmpty()) {

                        Empresa empresa = new Empresa(idUsuarioLogado, urlImagemSelecionada, nome, tempo, categoria, Double.parseDouble(taxa));
                        empresa.salvar();
                        finish();


                    } else {
                        exibirMensagemErro("Preencha o campo taxa!");
                    }

                } else {
                    exibirMensagemErro("Preencha o campo tempo de entrega!");
                }

            } else {
                exibirMensagemErro("Preencha o campo categoria!");
            }

        } else {
            exibirMensagemErro("Preencha o campo nome!");
        }

    }

    private void inicializarComponentes() {
        editEmpresaNome = findViewById(R.id.editEmpresaNome);
        editEmpresaCategoria = findViewById(R.id.editEmpresaCategoria);
        editEmpresaTempo = findViewById(R.id.editEmpresaTempo);
        editEmpresaTaxa = findViewById(R.id.editEmpresaTaxa);
        imagePerfilEmpresa = findViewById(R.id.imagePerfilEmpresa);

    }

    private void exibirMensagemErro(String mensagem) {
        Toast.makeText(ConfiguracoesEmpresaActivity.this, mensagem, Toast.LENGTH_SHORT);

    }
}
