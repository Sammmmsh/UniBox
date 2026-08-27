package com.example.unibox.data.remote

import okhttp3.Dns
import java.net.InetAddress
import java.net.UnknownHostException

/** Re-checks addresses at connection time, including after a DNS change. */
class PublicNetworkDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        val addresses = Dns.SYSTEM.lookup(hostname)
        if (addresses.isEmpty() || addresses.any { !it.isPublicWebAddress() }) {
            throw UnknownHostException("Non-public addresses are not allowed for web previews")
        }
        return addresses
    }
}

internal fun InetAddress.isPublicWebAddress(): Boolean {
    if (isAnyLocalAddress || isLoopbackAddress || isLinkLocalAddress ||
        isSiteLocalAddress || isMulticastAddress
    ) return false

    val bytes = address.map { it.toInt() and 0xff }
    return when (bytes.size) {
        4 -> !(bytes[0] == 0 || bytes[0] >= 224 ||
            bytes[0] == 100 && bytes[1] in 64..127 ||
            bytes[0] == 192 && bytes[1] == 0 ||
            bytes[0] == 198 && bytes[1] in 18..19 ||
            bytes[0] == 198 && bytes[1] == 51 && bytes[2] == 100 ||
            bytes[0] == 203 && bytes[1] == 0 && bytes[2] == 113)
        16 -> !((bytes[0] and 0xfe) == 0xfc ||
            bytes[0] == 0x20 && bytes[1] == 0x01 && bytes[2] == 0x0d && bytes[3] == 0xb8)
        else -> false
    }
}
