package com.example.quickbyte.Domain

import java.io.Serializable


class Foods : Serializable {
    var categoryId: Int = 0
    var description: String? = null
    var isBestFood: Boolean = false
    var id: Int = 0
    var locationId: Int = 0
    var price: Double = 0.0
    var imagePath: String? = null
    var priceId: Int = 0
    var star: Double = 0.0
    var timeId: Int = 0
    var timeValue: Int = 0
    var title: String? = null
    var numberInCart: Int = 0

    override fun toString(): String {
        return title!!
    }
}