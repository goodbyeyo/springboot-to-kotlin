package com.group.libraryapp.controller.user

import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.service.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/user")
    fun saveUser(@RequestBody request: UserCreateRequest) {
        return userService.saveUser(request)
    }

    @GetMapping("/user")
    fun getUsers(): List<UserResponse> = userService.findUsers()
//    fun getUsers(): List<UserResponse> {
//        return userService.findUsers()
//    }

    @PutMapping("/user")
    fun updateUserName(@RequestBody request: UserUpdateRequest) {
        userService.updateUserName(request)
    }

    @DeleteMapping("/user")
    fun deleteUser(@RequestBody name: String) {
        userService.deleteUser(name)
    }

    @GetMapping("/user/loan")
    fun getUserLoanHistories(): List<UserLoanHistoryResponse> {
        return userService.getUserLoanHistories()
    }
}