package com.example.grpcdemo.paging

import com.example.BookServiceGrpc
import com.example.BookServiceOuterClass
import com.example.PageModel
import net.devh.boot.grpc.client.inject.GrpcClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@TestPropertySource(properties = [
    "grpc.server.port=-1",
    "grpc.server.in-process-name=test",
    "grpc.client.bookService.address=in-process:test"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookServiceGrpcControllerIntegrationTests {
    @GrpcClient("bookService")
    private lateinit var grpcClient:BookServiceGrpc.BookServiceBlockingStub

    @Autowired
    private lateinit var bookRepository: BookRepository

    @BeforeAll
    fun setUp() {
        val books = mutableListOf<Book>()
        for(i in 1..18) {
            val suffix = if(i>9) "$i" else "0$i"
            books.add(Book("Book_$suffix","Author_$suffix"))
        }
        bookRepository.saveAll(books)
    }

    @AfterAll
    fun tearDown() {
        bookRepository.deleteAll()
    }

    @Test
    fun `it should return books in pages`() {
        val pageSize = 5
        for(pageNumber in 0..3)
            checkBookResponse(pageNumber,pageSize,grpcClient.getBooks(createListBookRequest(pageNumber,pageSize)))
    }

    private fun createListBookRequest(page:Int, size:Int) : BookServiceOuterClass.ListBookRequest {
        val order = PageModel.Order.newBuilder()
            .setDirection(PageModel.Direction.DESC)
            .setProperty("title").build()
        val sort = PageModel.Sort.newBuilder().addOrder(order).build()
        val pageRequest = PageModel.PageRequest.newBuilder().setPage(page).setSize(size).setSort(sort).build()
        return BookServiceOuterClass.ListBookRequest.newBuilder().setPageRequest(pageRequest).build()
    }

    private fun checkBookResponse(pageNumber:Int, pageSize:Int,
                                  listBookResponse: BookServiceOuterClass.ListBookResponse) {
        val books = listBookResponse.bookList
        val page = listBookResponse.page
        val expectedPage = PageModel.Page.newBuilder()
            .setSize(pageSize)
            .setNumber(pageNumber)
            .setTotalPages(4)
            .setTotalElements(18).build()
        assertEquals(expectedPage,page)
        if(pageNumber == 3) {
            assertContentEquals(listOf(
                book(3),
                book(2),
                book(1)),books)
        } else {
            val offset = pageNumber * pageSize
            assertContentEquals(listOf(
                book(18-offset),
                book(17-offset),
                book(16-offset),
                book(15-offset),
                book(14-offset)),books)
        }
    }

    private fun book(i:Int) : BookServiceOuterClass.Book {
        val suffix = if(i>9) "$i" else "0$i"
        return BookServiceOuterClass.Book.newBuilder().setTitle("Book_$suffix").setAuthor("Author_$suffix").build()
    }
}