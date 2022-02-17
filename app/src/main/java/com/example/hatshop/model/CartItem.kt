package com.example.hatshop.model

data class CartItem(var id: String ?= null, var name: String ?= null, var downloadURL: String ?= null, var quantity: Int ?= null, var price: Int ?= null)