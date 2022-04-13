package com.example.grpcdemo.hello

import com.example.HelloServiceGrpc
import com.example.HelloServiceOuterClass
import com.example.grpcdemo.hello.HelloServiceAdapter
import com.example.grpcdemo.hello.HelloServiceAdapter2
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals

@GrpcService
@ConditionalOnProperty(name = ["testHelloService.enabled"],havingValue = "true",matchIfMissing = false)
class TestHelloService : HelloServiceGrpc.HelloServiceImplBase() {
    override fun sayHello(
        request: HelloServiceOuterClass.SayHelloRequest,
        responseObserver: StreamObserver<HelloServiceOuterClass.SayHelloResponse>
    ) {
        val response = HelloServiceOuterClass.SayHelloResponse.newBuilder()
            .setReply("Hello ${request.name}!").build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}

@SpringBootTest
@TestPropertySource(properties = [
    "grpc.server.port=-1",
    "grpc.server.enable-keep-alive=false",
    "grpc.server.inProcessName=helloService",
    "grpc.client.helloService.address=in-process:helloService",
    "testHelloService.enabled=true",
    "helloServiceGrpcController.enabled=false"])
class HelloServiceAdapterIntegrationTests {
    @Autowired
    private lateinit var helloServiceAdapter: HelloServiceAdapter

    @Autowired
    private lateinit var helloServiceAdapter2: HelloServiceAdapter2

    @Test
    fun `it should say hello`() {
        val response = helloServiceAdapter.sayHello("John")
        assertEquals("Hello John!",response)
    }
}
