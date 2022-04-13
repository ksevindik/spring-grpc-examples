package com.example.grpcdemo.hello

import com.example.HelloServiceGrpc
import com.example.HelloServiceOuterClass
import io.grpc.Channel
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class HelloServiceAdapter {
    @GrpcClient("helloService")
    private lateinit var managedChannel: Channel

    fun sayHello(name:String) : String {
        val clientStub = HelloServiceGrpc.newBlockingStub(managedChannel)
        val request = HelloServiceOuterClass.SayHelloRequest.newBuilder().setName(name).build()
        val response = clientStub.sayHello(request)
        return response.reply
    }
}
