package com.marcosviniciusferreira.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcosviniciusferreira.ifood.R;
import com.marcosviniciusferreira.ifood.helper.ConfiguracaoFirebase;
import com.marcosviniciusferreira.ifood.helper.UsuarioFirebase;
import com.marcosviniciusferreira.ifood.model.Empresa;
import com.marcosviniciusferreira.ifood.model.Produto;
import com.marcosviniciusferreira.ifood.model.Usuario;
import com.squareup.picasso.Picasso;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private EditText editUsuarioNome, editUsuarioEndereco;
    private Button buttonSalvarDadosUsuario;
    private String idUsuario;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        inicializarComponentes();

        idUsuario = UsuarioFirebase.getIdUsuario();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        //Configuracoes Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recuperarDadosUsuario();

    }

    private void inicializarComponentes() {

        editUsuarioNome = findViewById(R.id.editUsuarioNome);
        editUsuarioEndereco = findViewById(R.id.editUsuarioEndereco);
        buttonSalvarDadosUsuario = findViewById(R.id.buttonSalvarDadosUsuario);


    }

    public void validarDadosUsuario(View view) {

        String nome = editUsuarioNome.getText().toString();
        String endereco = editUsuarioEndereco.getText().toString();


        if (!nome.isEmpty()) {
            if (!endereco.isEmpty()) {

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setNome(nome);
                usuario.setEndereco(endereco);
                usuario.salvar();

                Toast.makeText(ConfiguracoesUsuarioActivity.this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT);
                finish();


            } else {
                exibirMensagemErro("Preencha o campo descrição!");
            }

        } else {
            exibirMensagemErro("Preencha o campo nome!");
        }



    }

    private void exibirMensagemErro(String mensagem) {
        Toast.makeText(ConfiguracoesUsuarioActivity.this, mensagem, Toast.LENGTH_SHORT);

    }

    private void recuperarDadosUsuario() {
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(idUsuario);

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    editUsuarioNome.setText(usuario.getNome());
                    editUsuarioEndereco.setText(usuario.getEndereco());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
