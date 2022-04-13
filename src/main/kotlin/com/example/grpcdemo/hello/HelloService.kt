package com.example.grpcdemo.hello

import org.springframework.stereotype.Service

@Service
class HelloService {
    fun sayHello(name:String):String {
        return "Hello $name!!!"
    }
}
