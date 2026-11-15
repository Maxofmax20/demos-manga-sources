package eu.kanade.tachiyomi.extension.ar.mangastarz

import eu.kanade.tachiyomi.multisrc.madara.Madara
import keiyoushi.annotation.Source
import okhttp3.Dns
import okhttp3.OkHttpClient
import java.net.Inet4Address
import java.net.InetAddress
import java.time.format.DateTimeFormatter
import java.util.Locale

@Source
abstract class MangaStarz : Madara() {
    // Cloudflare currently advertises IPv6 endpoints that are unreachable on some mobile
    // networks. Prefer IPv4 for this source while retaining the normal DNS fallback.
    override fun OkHttpClient.Builder.configureClient(): OkHttpClient.Builder = apply {
        dns(object : Dns {
            override fun lookup(hostname: String): List<InetAddress> {
                val addresses = Dns.SYSTEM.lookup(hostname)
                if (!hostname.equals("starzmanga.net", ignoreCase = true)) return addresses
                val ipv4 = addresses.filterIsInstance<Inet4Address>()
                return ipv4.ifEmpty { addresses }
            }
        })
    }

    override val chapterDateFormat = DateTimeFormatter.ofPattern("d MMMM، yyyy", Locale.forLanguageTag("ar"))
}
