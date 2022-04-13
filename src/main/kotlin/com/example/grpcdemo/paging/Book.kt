package com.example.grpcdemo.paging

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Book(var title:String, var author:String) {
    @Id
    @GeneratedValue
    var id:Long? = null
}