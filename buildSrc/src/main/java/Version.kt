//Version.kt will hold all the versions like this
object Version {
    // android configuration
    const val compileSdk = 32
    const val minSdk = 21
    const val targetSdk = 32
    const val versionCode = 1
    const val versionName = "1.0"

    const val jvmTarget = "1.8"

    //Libraries
    object AndroidX {
        const val core = "1.8.0"
        const val activity = "1.5.1"
        const val appcompat = "1.5.0"
        const val constraintLayout = "2.1.4"
    }
    const val material = "1.6.1"

    object Coroutine {
        const val core = "1.6.4"
        const val android = "1.6.4"
    }

    object Squareup {
        const val loggingInterceptor = "4.10.0"
        const val okhttp = "4.10.0"
        const val converterGson = "2.9.0"
        const val retrofit = "2.9.0"
    }

    const val coil = "2.2.0"

    object UnitTest {
        const val junit = "4.13.2"
        const val androidJUnit = "1.1.3"
        const val espressoCore = "3.4.0"
        const val robolectric = "4.8"
    }
}
