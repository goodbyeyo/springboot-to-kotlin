package com.group.libraryapp.service

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository

@SpringBootTest
class CleaningSpringBootTest {

    @Autowired
    private lateinit var repository: List<JpaRepository<*, *>>

    @AfterEach
    fun clean() {
        val currentTimeMillis = System.currentTimeMillis()
        repository.forEach { it.deleteAll() }
        println("소요 시간 : ${System.currentTimeMillis() - currentTimeMillis}")
    }
}