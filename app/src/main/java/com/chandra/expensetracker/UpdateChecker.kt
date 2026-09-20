package com.chandra.expensetracker

import android.content.Context
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

        try {
            val url = URL(GITHUB_LATEST_RELEASE_URL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json")
            connection.setRequestProperty("User-Agent", "ExpenseTracker-AndroidApp")
            connection.connectTimeout = 8000
            connection.readTimeout = 8000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val jsonStr = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(jsonStr)

                val tagName = json.optString("tag_name", "1.0.0")
                val releaseNotes = json.optString("body", "No release notes provided.").ifBlank { "No release notes provided." }
                val htmlUrl = json.optString("html_url", "https://github.com/$GITHUB_REPO_OWNER/$GITHUB_REPO_NAME/releases")

                // Extract APK download URL from assets if present
                var apkDownloadUrl = htmlUrl
                val assets = json.optJSONArray("assets")
                if (assets != null && assets.length() > 0) {
                    for (i in 0 until assets.length()) {
                        val asset = assets.getJSONObject(i)
                        val name = asset.optString("name", "")
                        if (name.endsWith(".apk", ignoreCase = true)) {
                            apkDownloadUrl = asset.optString("browser_download_url", htmlUrl)
                            break
                        }
                    }
                }

                val latestVersion = SemanticVersion.parse(tagName)
                val cleanLatestTag = tagName.removePrefix("v").removePrefix("V")

                if (latestVersion > installedVersion) {
                    UpdateResult.UpdateAvailable(
                        installedVersion = installedVersionStr,
                        latestVersion = cleanLatestTag,
                        downloadUrl = apkDownloadUrl,
                        releaseNotes = releaseNotes
                    )
                } else {
                    UpdateResult.UpToDate(
                        installedVersion = installedVersionStr,
                        latestVersion = cleanLatestTag
                    )
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                UpdateResult.UpToDate(
                    installedVersion = installedVersionStr,
                    latestVersion = installedVersionStr
                )
            } else {
                UpdateResult.Error("Unable to fetch update info (HTTP $responseCode)")
            }
        } catch (e: Exception) {
            UpdateResult.Error(e.localizedMessage ?: "Failed to connect to GitHub. Check internet connection.")
        }
    }
}
