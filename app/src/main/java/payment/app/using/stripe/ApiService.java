package payment.app.using.stripe;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("charges.php")
    Call<String> getHash(@FieldMap Map<String, Object> fields);
}
