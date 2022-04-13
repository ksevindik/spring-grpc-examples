package com.example.grpcdemo.paging

import com.example.BookServiceGrpc
import com.example.BookServiceOuterClass
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.beans.factory.annotation.Autowired

@GrpcService
class BookServiceGrpcController : BookServiceGrpc.BookServiceImplBase() {

    @Autowired
    private lateinit var bookRepository: BookRepository

    override fun getBooks(
        request: BookServiceOuterClass.ListBookRequest,
        responseObserver: StreamObserver<BookServiceOuterClass.ListBookResponse>
    ) {
        val booksPage = bookRepository.findAll(from(request.pageRequest))
        val books = booksPage.content.map {
            BookServiceOuterClass.Book.newBuilder().setTitle(it.title).setAuthor(it.author).build()
        }.toMutableList()
        val page = from(booksPage)
        val response = BookServiceOuterClass.ListBookResponse
            .newBuilder().addAllBook(books).setPage(page).build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}