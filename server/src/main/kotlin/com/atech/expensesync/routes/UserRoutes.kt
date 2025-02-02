package com.atech.expensesync.routes

import com.atech.expensesync.firebase.CreateUser
import com.atech.expensesync.database.models.User
import com.atech.expensesync.server_utils.handleException
import com.atech.expensesync.utils.ApiPaths
import com.atech.expensesync.utils.ResponseDataState
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.userRoutes() {
    routing {
        post("${ApiPaths.User.path}/create") {
            handleException {
                val model = call.receive<User>()
                if (model.uid.isEmpty()) {
                    call.respond(
                        HttpStatusCode.BadRequest, ResponseDataState.Error(
                            error = "User uid is required"
                        )
                    )
                    return@handleException
                }
                CreateUser().invoke(model)
                call.respond(
                    HttpStatusCode.Created, ResponseDataState.Success(
                        data = model
                    )
                )
            }
        }
    }
}

