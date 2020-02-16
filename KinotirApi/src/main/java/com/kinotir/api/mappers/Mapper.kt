package com.kinotir.api.mappers

interface Mapper<From, To> {
    fun map(src: From): To
}
