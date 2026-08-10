package com.kubernetes.playground.kubernetesplayground.dto

import com.kubernetes.playground.kubernetesplayground.model.Gender
import com.kubernetes.playground.kubernetesplayground.model.UserInfo

data class UserInfoResponse(
    val id: Long,
    val age: Int,
    val firstName: String,
    val lastName: String,
    val placeOfBirth: String,
    val address: String,
    val gender: Gender
) {
    companion object {
        fun fromEntity(userInfo: UserInfo): UserInfoResponse {
            return UserInfoResponse(
                id = userInfo.id ?: 0,
                age = userInfo.age,
                firstName = userInfo.firstName,
                lastName = userInfo.lastName,
                placeOfBirth = userInfo.placeOfBirth,
                address = userInfo.address,
                gender = userInfo.gender
            )
        }
    }
}
