package com.atech.expensesync.firebase.helper

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

fun buildCollectionRef(
    vararg firebaseHelper: FirebaseHelper,
    firestore: FirebaseFirestore
): CollectionReference {
    val collectionRef = firestore.collection(firebaseHelper[0].collectionName)
    firebaseHelper.toList()
        .forEachIndexed { index, firebaseHelper ->
            if (index == 0) return@forEachIndexed
            if (firebaseHelper.documentName != null) {
                collectionRef.document(firebaseHelper.documentName)
            } else if (firebaseHelper.queries != null) {
                collectionRef.whereEqualTo(
                    firebaseHelper.queries.first,
                    firebaseHelper.queries.second
                )
            } else {
                throw IllegalArgumentException("Document name or queries must be provided")
            }
        }
    return collectionRef
}
