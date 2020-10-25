package com.dps0340.attman

class BooleanHolder(private val initialValue: Boolean = false) {
    private var holding: Boolean = initialValue
    fun set(updateValue: Boolean): Unit {
        holding = updateValue
    }
    fun get(): Boolean {
        return holding
    }
}