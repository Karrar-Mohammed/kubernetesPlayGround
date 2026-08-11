package com.kubernetes.playground.imageservice

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "user_images")
data class ImageMetadata(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val userId: String,
    val filename: String,
    val uploadedAt: Instant = Instant.now()
)
