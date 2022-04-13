package com.example.grpcdemo.hello

import com.example.HelloServiceGrpc
import com.example.HelloServiceOuterClass
import com.example.grpcdemo.hello.HelloService
import net.devh.boot.grpc.client.inject.GrpcClient
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals

@SpringBootTest
@TestPropertySource(properties = [
    "grpc.server.port=-1",
    "grpc.server.enable-keep-alive=false",
    "grpc.server.in-process-name=helloService2",
    "grpc.client.helloService2.address=in-process:helloService2"])
class HelloServiceGrpcControllerIntegrationTests {
    @MockBean
    private lateinit var helloService: HelloService

    @GrpcClient("helloService2")
    private lateinit var grpcClient: HelloServiceGrpc.HelloServiceBlockingStub

    @Test
    fun `it should say hello`() {
        val name = "John"
        Mockito.doReturn("Hello $name!").`when`(helloService).sayHello(name)
        val request = HelloServiceOuterClass.SayHelloRequest.newBuilder().setName(name).build()
        val response = grpcClient.sayHello(request)
        assertEquals("Hello John!",response.reply)
    }
}
