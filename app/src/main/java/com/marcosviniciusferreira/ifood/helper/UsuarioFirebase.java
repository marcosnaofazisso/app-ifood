package com.marcosviniciusferreira.ifood.helper;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

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

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(tipo)
                    .build();

            user.updateProfile(profile);
            return true;

        } catch (Exception e) {
            Log.e("ERROR", "Update UserType EXCEPTION: " + e.getMessage());
            return false;
        }



    }
}
