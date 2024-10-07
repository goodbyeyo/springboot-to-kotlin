package com.group.libraryapp.calculator

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JunitCalculatorTest {

    @Test
    fun addTest() {
        val calculator = Calculator(10)
        calculator.add(5)
        assertThat(calculator.number).isEqualTo(15)
        // Assertions.assertEquals(15, calculator.number)
    }

    @Test
    fun minusTest() {
        val calculator = Calculator(15)
        calculator.minus(10)
        assertThat(calculator.number).isEqualTo(5)
        // Assertions.assertEquals(5, calculator.number)
    }

    @Test
    fun multiplyTest() {
        val calculator = Calculator(8)
        calculator.multiply(4)
        assertThat(calculator.number).isEqualTo(32)
        // Assertions.assertEquals(32, calculator.number)
    }

    @Test
    fun divideTest() {
        val calculator = Calculator(20)
        calculator.divide(5)
        assertThat(calculator.number).isEqualTo(4)
        // Assertions.assertEquals(4, calculator.number)
    }

    @Test
    fun divideExceptionTest() {
        val calculator = Calculator(50)

        assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.apply {
            assertThat(message).isEqualTo("0으로 나눌수 없습니다")
        }

        /*
        assertThat(
            assertThrows<IllegalArgumentException> {
                calculator.divide(0)
            }.message
        ).isEqualTo("0으로 나눌수 없습니다")
        */


    }

}