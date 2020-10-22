package com.dps0340.attman

class BooleanHolder(private val initialValue: Boolean = false) {
    private var holding: Boolean = initialValue;
    public fun set(updateValue: Boolean): Unit {
        holding = updateValue
    }
    public fun get(): Boolean {
        return holding
    }
}