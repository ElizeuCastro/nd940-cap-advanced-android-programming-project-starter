package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CivicsHttpClient : OkHttpClient() {

    companion object {

        private const val API_KEY = "AIzaSyDGuPct57P4jwNxTxB7TCmua7anA-_0RkY"

        fun getClient(): OkHttpClient {

            return Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url
                                .newBuilder()
                                .addQueryParameter("key", API_KEY)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }
                    .addInterceptor(getLoggingInterceptor())
                    .build()
        }

        private fun getLoggingInterceptor() : HttpLoggingInterceptor {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            return logging
        }

    }

}