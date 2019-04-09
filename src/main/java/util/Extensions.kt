package util

fun String.getLastBitFromUrl(): String {
    return this.replaceFirst(".*/([^/?]+).*".toRegex(), "$1")
}