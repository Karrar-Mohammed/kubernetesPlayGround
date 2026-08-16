package com.kubernetes.playground.imageservice

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ImageRepository : JpaRepository<ImageMetadata, Long> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM user_info WHERE id = :userId)", nativeQuery = true)
    fun userExists(@Param("userId") userId: Long): Boolean
}
