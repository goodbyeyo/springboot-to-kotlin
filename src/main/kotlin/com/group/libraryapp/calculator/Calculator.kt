package com.group.libraryapp.calculator

//data class Calculator (
class Calculator (
//    private var _number: Int
    var number: Int
) {
//    val number: Int
//        get() = this._number

    fun add(operand: Int) {
        this.number += operand
    }

    fun minus(operand: Int) {
        this.number -= operand
    }

    fun multiply(operand: Int) {
        this.number *= operand
    }

    fun divide(operand: Int) {
        if (operand == 0) {
            throw IllegalArgumentException("0으로 나눌수 없습니다")
        }
        this.number /= operand
    }
}