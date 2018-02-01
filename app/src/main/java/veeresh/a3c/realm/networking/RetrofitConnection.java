package veeresh.a3c.realm.networking;


import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import veeresh.a3c.realm.utilities.AppConstants;


public class RetrofitConnection {

    private static final String TAG = RetrofitConnection.class.getSimpleName();

    public static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .build();
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(message -> Log.d(TAG, message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public static APIService getService() {
        return provideRetrofit().create(APIService.class);
    }

}