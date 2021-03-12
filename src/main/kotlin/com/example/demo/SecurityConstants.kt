package com.example.demo




object SecurityConstants {
    val login_url = "/api/authenticate"

    // Signing key for HS512 algorithm
    // You can use the page http://www.allkeysgenerator.com/ to generate all kinds of keys
    val JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y\$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf"

    // JWT token defaults
    val Authorization = "Authorization"
    val Bearer = "Bearer "
    val JWT = "JWT"
    val secure_api = "secure-api"
    val secure_app = "secure-app"
}

