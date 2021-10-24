package com.subsidian.emvcardmanager.exceptions

import java.lang.Exception

class KeyException : Exception {
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}