package com.kubernetes.playground.kubernetesplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KubernetesPlayGroundApplication

fun main(args: Array<String>) {
    runApplication<KubernetesPlayGroundApplication>(*args)
}
