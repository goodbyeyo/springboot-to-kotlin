package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.UserRepositoryCustomImpl
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.fail
import com.group.libraryapp.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    // open
    private val userRepository: UserRepository,
){
    @Transactional
    fun saveUser(request: UserCreateRequest) {
        // default parameter -> mutableListOf(), null 있으므로 name, age 만 할당함
        val newUser = User(request.name, request.age)
        userRepository.save(newUser)
    }

    @Transactional(readOnly = true)
    fun findUsers(): List<UserResponse> {
        return userRepository.findAll()
            .map { user -> UserResponse.of(user) }
        // .map(::UserResponse)    // user 를 UserResponse 생성자에 넣음
        // .map { UserResponse(it) }
    }

    @Transactional
    fun updateUserName(request: UserUpdateRequest) {
        // val user = userRepository.findById(request.id).orElseThrow(::IllegalArgumentException)
        // val user = userRepository.findByIdOrNull(request.id) ?: fail()
        val user = userRepository.findByIdOrThrow(request.id)
        user.updateName(request.name)
    }

    @Transactional
    fun deleteUser(name: String) {
        // val user = userRepository.findByName(name) ?: throw IllegalArgumentException()
        // val user = userRepository.findByName(name).orElseThrow(::IllegalArgumentException)
        val user = userRepository.findByName(name) ?: fail()
        userRepository.delete(user)
    }

    @Transactional(readOnly = true)
    fun getUserLoanHistories(): List<UserLoanHistoryResponse> {
        return userRepository.findAllWithHistories().map(UserLoanHistoryResponse::of)

        // .map
        // { user ->
            // UserLoanHistoryResponse(
                // name = user.name,
                // books = user.userLoanHistories.map(BookHistoryResponse::of)
                //{ history ->
                //    BookHistoryResponse.of(history) // dto 정적 팩토리 메서드
//                    BookHistoryResponse(
//                        name = history.bookName,
//                        isReturn = history.status == UserLoanStatus.RETURNED
//                    )
//                }
//            )
//        }
    }
}