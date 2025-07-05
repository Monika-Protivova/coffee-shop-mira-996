package com.motycka.edu.order

import com.motycka.edu.menu.MenuItemDTO

object PriceCalculator {

    fun calculatePrice(
        menuItems: List<MenuItemDTO>,
        discountInPercent: Double,
        orderItems: List<OrderItemDTO> = emptyList()
    ): Double {
        // Create a map for fast menuItem lookup
        val menuItemMap = menuItems.associateBy { it.id }

        // Calculate total without discount
        val subtotal = orderItems.sumOf { item ->
            val menuItem = menuItemMap[item.menuItemId]
                ?: throw IllegalArgumentException("Menu item with ID ${item.menuItemId} not found")
            menuItem.price * item.quantity
        }

        // Apply discount
        val discountMultiplier = (100 - discountInPercent).coerceIn(0.0, 100.0) / 100.0
        return subtotal * discountMultiplier
    }
}
