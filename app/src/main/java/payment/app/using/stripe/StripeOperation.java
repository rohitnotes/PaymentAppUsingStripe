package payment.app.using.stripe;

import android.content.Context;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class StripeOperation {

    /*
     * You should not make a charge inside the Android app.
     * The client (here your App), should only get a Stripe token,
     * then send this token to your backend (your server), and then use this token to create a charge with stripe.
     */
    private static final String TAG = StripeOperation.class.getSimpleName();

    public static void stripeChargeToUser(AppConstants appConstants,Context context, String tokenId)
    {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        /**
         * Minimum amount is $0.50 US
         */
        chargeParams.put("amount", 60);
        chargeParams.put("currency", "USD");
        chargeParams.put("description", "A T-shirt is a style of fabric shirt named after the T shape of its body and sleeves.");
        chargeParams.put("statement_descriptor", "statement descriptor");
        chargeParams.put("receipt_email", "rohit2403yadav@gmail.com");
        /*
         * Get the payment token ID:
         */
        chargeParams.put("token", tokenId);
        chargeParams.put("custom_order_id", appConstants.getTransactionId());
        chargeParams.put("phone", "7898680304");
        chargeParams.put("email", "rohit2403yadav@gmail.com");
        chargeParams.put("name", "rohit yadav");

        ApiService apiService = ApiUtils.getAPIServiceString();
        Call<String> call = apiService.getHash(chargeParams);

        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("Response Code : " + response.code());
                    ProgressUtils.cancelLoading();
                    return;
                }
                String responseString = response.body();
                ProgressUtils.cancelLoading();
                Toast.makeText(context, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable)
            {
                System.out.println("ERROR : " + throwable.getMessage());
            }
        });
    }
}
