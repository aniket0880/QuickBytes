package com.example.quickbyte.Domain


class Time {
    var id: Int = 0
    var value: String? = null

    override fun toString(): String {
        return value!!
    }
}