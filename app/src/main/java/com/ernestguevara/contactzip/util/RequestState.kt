package com.ernestguevara.contactzip.util

enum class RequestState(val message: String?) {
    Loading(""),
    Finished(""),
    Failed(""),
    NoInternetConnection("")
}