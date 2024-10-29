package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {

    @AfterEach
    fun clean() {
        println("클린 시작")
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 저장 정상 동작")
    fun saveUserTest() {
        val request = UserCreateRequest("wook", 24)
        userService.saveUser(request)
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].age).isEqualTo(24)
        assertThat(results[0].name).isEqualTo("wook")
    }

    @Test
    @DisplayName("유저 저장 시 나이가 없어도 정상 동작")
    fun saveUserAgeNullTest() {
        val request = UserCreateRequest("yoo", null)
        userService.saveUser(request)
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("yoo")
        assertThat(results[0].age).isNull()
    }


    @Test
    @DisplayName("유저 조회 정상 동작")
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
    @DisplayName("유저 업데이트 정상 동작")
    fun updateUserNameTest() {
        val savedUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUser.id!!, "B")

        userService.updateUserName(request)

        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제 정상 동작")
    fun deleteUserTest() {
        val savedUser = userRepository.save(User("A", null))
        assertThat(userRepository.findAll()).hasSize(1)

        userService.deleteUser(savedUser.name)

        assertThat(userRepository.findAll()).hasSize(0)
    }

    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함")
    fun getUserLoanHistoriesEmptyTest() {
        userRepository.save(User("wook", null))
        val results = userService.getUserLoanHistories()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("wook")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 유저도 정상 응답")
    fun getUserLoanHistoriesManyBooksTest() {
        val savedUser = userRepository.save(User("wook", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser, "죄와벌", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser, "파우스트", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser, "노인과바다", UserLoanStatus.RETURNED),
            )
        )
        val results = userService.getUserLoanHistories()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("wook")
        assertThat(results[0].books).extracting("name")
            .containsExactlyInAnyOrder("죄와벌", "파우스트", "노인과바다")
        assertThat(results[0].books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)
    }

    @Test
    @DisplayName("대출 기록 현황 전체 테스트")
    fun getUserLoanHistoriesBooksAllTest() {
        val savedUser = userRepository.saveAll(listOf(
            User("wook", null),
            User("yoon", null)
        ))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser[0], "죄와벌", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser[0], "파우스트", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser[0], "노인과바다", UserLoanStatus.RETURNED),
            )
        )
        val results = userService.getUserLoanHistories()

        assertThat(results).hasSize(2)
        val userFirstResult = results.first { it.name == "wook" }
        assertThat(userFirstResult.books).hasSize(3)
        assertThat(userFirstResult.books).extracting("name")
            .containsExactlyInAnyOrder("죄와벌", "파우스트", "노인과바다")
        assertThat(userFirstResult.books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)
        assertThat(results[1].books).isEmpty()
    }
}