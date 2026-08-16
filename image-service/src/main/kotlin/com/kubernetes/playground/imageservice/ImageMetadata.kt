package com.kubernetes.playground.imageservice

import jakarta.persistence.*
import org.springframework.data.domain.Persistable
import java.time.Instant

@Entity
@Table(name = "user_images")
data class ImageMetadata(
    @Id
    val userId: Long,
    val filename: String,
    val uploadedAt: Instant = Instant.now(),
    @Transient
    private val isNewEntity: Boolean = true
) : Persistable<Long> {
    override fun getId(): Long = userId
    override fun isNew(): Boolean = isNewEntity
}
