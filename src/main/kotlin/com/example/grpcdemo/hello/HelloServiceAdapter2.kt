package com.example.grpcdemo.hello

import com.example.HelloServiceGrpc
import com.example.HelloServiceOuterClass
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class HelloServiceAdapter2 {

    @GrpcClient("helloService")
    private lateinit var clientStub: HelloServiceGrpc.HelloServiceBlockingStub

    fun sayHello(name:String) : String  {
        val request = HelloServiceOuterClass.SayHelloRequest.newBuilder().setName(name).build()
        val response = clientStub.sayHello(request)
        return response.reply
    }
}
