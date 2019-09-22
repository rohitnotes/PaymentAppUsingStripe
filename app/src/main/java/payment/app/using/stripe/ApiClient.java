package payment.app.using.stripe;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

class ApiClient {

    private static Retrofit retrofit = null;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builderJson
            = new Retrofit.Builder()
            .client(httpClient.build())
            . addConverterFactory(GsonConverterFactory.create());

    private static Retrofit.Builder builderString
            = new Retrofit.Builder()
            .client(httpClient.build())
            . addConverterFactory(ScalarsConverterFactory.create());

    /*
     * Create the Retrofit Instance For Json Response
     */
    static Retrofit getApiClientJson(String baseUrl)
    {
        if (retrofit==null)
        {
            retrofit = builderJson.baseUrl(baseUrl).build();
        }
        return retrofit;
    }

    /*
     * Create the Retrofit Instance For String Response
     */
    static Retrofit getApiClientString(String baseUrl)
    {
        if (retrofit==null)
        {
            retrofit = builderString.baseUrl(baseUrl).build();
        }
        return retrofit;
    }
}
