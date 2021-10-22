package com.subsidian.emvcardmanager.exceptions

import java.lang.Exception

class ISOClientException : Exception {
    constructor(message: String?) : super(message) {}
    constructor(e: Exception?) : super(e) {}
}