package com.example.grpcdemo.paging

import com.example.PageModel
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import kotlin.test.assertEquals

class PageModelFunctionsTests {
    @Test
    fun `it should return PageModel_PageRequest when given Pageable has both page and size`() {
        val pageRequest = PageRequest.of(5,20)
        val actualPageRequest = from(pageRequest)
        val expectedPageRequest = PageModel.PageRequest.newBuilder()
            .setPage(5).setSize(20).setSort(PageModel.Sort.getDefaultInstance()).build()
        assertEquals(expectedPageRequest,actualPageRequest)
    }

    @Test
    fun `it should return PageModel_PageRequest when given Pageable has only size`() {
        val pageRequest = PageRequest.ofSize(20)
        val actualPageRequest = from(pageRequest)
        val expectedPageRequest = PageModel.PageRequest.newBuilder()
            .setPage(0).setSize(20).setSort(PageModel.Sort.getDefaultInstance()).build()
        assertEquals(expectedPageRequest,actualPageRequest)
    }

    @Test
    fun `it should return PageModel_PageRequest when given Pageable has page, size and sort with several properties`() {
        val pageRequest = PageRequest.of(5,20,
            Sort.by(listOf(
                Sort.Order(Sort.Direction.ASC,"foo"),
                Sort.Order(Sort.Direction.DESC,"bar"))))
        val actualPageRequest = from(pageRequest)
        val sort = PageModel.Sort.newBuilder()
            .addOrder(PageModel.Order.newBuilder()
                        .setDirectionValue(PageModel.Direction.ASC_VALUE).setProperty("foo").build())
            .addOrder(PageModel.Order.newBuilder()
                        .setDirectionValue(PageModel.Direction.DESC_VALUE).setProperty("bar").build()).build()
        val expectedPageRequest = PageModel.PageRequest.newBuilder()
            .setPage(5).setSize(20).setSort(sort).build()
        assertEquals(expectedPageRequest,actualPageRequest)
    }

    @Test
    fun `it should return Pageable when given PageModel_PageRequest has only size`() {
        val pageRequest = PageModel.PageRequest.newBuilder().setSize(20).build()
        val actualPageable = from(pageRequest)
        val expectedPageable = PageRequest.ofSize(20)
        assertEquals(expectedPageable,actualPageable)
    }

    @Test
    fun `it should return Pageable when given PageModel_PageRequest has both page and size`() {
        val pageRequest = PageModel.PageRequest.newBuilder().setPage(5).setSize(20).build()
        val actualPageable = from(pageRequest)
        val expectedPageable = PageRequest.of(5,20)
        assertEquals(expectedPageable,actualPageable)
    }

    @Test
    fun `it should return Pageable when given PageModel_PageRequest has page, size and sort with several properties`() {
        val sort = PageModel.Sort.newBuilder()
            .addOrder(PageModel.Order.newBuilder()
                .setDirectionValue(PageModel.Direction.ASC_VALUE).setProperty("foo").build())
            .addOrder(PageModel.Order.newBuilder()
                .setDirectionValue(PageModel.Direction.DESC_VALUE).setProperty("bar").build()).build()
        val pageRequest = PageModel.PageRequest.newBuilder()
            .setPage(5).setSize(20).setSort(sort).build()
        val actualPageable = from(pageRequest)
        val expectedPageable = PageRequest.of(5,20,
            Sort.by(Sort.Order.asc("foo"),Sort.Order.desc("bar")))
        assertEquals(expectedPageable,actualPageable)
    }

    @Test
    fun `it should return PageModel_Page for the given Page`() {
        val page = PageImpl<String>(listOf("a","b","c","d","e"),PageRequest.of(4,20),100)
        val actualPage = from(page)
        val expectedPage = PageModel.Page.newBuilder()
            .setNumber(4).setSize(20).setTotalPages(5).setTotalElements(100).build()
        assertEquals(expectedPage,actualPage)
    }

    @Test
    fun `it should return Page for the given PageModel_Page`() {
        val page = PageModel.Page.newBuilder().setNumber(4).setSize(20).setTotalPages(5).setTotalElements(100).build()
        val content = listOf("a","b","c","d","e")
        val actualPage = from(page,content)
        val expectedPage = PageImpl(content,PageRequest.of(4,20),100)
        assertEquals(expectedPage,actualPage)
    }
}