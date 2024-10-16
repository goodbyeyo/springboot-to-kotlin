package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService ( // open
    private val userRepository: UserRepository,
){
}