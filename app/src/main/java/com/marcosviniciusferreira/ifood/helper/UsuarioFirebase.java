package com.marcosviniciusferreira.ifood.helper;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioFirebase {

    public static String getIdUsuario() {
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
        return auth.getCurrentUser().getUid();
    }

    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAuth();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarTipoUsuario(String tipo) {




        return true;
    }
}
