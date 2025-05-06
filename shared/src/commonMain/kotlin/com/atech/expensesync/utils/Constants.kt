package com.atech.expensesync.utils

const val SERVER_PORT = 9090

const val FIREBASE_PARENT_COLLECTION = "ExpanseSync"
const val FIREBASE_PARENT_COLLECTION_VERSION = "v1"

enum class FirebaseCollectionPath(val path: String) {
    USER("$FIREBASE_PARENT_COLLECTION/$FIREBASE_PARENT_COLLECTION_VERSION/${FirebaseDocumentName.USER.path}"),
    MEAL("${USER.path}/data"),
    SPLIT("$FIREBASE_PARENT_COLLECTION/$FIREBASE_PARENT_COLLECTION_VERSION/${FirebaseDocumentName.SPLIT.path}")
}


enum class FirebaseDocumentName(val path: String) {
    USER("user"),
    MEAL_BOOK("meal_book"),
    MEAL_BOOK_ENTRY("meal_book_entry"),
    EXPENSE_BOOK("expense_book"),
    EXPENSE_BOOK_ENTRY("expense_book_entry"),
    SPLIT("split"),
    SPLIT_TRANSACTION("split_transaction"),
    SPLIT_TRANSACTION_ENTRY("split_transaction_entry"),
}

const val API_PATH = "/api/v1"
const val WEB_SOCKET_PATH = "/ws/v1"

enum class ApiPaths(val path: String) {
    User("$API_PATH/user"),
}

enum class WebSocketPaths(val path: String) {
    User("$WEB_SOCKET_PATH/user"),
}

const val FLAG_LOG_OUT = "log_out"