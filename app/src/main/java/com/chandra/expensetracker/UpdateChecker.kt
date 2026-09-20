package com.chandra.expensetracker

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class SemanticVersion(val major: Int, val minor: Int, val patch: Int) : Comparable<SemanticVersion> {
    override fun compareTo(other: SemanticVersion): Int {
        if (this.major != other.major) return this.major.compareTo(other.major)
        if (this.minor != other.minor) return this.minor.compareTo(other.minor)
        return this.patch.compareTo(other.patch)
    }

    companion object {
        fun parse(versionStr: String): SemanticVersion {
            val cleanStr = versionStr.trim().removePrefix("v").removePrefix("V")
            val parts = cleanStr.split(".").mapNotNull { segment ->
                segment.takeWhile { it.isDigit() }.toIntOrNull()
            }
            val major = parts.getOrNull(0) ?: 0
            val minor = parts.getOrNull(1) ?: 0
            val patch = parts.getOrNull(2) ?: 0
            return SemanticVersion(major, minor, patch)
        }
    }
}

sealed class UpdateResult {
    object Loading : UpdateResult()
    data class UpToDate(val installedVersion: String, val latestVersion: String) : UpdateResult()
    data class UpdateAvailable(
        val installedVersion: String,
        val latestVersion: String,
        val downloadUrl: String,
        val releaseNotes: String
    ) : UpdateResult()
    data class Error(val message: String) : UpdateResult()
}

object UpdateChecker {

    private const val GITHUB_REPO_OWNER = "chandra1234456"
    private const val GITHUB_REPO_NAME = "Expense"
    private const val GITHUB_LATEST_RELEASE_URL = "https://api.github.com/repos/$GITHUB_REPO_OWNER/$GITHUB_REPO_NAME/releases/latest"
    private const val GITHUB_ALL_RELEASES_URL = "https://api.github.com/repos/$GITHUB_REPO_OWNER/$GITHUB_REPO_NAME/releases"
    private const val GITHUB_RAW_BUILD_GRADLE_MAIN = "https://raw.githubusercontent.com/$GITHUB_REPO_OWNER/$GITHUB_REPO_NAME/main/app/build.gradle.kts"
    private const val GITHUB_RAW_BUILD_GRADLE_MASTER = "https://raw.githubusercontent.com/$GITHUB_REPO_OWNER/$GITHUB_REPO_NAME/master/app/build.gradle.kts"

    fun getInstalledVersion(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "1.0.0"
        } catch (_: Exception) {
            "1.0.0"
        }
    }

    suspend fun checkForUpdates(context: Context): UpdateResult = withContext(Dispatchers.IO) {
        val installedVersionStr = getInstalledVersion(context)
        val installedVersion = SemanticVersion.parse(installedVersionStr)

        var latestVersionStr = installedVersionStr
        var downloadUrl = "https://github.com/$GITHUB_REPO_OWNER/$GITHUB_REPO_NAME/releases"
        var releaseNotes = "A new version of Expense Tracker is available on GitHub."

        var foundRelease = false

        // 1. Try GitHub Releases API (/releases/latest or /releases)
        try {
            val releaseObj = fetchGitHubReleaseJson()
            if (releaseObj != null) {
                val tagName = releaseObj.optString("tag_name", "")
                if (tagName.isNotBlank()) {
                    latestVersionStr = tagName.removePrefix("v").removePrefix("V")
                    releaseNotes = releaseObj.optString("body", releaseNotes).ifBlank { releaseNotes }
                    val htmlUrl = releaseObj.optString("html_url", downloadUrl)
                    downloadUrl = htmlUrl

                    val assets = releaseObj.optJSONArray("assets")
                    if (assets != null && assets.length() > 0) {
                        for (i in 0 until assets.length()) {
                            val asset = assets.getJSONObject(i)
                            val name = asset.optString("name", "")
                            if (name.endsWith(".apk", ignoreCase = true)) {
                                downloadUrl = asset.optString("browser_download_url", htmlUrl)
                                break
                            }
                        }
                    }
                    foundRelease = true
                }
            }
        } catch (_: Exception) { }

        // 2. Fallback check: Read versionName from GitHub raw build.gradle.kts (main/master branch)
        if (!foundRelease || SemanticVersion.parse(latestVersionStr) <= installedVersion) {
            val rawVersion = fetchVersionFromRawGradle()
            if (rawVersion != null) {
                val rawSemantic = SemanticVersion.parse(rawVersion)
                val currentLatestSemantic = SemanticVersion.parse(latestVersionStr)
                if (rawSemantic > currentLatestSemantic) {
                    latestVersionStr = rawVersion
                    downloadUrl = "https://github.com/$GITHUB_REPO_OWNER/$GITHUB_REPO_NAME/releases"
                    releaseNotes = "New version v$rawVersion pushed to GitHub repository."
                }
            }
        }

        val latestSemantic = SemanticVersion.parse(latestVersionStr)

        if (latestSemantic > installedVersion) {
            UpdateResult.UpdateAvailable(
                installedVersion = installedVersionStr,
                latestVersion = latestVersionStr,
                downloadUrl = downloadUrl,
                releaseNotes = releaseNotes
            )
        } else {
            UpdateResult.UpToDate(
                installedVersion = installedVersionStr,
                latestVersion = latestVersionStr
            )
        }
    }

    private fun fetchGitHubReleaseJson(): JSONObject? {
        // Try latest release endpoint
        val latestObj = fetchJsonObject(GITHUB_LATEST_RELEASE_URL)
        if (latestObj != null && latestObj.has("tag_name")) {
            return latestObj
        }

        // Try releases array endpoint
        val arrayStr = fetchString(GITHUB_ALL_RELEASES_URL)
        if (!arrayStr.isNullOfEmptyJson()) {
            try {
                val array = JSONArray(arrayStr)
                if (array.length() > 0) {
                    return array.getJSONObject(0)
                }
            } catch (_: Exception) { }
        }

        return null
    }

    private fun fetchVersionFromRawGradle(): String? {
        val contentMain = fetchString(GITHUB_RAW_BUILD_GRADLE_MAIN)
        val versionFromMain = parseVersionFromGradleContent(contentMain)
        if (versionFromMain != null) return versionFromMain

        val contentMaster = fetchString(GITHUB_RAW_BUILD_GRADLE_MASTER)
        return parseVersionFromGradleContent(contentMaster)
    }

    private fun parseVersionFromGradleContent(content: String?): String? {
        if (content.isNullOrBlank()) return null
        val regex = Regex("""versionName\s*=\s*"([^"]+)"""")
        val match = regex.find(content)
        return match?.groupValues?.getOrNull(1)
    }

    private fun fetchJsonObject(urlString: String): JSONObject? {
        val str = fetchString(urlString) ?: return null
        return try {
            JSONObject(str)
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchString(urlString: String): String? {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json, text/plain")
            connection.setRequestProperty("User-Agent", "ExpenseTracker-AndroidApp")
            connection.connectTimeout = 6000
            connection.readTimeout = 6000

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun String?.isNullOfEmptyJson(): Boolean {
        if (this.isNullOrBlank()) return true
        return this.trim() == "[]" || this.trim() == "{}"
    }
}
