package com.ymnberkay.test1.models

data class MemortCard(
    val identifier: Int ,
    var isOpen: Boolean = false,
    var isMatched: Boolean = false,
    var name: String ?= null,
    var home: String ?= null,
    var cardName: String ?= null,
    var point: Int ?= null
)