package com.subsidian.emvcardmanager.interfaces

import javax.net.ssl.KeyManager

interface SSLKeyManagers {
    fun setKeyManagers(keyManagers: Array<KeyManager>): SSLTrustManagers
}