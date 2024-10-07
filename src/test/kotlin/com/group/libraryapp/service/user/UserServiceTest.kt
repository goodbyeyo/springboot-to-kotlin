package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun saveUserTest() {
        val request = UserCreateRequest("wook", 24)
        userService.saveUser(request)
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].age).isEqualTo(24)
        assertThat(results[0].name).isEqualTo("wook")
    }

    @Test
    fun saveUserAgeNullTest() {
        val request = UserCreateRequest("yoo", null)
        userService.saveUser(request)
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("yoo")
        assertThat(results[0].age).isNull()
    }


    @Test
    fun getUserTest() {
        userRepository.saveAll(listOf(
                User("person1", 20),
                User("person2", null),
                User("person3", 34),
        ))
        val users = userService.findUsers()
        assertThat(users).hasSize(3)
        assertThat(users).extracting("name")
                .containsExactlyInAnyOrder("person1", "person2", "person3")
        assertThat(users).extracting("age")
                .containsExactlyInAnyOrder(20, 34, null)
    }

    @Test
    fun updateUserNameTest() {
        val savedUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUser.id, "B", )

        userService.updateUserName(request)

        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    fun deleteUserTest() {
        val savedUser = userRepository.save(User("A", null))
        assertThat(userRepository.findAll()).hasSize(1)

        userService.deleteUser(savedUser.name)

        assertThat(userRepository.findAll()).hasSize(0)
    }

}