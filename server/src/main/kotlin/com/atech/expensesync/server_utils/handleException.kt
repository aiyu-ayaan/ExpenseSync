package com.atech.expensesync.server_utils

import com.atech.expensesync.utils.ResponseDataState
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.CannotTransformContentToTypeException
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext

suspend fun RoutingContext.handleException(
    action: suspend () -> Unit,
) {
    try {
        action()
    } catch (e: CannotTransformContentToTypeException) {
        call.respond(
            HttpStatusCode.BadRequest, ResponseDataState.Error(
                error = "Unable to parse request body or missing required fields"
            )
        )
    } catch (e: Exception) {
        call.respond(
            HttpStatusCode.InternalServerError, ResponseDataState.Error(
                error = "Failed to create user: ${e.javaClass.simpleName} ${e.message}"
            )
        )
    }
}