package com.husseinsilver.store.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreManager {

    private static FirestoreManager instance;
    private final FirebaseFirestore db;

    private FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FirestoreManager getInstance() {
        if (instance == null) {
            instance = new FirestoreManager();
        }
        return instance;
    }

    public CollectionReference getCollection(String collectionPath) {
        return db.collection(collectionPath);
    }

    public DocumentReference getDocument(String collectionPath, String documentId) {
        return db.collection(collectionPath).document(documentId);
    }

    public Task<QuerySnapshot> getDocuments(String collectionPath) {
        return db.collection(collectionPath).get();
    }
}
