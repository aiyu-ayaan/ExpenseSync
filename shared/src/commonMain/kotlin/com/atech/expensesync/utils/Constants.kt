package com.atech.expensesync.utils

const val SERVER_PORT = 9090

const val FIREBASE_PARENT_COLLECTION = "ExpanseSync"
const val FIREBASE_PARENT_COLLECTION_VERSION = "v1"

enum class FirebaseCollectionPath(val path: String) {
    USER("$FIREBASE_PARENT_COLLECTION/$FIREBASE_PARENT_COLLECTION_VERSION/user"),
}


const val API_PATH = "/api/v1"
const val WEB_SOCKET_PATH = "/ws/v1"

enum class ApiPaths(val path: String){
    User("$API_PATH/user"),
}

enum class WebSocketPaths(val path: String){
    User("$WEB_SOCKET_PATH/user"),
}