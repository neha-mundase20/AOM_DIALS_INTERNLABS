package com.example.aom_dials_app.apis

data class Orders(
    val GP: Int,
    val NP: Int,
    val __v: Int,
    val _id: String,
    val copper: Int,
    val department: String,
    val dialPicture: String,
    val extraQty: Int,
    val feature1BaseFeature: String,
    val feature2Extra: String,
    val feature3Mechanical: String,
    val material: String,
    val modelNo: Int,
    val numberType: String,
    val orderDate: String,
    val orderName: String,
    val orderPicture: String,
    val otherColorIndex: Int,
    val partyName: String,
    val pkgQty: Int,
    val size: Double
)

data class orderFormat(
    val status: String,
    val orders: List<Orders>
)

data class signupRequest(
    val name : String,
    val email : String,
    val password : String,
    val password_confirmation : String,
    val userType:String,
    val tc:Boolean
)

data class signupResponse(
    val status : String,
    val message : String,
    val token : String
)

data class loginRequest(
    val email : String,
    val password : String
)

data class loginResponse(
    val status : String,
    val message : String,
    val token : String
    )

data class newOrderCreationRequest(
    val GP: Int,
    val NP: Int,
    val copper: Int,
    val department: String,
    //val dialPicture: String,
    val extraQtyPercentage: Int,
    val feature1BaseFeature: String,
    val feature2Extra: String,
    val feature3Mechanical: String,
    val material: String,
    val modelNo: Int,
    val numberType: String,
    val orderDate: String,
    val orderName: String,
    //val orderPicture: String,
    val otherColorIndex: Int,
    val partyName: String,
    val pkgQty: Int,
    val size: Double
)

data class createOrderResponse(
    val status : String,
    val message : String
)

data class orderUpdationRequest(
    val id: String,
    val GP: Int,
    val NP: Int,
    val copper: Int,
    val department: String,
    //val dialPicture: String,
    val extraQtyPercentage: Int,
    val feature1BaseFeature: String,
    val feature2Extra: String,
    val feature3Mechanical: String,
    val material: String,
    val modelNo: Int,
    val numberType: String,
    val orderDate: String,
    val orderName: String,
    //val orderPicture: String,
    val otherColorIndex: Int,
    val partyName: String,
    val pkgQty: Int,
    val size: Double
)

data class updateOrderResponse(
    val status : String,
    val message : String
)

data class orderDeletionResponse(
    val status : String,
    val message : String
)

data class loggedUser(
    val _id : String,
    val name : String,
    val email : String,
    val userType : String,
    val tc : Boolean,
    val __v: Int
)


