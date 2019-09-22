package payment.app.using.stripe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import java.util.HashMap;
import java.util.Map;

public class StripePaymentActivity extends AppCompatActivity {

    private static final String TAG = StripePaymentActivity.class.getSimpleName();

    private TextView productNameTextView,productDescriptionTextView,productPriceTextView;
    private ProgressBar loadingProgressBar;
    private LinearLayout paymentUsingStripeButton;

    private AppConstants appConstants;
    private Stripe stripe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        initView();
        initObject();
        initEvent();
    }

    private void initView()
    {
        productNameTextView = findViewById(R.id.product_name);
        productPriceTextView = findViewById(R.id.product_price);
        productDescriptionTextView = findViewById(R.id.product_description);
        loadingProgressBar = findViewById(R.id.loading_payment);
        paymentUsingStripeButton = findViewById(R.id.payment_using_stripe);
    }

    private void initObject()
    {
        appConstants = AppConstants.CONFIG;
        stripe = new Stripe(StripePaymentActivity.this, appConstants.publishableKey());
    }


    private void initEvent()
    {
        paymentUsingStripeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressUtils.showLoadingDialog(StripePaymentActivity.this);
                getStripeToken("4242424242424242","12","2020","123");
            }
        });
    }

    private void getStripeToken(String cardNumber, String expirationMonth, String expirationYear, String cvv)
    {
        final Card card = Card.create(cardNumber, Integer.parseInt(expirationMonth), Integer.parseInt(expirationYear), cvv);

        /*
         * Remember to validate the card object before you use it to save time.
         */
        if (card.validateCard())
        {
            stripe.createToken(
                    card,
                    new ApiResultCallback<Token>()
                    {
                        public void onSuccess(@NonNull Token token)
                        {
                            Log.d(TAG, "******TOKEN******"+token);
                            /*
                             * send token ID to your server, and charge to customer in server side
                             */
                            StripeOperation.stripeChargeToUser(appConstants,StripePaymentActivity.this,token.getId());

                            Toast.makeText(StripePaymentActivity.this, token.getId(), Toast.LENGTH_SHORT).show();
                            /*
                             * charge to customer in client side
                             */
                           // sendCardToInfoToStripe(token, false);
                        }

                        @Override
                        public void onError(@NonNull Exception error)
                        {
                            /*
                             * Show localized error message
                             */
                            Toast.makeText(StripePaymentActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }
        else if (!card.validateNumber())
        {
            Toast.makeText(StripePaymentActivity.this, "The card number that you entered is invalid.", Toast.LENGTH_SHORT).show();
        }
        else if (!card.validateExpiryDate())
        {
            Toast.makeText(StripePaymentActivity.this, "The expiration date that you entered is invalid.", Toast.LENGTH_SHORT).show();
        }
        else if (!card.validateCVC())
        {
            Toast.makeText(StripePaymentActivity.this, "The CVC code that you entered is invalid.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(StripePaymentActivity.this, "The card details that you entered are invalid.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendCardToInfoToStripe(final Token token, final boolean isLive)
    {
        com.stripe.Stripe.apiKey = appConstants.secretKey();

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
        chargeParams.put("source", token.getId());

        Map<String, String> initialMetadata = new HashMap<>();
        initialMetadata.put("custom_order_id", appConstants.getTransactionId());
        initialMetadata.put("phone", "7898680304");
        initialMetadata.put("email", "rohit2403yadav@gmail.com");
        initialMetadata.put("name", "rohit yadav");
        chargeParams.put("metadata", initialMetadata);
        ChargeTask getHashesFromServerTask = new ChargeTask(chargeParams);
        getHashesFromServerTask.execute();
    }

    /**
     * This AsyncTask generates hash from server.
     */
    public class ChargeTask extends AsyncTask<Void, Void, Void>
    {
        Charge charge;
        Map<String, Object> chargeParams;
        public ChargeTask(Map<String, Object> chargeParams) {
          this.chargeParams=chargeParams;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try
            {
                charge = Charge.create(chargeParams);
                Log.d(TAG, "******CHARGE RESPONSE******"+charge);
                Log.d(TAG, "******IsCharged******"+charge.getCreated());
            }
            catch (StripeException chargeException)
            {
                chargeException.printStackTrace();
                Log.d(TAG, "******Exception From Charge******"+charge);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(StripePaymentActivity.this,
                    "Card Charged : " + charge.getCreated() + "\nPaid : " +charge.getPaid(),
                    Toast.LENGTH_LONG
            ).show();
            Log.d(TAG, "******CHARGE******"+charge);
            ProgressUtils.cancelLoading();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
