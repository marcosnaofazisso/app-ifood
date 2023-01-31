package com.marcosviniciusferreira.ifood.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth referenciaAuth;
    private static StorageReference referenciaStorage;


    public static DatabaseReference getFirebaseDatabase() {
        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;



    }

    public static FirebaseAuth getFirebaseAuth() {
        if (referenciaAuth == null) {
            referenciaAuth = FirebaseAuth.getInstance();
        }
        return referenciaAuth;

    }

    public static StorageReference getFirebaseStorage() {
        if (referenciaStorage == null) {
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;

    }
}
