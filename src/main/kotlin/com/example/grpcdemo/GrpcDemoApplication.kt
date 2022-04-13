package com.example.grpcdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcDemoApplication

fun main(args: Array<String>) {
    runApplication<GrpcDemoApplication>(*args)
}
