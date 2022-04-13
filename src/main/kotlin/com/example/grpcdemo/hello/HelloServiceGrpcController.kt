package com.example.grpcdemo.hello

import com.example.HelloServiceGrpc
import com.example.HelloServiceOuterClass
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@GrpcService
@ConditionalOnProperty(name = ["helloServiceGrpcController.enabled"],havingValue = "true",matchIfMissing = true)
class HelloServiceGrpcController : HelloServiceGrpc.HelloServiceImplBase() {

    @Autowired
    private lateinit var helloService: HelloService

    override fun sayHello(
        request: HelloServiceOuterClass.SayHelloRequest,
        responseObserver: StreamObserver<HelloServiceOuterClass.SayHelloResponse>
    ) {
        val response = HelloServiceOuterClass.SayHelloResponse.newBuilder()
            .setReply(helloService.sayHello(request.name)).build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
