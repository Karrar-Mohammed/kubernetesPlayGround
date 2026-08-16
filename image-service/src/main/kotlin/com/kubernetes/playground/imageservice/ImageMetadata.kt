package com.kubernetes.playground.imageservice

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "user_images")
data class ImageMetadata(
    @Id
    val userId: Long,
    val filename: String,
    val uploadedAt: Instant = Instant.now()
)
