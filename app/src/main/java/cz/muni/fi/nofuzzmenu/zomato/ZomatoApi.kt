package cz.muni.fi.nofuzzmenu.zomato

import cz.muni.fi.nofuzzmenu.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ZomatoApi(val apiKey: String) {
    val service: ZomatoApiService

    companion object {
        const val protocol = "https://"
        const val hostname = "developers.zomato.com"
        const val apiVersion = "api/v2.1"
    }

    init {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }

        val client = builder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("$protocol/$hostname/$apiVersion/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ZomatoApiService::class.java)
    }
}