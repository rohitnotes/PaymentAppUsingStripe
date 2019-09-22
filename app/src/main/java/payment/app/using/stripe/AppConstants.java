package payment.app.using.stripe;

import java.util.UUID;

public enum AppConstants {

  /*
     * Success URL for LIVE mode
     */
    CONFIG {
      @Override
      public String publishableKey() {
          return "";
      }

      @Override
      public String secretKey() {
          return "";
      }

      @Override
      public String chargeURL() {
          return "https://hellomajorproject.000webhostapp.com/payment/stripe/";
      }

    @Override
    public String getTransactionId() {
      UUID uuid = UUID.randomUUID();
      //return "TXNID"+uuid.getMostSignificantBits();
      return "TXNID"+System.currentTimeMillis();
    }
  };

    public abstract String publishableKey();
    public abstract String secretKey();
    public abstract String chargeURL();
    public abstract String getTransactionId();
}
