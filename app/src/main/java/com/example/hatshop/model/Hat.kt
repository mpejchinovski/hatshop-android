package com.example.hatshop.model

data class Hat(var id: String ?= null, var name: String ?= null, var price: Int ?= null, var stock: Int? = null, var downloadURL: String ?= null )