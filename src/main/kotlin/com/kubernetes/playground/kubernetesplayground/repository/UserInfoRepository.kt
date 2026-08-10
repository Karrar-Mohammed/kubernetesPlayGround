package com.kubernetes.playground.kubernetesplayground.repository

import com.kubernetes.playground.kubernetesplayground.model.UserInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserInfoRepository : JpaRepository<UserInfo, Long>
