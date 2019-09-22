package payment.app.using.stripe;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

public class ProgressUtils
{
    private static ProgressDialog progressDialog;

    public static void showLoadingDialog(Context context)
    {
        if (!(progressDialog != null && progressDialog.isShowing()))
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
        }
    }

    public static void cancelLoading()
    {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();
    }
}
