package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.User

// DTO class 는 data class 를 추천(for debugging)
data class UserResponse(
    val id: Long,
    val name: String,
    val age: Int?,
) {
    // 정적 팩터리 메서드 사용
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                name = user.name,
                age = user.age
            )
        }
    }
    // 부 생성자 사용
//   constructor(user: User): this (
//       id = user.id!!,
//       name = user.name,
//       age = user.age
//   )
}