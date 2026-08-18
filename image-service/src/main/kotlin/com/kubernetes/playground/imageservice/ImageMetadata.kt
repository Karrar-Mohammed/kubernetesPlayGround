package com.kubernetes.playground.imageservice

import jakarta.persistence.*
import org.springframework.data.domain.Persistable
import java.time.Instant

@Entity
@Table(name = "user_images")
class ImageMetadata(
    @Id
    val userId: Long,
    val filename: String,
    val uploadedAt: Instant = Instant.now()
) : Persistable<Long> {

    @Transient
    private var alreadyExists: Boolean = false

    override fun getId(): Long = userId
    override fun isNew(): Boolean = !alreadyExists

    fun markAsExisting(): ImageMetadata {
        alreadyExists = true
        return this
    }
}
