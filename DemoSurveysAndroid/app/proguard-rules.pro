# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android SDK\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn javax.xml.**
-dontwarn com.opg.sdk.**



-dontwarn junit.framework.**
-dontwarn org.junit.**
-dontwarn android.net.http.**
-dontwarn android.test.**
#-keep junit.framework.**
-dontwarn OnePoint.Logging.LogManager
-dontwarn com.opg.main.**
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
  public *;
}
-keepattributes EnclosingMethod
-keepattributes InnerClasses

-dontwarn org.apache.log4j.**
-keep class org.apache.log4j.** { *; }

-dontwarn com.androidquery.auth.**
-keep class com.androidquery.auth.** { *; }

-keep class com.google.**
-dontwarn com.google.**

-dontwarn oauth.**
-keep class oauth.** { *; }

-dontwarn com.android.auth.TwitterHandle.**
-keep class com.android.auth.TwitterHandle.** { *; }

-dontwarn org.apache.**
-keep class org.apache.** { *; }

-keep public class OnePoint.Player.Html.UI.HtmlPage
-dontwarn OnePoint.Player.Html.UI.HtmlPage

-keep class apache.** { *; }

-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient
-keep public class android.webkit.WebView

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient


-keep class org.apache.http.auth.InvalidCredentialsException
-keep class org.apache.http.** { *; }

-dontwarn javax.annotation.**

-dontwarn org.chromium.base.multidex.**
