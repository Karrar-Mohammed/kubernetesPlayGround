package com.kubernetes.playground.kubernetesplayground.model

import jakarta.persistence.*

@Entity
@Table(name = "user_info")
class UserInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "age", nullable = false)
    var age: Int = 0,

    @Column(name = "first_name", nullable = false)
    var firstName: String = "",

    @Column(name = "last_name", nullable = false)
    var lastName: String = "",

    @Column(name = "place_of_birth", nullable = false)
    var placeOfBirth: String = "",

    @Column(name = "address", nullable = false)
    var address: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    var gender: Gender = Gender.Male
)
