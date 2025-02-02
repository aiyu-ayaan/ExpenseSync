package com.atech.expensesync.routes

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

private const val hello = """
    <html>
        <head>
            <title>Research Hub</title>
        </head>
        <body>
            <h1>
                <h1>Welcome to ExpenseSync Server</h1>
                Created by Ayaan
            </h1>
        </body>
    </html>
"""

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText(hello, contentType = ContentType.Text.Html)
        }
    }
    userRoutes()
}


