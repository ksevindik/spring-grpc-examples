package com.example.grpcdemo.paging

import com.example.PageModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import kotlin.streams.toList

fun from(pageable: Pageable) : PageModel.PageRequest{
    val orders = pageable.sort.stream().map {
        PageModel.Order.newBuilder()
            .setDirection(PageModel.Direction.forNumber(it.direction.ordinal)).setProperty(it.property)
            .build()
    }.toList().toMutableList()
    return PageModel.PageRequest.newBuilder()
        .setPage(pageable.pageNumber)
        .setSize(pageable.pageSize)
        .setSort(PageModel.Sort.newBuilder().addAllOrder(orders).build())
        .build()
}

fun from(pageRequest:PageModel.PageRequest) : Pageable {
    val orders = pageRequest.sort.orderList.map {
        Sort.Order(Sort.Direction.values()[it.directionValue],it.property)
    }.toMutableList()
    return PageRequest.of(pageRequest.page,pageRequest.size,Sort.by(orders))
}

fun from(page:Page<out Any>) : PageModel.Page {
    return PageModel.Page.newBuilder()
        .setNumber(page.number).setSize(page.size)
        .setTotalPages(page.totalPages).setTotalElements(page.totalElements).build()
}

fun <T> from(page:PageModel.Page, content:List<T>) : Page<T> {
    return PageImpl<T>(content, PageRequest.of(page.number,page.size),page.totalElements)
}