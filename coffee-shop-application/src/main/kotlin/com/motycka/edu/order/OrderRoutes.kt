package com.motycka.edu.order

import com.motycka.edu.security.getUserIdentity
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val logger = KotlinLogging.logger {}

private const val ORDER_NOT_FOUND = "Order not found"
private const val INVALID_ID = "Invalid ID format"

fun Route.orderRoutes(
    orderService: Any, // Replace with the actual order service
    basePath: String
) {
    route("$basePath/orders") {

        // implement the routes for orders
        get {
            val orders = orderService.getAllOrders()
            call.respond(orders)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, INVALID_ID)
                return@get
            }

            val order = orderService.getOrderById(id)
            if (order == null) {
                call.respond(HttpStatusCode.NotFound, ORDER_NOT_FOUND)
            } else {
                call.respond(order)
            }
        }

        post {
            val userId = call.getUserIdentity()
            val orderRequest = call.receive<Order>()
            val newOrder = orderRequest.copy(userId = userId)
            val created = orderService.createOrder(newOrder)
            call.respond(HttpStatusCode.Created, created)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, INVALID_ID)
                return@put
            }

            val updatedOrder = call.receive<Order>()
            val success = orderService.updateOrder(id, updatedOrder)
            if (success) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, ORDER_NOT_FOUND)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, INVALID_ID)
                return@delete
            }

            val success = orderService.deleteOrder(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, ORDER_NOT_FOUND)
            }
        }
    }
}
