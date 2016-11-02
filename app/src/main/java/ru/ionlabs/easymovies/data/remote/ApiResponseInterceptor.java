package ru.ionlabs.easymovies.data.remote;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class ApiResponseInterceptor implements Interceptor {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Gson GSON = new Gson();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        final ResponseBody body = response.body();
        ApiResponse apiResponse = GSON.fromJson(body.string(), ApiResponse.class);
        final Response.Builder newResponse = response.newBuilder()
                .body(ResponseBody.create(JSON, apiResponse.results.toString()));
        return newResponse.build();
    }
}
