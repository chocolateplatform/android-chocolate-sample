# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/jesnia/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class com.vdopia.ads.lw.VPAIDPlayer.JavaScriptInterface {
   public *;
}

#Vdopia
-keep class com.vdopia.ads.lw.** {*;}
-keep interface com.vdopia.ads.lw.** {*;}
-keepattributes Exceptions, MethodParameters
-keepattributes LocalVariableTable,LocalVariableTypeTable
-keepparameternames

# Preserve all annotations.
-keepattributes *Annotation*

-keep public class * {
    public protected *;
}
-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
}


-keepattributes SourceFile,LineNumberTable

-keep class com.android.** {*;}
-keep interface com.android.** {*;}
-keep class com.google.android.** {*;}
-keep interface com.google.android.** {*;}

-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
     public *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
     public *;
}
# skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**


-dontwarn com.moat.**
# skip AVID classes
-keep class com.integralads.avid.library.* {*;}


-keep class javax.inject.*

# GreenRobot
-dontwarn de.greenrobot.event.util.**

# RxJava
-dontwarn rx.internal.util.unsafe.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
   rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keep class rx.schedulers.Schedulers { public static <methods>; }
-keep class rx.schedulers.ImmediateScheduler { public <methods>; }
-keep class rx.schedulers.TestScheduler { public <methods>; }
-keep class rx.schedulers.Schedulers { public static ** test(); }


# Retrofit
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8


# For removing warnings due to lack of Multi-Window support
-dontwarn android.app.Activity


#Google IMA
#https://www.vmax.com/2016/08/09/google-ima-integration-using-android-sdk
-keep class com.google.** { *; }
-keep interface com.google.** { *; }
-keep class com.google.ads.interactivemedia.v3.api.** { *;}
-keep interface com.google.ads.interactivemedia.** { *; }
-dontwarn com.google.ads.interactivemedia.v3.api.**

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient


# Keep JavascriptInterface for WebView bridge
-keepattributes JavascriptInterface

# Sometimes keepattributes is not enough to keep annotations
-keep class android.webkit.JavascriptInterface {
   *;
}


-keep class com.moat.** { *; }
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
@com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}

-dontwarn com.vdopia.ads.lw.R$id

-keep class com.devbrackets.** {*;}

-keep class com.danikula.** {*;}
-keep interface com.danikula.** {*;}


#Applovin
# Do not warn about unknown referenced methods if compiling against older Android veresions or Google Play Services Identifier APIs without reflection
-dontwarn com.applovin.**
-dontpreverify
-dontusemixedcaseclassnames
-printmapping proguard.map
-keepattributes Signature,InnerClasses,Exceptions,*Annotation*
-keep public class com.applovin.sdk.AppLovinSdk { *; }
-keep public class com.applovin.sdk.AppLovin* { public protected *; }
-keep public class com.applovin.nativeAds.AppLovin* { public protected *; }
-keep public class com.applovin.adview.* { public protected *; }
-keep public class com.applovin.mediation.* { public protected *; }
-keep public class com.applovin.mediation.ads.* { public protected *; }
-keep public class com.applovin.impl.sdk.utils.BundleUtils { public protected *; }
-keep public class com.applovin.impl.**.AppLovin* { public protected *; }
-keep public class com.applovin.impl.**.*Impl { public protected *; }
-keep public class com.applovin.impl.adview.AppLovinOrientationAwareInterstitialActivity { *; }
-keepclassmembers class com.applovin.sdk.AppLovinSdkSettings { private java.util.Map localSettings; }
-keep class com.applovin.mediation.adapters.** { *; }
-keep class com.applovin.mediation.adapter.** { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }

#Adcolony
#https://github.com/AdColony/AdColony-Android-SDK-3/wiki/Project-Setup
# For communication with AdColony's WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep ADCNative class members unobfuscated
-keepclassmembers class com.adcolony.sdk.ADCNative** {
    *;
}

-keep class com.integralads.** {*;}
-keep interface com.integralads.** {*;}
-dontwarn com.integralads.**

-keep class com.adcolony.** {*;}
-keep interface com.adcolony.** {*;}
-dontwarn com.adcolony.**

# Amazon
-keep class com.amazon.** {*;}
-keep interface com.amazon.** {*;}


# Criteo
-keep class com.criteo.**

#Facebook
#https://www.vmax.com/2016/03/14/facebook-integration-using-android-sdk
-dontwarn com.facebook.ads.internal.**
-keep class com.facebook.ads.**


# MoPub Proguard Config
# NOTE: You should also include the Android Proguard config found with the build tools:
# $ANDROID_HOME/tools/proguard/proguard-android.txt

# Keep public classes and methods.
-keepclassmembers class com.mopub.** { public *; }
-keep public class com.mopub.**
-keep public class android.webkit.JavascriptInterface {}

# Explicitly keep any custom event classes in any package.
-keep class * extends com.mopub.mobileads.CustomEventBanner {}
-keep class * extends com.mopub.mobileads.CustomEventInterstitial {}
-keep class * extends com.mopub.mobileads.CustomEventRewardedAd {}
-keep class * extends com.mopub.nativeads.CustomEventNative {}

# Keep methods that are accessed via reflection
-keepclassmembers class ** { @com.mopub.common.util.ReflectionTarget *; }

# Support for Android Advertiser ID.
-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}

# Support for Google Play Services
# http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

# End, Mopub

# Tapjoy
-keep class com.tapjoy.** { *; }

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
@com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}
-keep class com.google.android.gms.ads.identifier.** { *; }

#Unity Ads
# Keep all classes in Unity Ads package
-keep class com.unity3d.ads.** {
   *;
}

# Keep all classes in Unity Services package
-keep class com.unity3d.services.** {
   *;
}

-dontwarn com.google.ar.core.**

#Vungle
-keep class com.vungle.warren.** { *; }
-dontwarn com.vungle.warren.error.VungleError$ErrorCode


# Okio
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Gson
-keepattributes Signature
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Google Android Advertising ID
-keep class com.google.android.gms.internal.** { *; }
-dontwarn com.google.android.gms.ads.identifier.**

# Yahoo
-keep class com.flurry.** { *; }
-keep interface com.flurry.** { *; }
-dontwarn com.flurry.**