package payment.app.using.stripe;

public class ApiUtils {

    private static AppConstants appConstants = AppConstants.CONFIG;
    private static final String API_BASE_URL_FOR_RETROFIT_2 =appConstants.chargeURL();

    public static ApiService getAPIServiceString()
    {
        return ApiClient.getApiClientString(API_BASE_URL_FOR_RETROFIT_2).create(ApiService.class);
    }

    public static ApiService getAPIServiceJson()
    {
        return ApiClient.getApiClientJson(API_BASE_URL_FOR_RETROFIT_2).create(ApiService.class);
    }
}
