package ro.tuiasi.ac.ProiectPIP;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ApiClient {
    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}