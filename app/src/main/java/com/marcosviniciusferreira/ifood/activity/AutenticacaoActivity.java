package com.marcosviniciusferreira.ifood.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marcosviniciusferreira.ifood.R;
import com.marcosviniciusferreira.ifood.helper.ConfiguracaoFirebase;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso, switchAcessoUsuarioEmpresa;

    private LinearLayout linearLayoutEmpresa;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);

        getSupportActionBar().hide();

        inicializarComponentes();

        auth = ConfiguracaoFirebase.getFirebaseAuth();

        verificaUsuarioLogado();

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                validarCampos(email, senha);
            }
        });

        tipoAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutEmpresa.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutEmpresa.setVisibility(View.GONE);

                }
            }
        });

    }

    private void inicializarComponentes() {
        botaoAcessar = findViewById(R.id.buttonAcesso);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        tipoAcesso = findViewById(R.id.switchAcesso);
        linearLayoutEmpresa = findViewById(R.id.linearLayoutEmpresa);
        switchAcessoUsuarioEmpresa = findViewById(R.id.switchTipoAcesso);

    }

    private void validarCampos(String email, String senha) {
        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {

                acessarLoginOuCadastro(email, senha);

            } else {
                exibirMensagemErro("Preencha o campo senha!");

            }
        } else {
            exibirMensagemErro("Preencha o campo email!");
        }
    }

    private void exibirMensagemErro(String mensagem) {
        Toast.makeText(AutenticacaoActivity.this, mensagem, Toast.LENGTH_SHORT);

    }

    private void acessarLoginOuCadastro(String email, String senha) {
        if (tipoAcesso.isChecked()) { //Cadastro

            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        abrirTelaPrincipal();

                    } else {
                        exibirMensagemErro("Erro no cadastro. Verifique e tente novamente");
                    }
                }
            });

        } else {//Login

            auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        abrirTelaPrincipal();


                    } else {
                        exibirMensagemErro("Erro no login. Verifique e tente novamente");
                    }

                }
            });

        }
    }

    private void abrirTelaPrincipal() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void verificaUsuarioLogado() {
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual != null) {
            abrirTelaPrincipal();

        }
    }

}
