package com.subsidian.emvcardmanager.interfaces

import com.subsidian.emvcardmanager.builders.ISOClientBuilder.ClientBuilder
import javax.net.ssl.TrustManager

interface SSLTrustManagers {
    fun setTrustManagers(trustManagers: Array<TrustManager?>): ClientBuilder
}