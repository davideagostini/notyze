package com.davideagostini.service

import com.davideagostini.data.models.User
import com.davideagostini.data.repository.user.UserRepository
import com.davideagostini.data.requests.CreateAccountRequest

class UserService(
    private val userRepository: UserRepository,
) {


    suspend fun doesUserWithEmailExist(email: String): Boolean {
        return userRepository.getUserByEmail(email) != null
    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean {
        return enteredPassword == actualPassword
    }

    suspend fun createUser(request: CreateAccountRequest) {
        userRepository.createUser(
            User(
                email = request.email,
                password = request.password
            )
        )
    }

    fun validateCreateAccountRequest(request: CreateAccountRequest): UserService.ValidationEvent {
        if (request.email.isBlank() || request.password.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.Success
    }


    sealed class ValidationEvent {
        object ErrorFieldEmpty: ValidationEvent()
        object Success: ValidationEvent()
    }
}