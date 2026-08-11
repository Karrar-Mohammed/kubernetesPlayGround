package com.kubernetes.playground.imageservice

import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<ImageMetadata, Long> {
    fun findTopByUserIdOrderByUploadedAtDesc(userId: String): ImageMetadata?
}
