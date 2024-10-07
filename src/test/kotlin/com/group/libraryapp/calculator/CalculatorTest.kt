package com.group.libraryapp.calculator

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

fun main() {
    val test = CalculatorTest()
    test.addTest()
    test.minusTest()
}
class CalculatorTest {
    fun addTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.add(3)
//        val expected = Calculator(8)
//        if (calculator != expected) {
//            throw IllegalArgumentException()
//        }

        // then
        if (calculator.number != 8) {
            throw IllegalArgumentException()
        }
    }

    fun minusTest() {
        // given
        val calculator = Calculator(6)

        // when
        calculator.minus(4)

        //then
        if (calculator.number != 2) {
            throw IllegalArgumentException()
        }
    }

    fun multiplyTest() {
        val calculator = Calculator(4)
        calculator.multiply(8)
        if (calculator.number != 32) {
            throw IllegalArgumentException()
        }
    }

    fun devideTest() {
        val calculator = Calculator(10)
        calculator.divide(2)
        if (calculator.number != 5) {
            throw IllegalArgumentException()
        }
    }

    fun devideExceptionTest() {
        val calculator = Calculator(10)
        try {
            calculator.divide(0)
        } catch(e: IllegalArgumentException) {
            return
        } catch(e: Exception) {
            throw IllegalArgumentException()
        }
        throw IllegalArgumentException("기대하는 예외가 발생하지 않음")
    }

}