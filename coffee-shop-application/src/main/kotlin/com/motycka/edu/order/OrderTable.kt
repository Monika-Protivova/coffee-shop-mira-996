package com.motycka.edu.order

// TODO implement OrderTable and OrderDAO
import com.motycka.edu.user.UserTable // Make sure this exists or replace it with a long("customer_id")
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.datetime

object OrderTable : LongIdTable("orders") {
    val customerId = long("customer_id")
    val status = enumerationByName("status", 20, OrderStatus::class)
    val createdAt = datetime("created_at")
}
