package com.example.cakeapp.todo.data

data class Cake(
    val _id: String,
    val userId: String,
    var name: String,
    var countertops: String,
    var cream: String,
    var amount: Number,
    var design: Boolean

) {
    override fun toString(): String = name
}
