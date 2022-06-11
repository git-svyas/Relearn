package com.example.mobileapp.repository;

import android.util.Log;
import com.example.mobileapp.model.Notes;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class NotesRepository {

    private static final String TAG = "NOTES";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final static String NOTES_COLLECTION = "notes";
    private final CollectionReference notesCollectionRef = db.collection(NOTES_COLLECTION);


    public void getNotes(String subject, List<Notes> notesList) {
        Query query = notesCollectionRef.whereEqualTo("subject", subject);
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Notes note = documentSnapshot.toObject(Notes.class);
                        note.setId(documentSnapshot.getId());
                        notesList.add(note);
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, e.toString()));
    }

    public void getNotesByFilter(String subject, List<Integer> filters, List<Notes> notesList) {
        Query query = notesCollectionRef.whereEqualTo("subject",subject).whereArrayContainsAny("tags",filters);
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Notes note = documentSnapshot.toObject(Notes.class);
                        note.setId(documentSnapshot.getId());
                        notesList.add(note);
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, e.toString()));
    }

}
