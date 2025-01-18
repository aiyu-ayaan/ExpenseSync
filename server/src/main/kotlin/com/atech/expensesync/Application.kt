package com.atech.expensesync

import com.atech.expensesync.utils.SERVER_PORT
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
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
                <h1>Welcome to ExpenseSync</h1>
                Created by Ayaan
            </h1>
        </body>
    </html>
"""


fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        get("/") {
            call.respondText(hello, contentType = ContentType.Text.Html)
        }
    }
}