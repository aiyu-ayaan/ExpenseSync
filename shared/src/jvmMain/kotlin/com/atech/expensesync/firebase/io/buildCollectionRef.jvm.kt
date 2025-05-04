package com.atech.expensesync.firebase.io

import com.atech.expensesync.firebase.helper.FirebaseHelper
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore

fun buildCollectionRef(
    vararg firebaseHelper: FirebaseHelper,
    firestore: Firestore
): CollectionReference {
    if (firebaseHelper.isEmpty()) {
        throw IllegalArgumentException("At least one FirebaseHelper must be provided")
    }

    var reference: Any = firestore.collection(firebaseHelper[0].collectionName!!)

    firebaseHelper.toList()
        .forEachIndexed { index, helper ->
            if (index == 0) return@forEachIndexed

            // Check if we need to navigate to a document
            if (helper.documentName != null) {
                if (reference is CollectionReference) {
                    reference = reference.document(helper.documentName)
                } else if (reference is DocumentReference) {
                    reference =
                        reference.collection(helper.collectionName!!).document(helper.documentName)
                }
            }
            // Check if we need to apply queries
            else if (helper.queries != null) {
                if (reference is CollectionReference) {
                    reference = reference.whereEqualTo(helper.queries.first, helper.queries.second)
                } else {
                    throw IllegalArgumentException("Queries can only be applied to CollectionReference")
                }
            } else {
                throw IllegalArgumentException("Document name or queries must be provided")
            }
        }

    // Ensure the final reference is a CollectionReference
    if (reference is CollectionReference) {
        return reference
    } else {
        throw IllegalArgumentException("The chain of operations must result in a CollectionReference")
    }
}