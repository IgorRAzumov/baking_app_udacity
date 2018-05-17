package udacity.com.baking_app.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static RetrofitHelper RETROFIT_HELPER;
    private final BakingApi bakingApiService;

    private RetrofitHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bakingApiService = retrofit.create(BakingApi.class);
    }

    public BakingApi getBakingApiService() {
        return bakingApiService;
    }

    public static RetrofitHelper getRetrofitHelper() {
        if (RETROFIT_HELPER == null) {
            RETROFIT_HELPER = new RetrofitHelper();
        }
        return RETROFIT_HELPER;
    }
}
