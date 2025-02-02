package com.atech.expensesync.firebase

import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient

object FirebaseInstance {

    val firestore: Firestore = FirestoreClient.getFirestore()
}