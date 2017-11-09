package com.onepointglobal.mysurveysn;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.opg.sdk.OPGSDK;
import com.opg.sdk.models.OPGForgotPassword;

/**
 * The type Forgot password activity.
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    /**
     * The Email et.
     */
    EditText email_et;
    /**
     * The Submit btn.
     */
    Button submit_btn;
    /**
     * The Output tv.
     */
    TextView output_tv;
    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        progressDialog   = new ProgressDialog(this);
        email_et         = (EditText)findViewById(R.id.email_et);
        submit_btn       = (Button) findViewById(R.id.submit_btn);
        output_tv        = (TextView) findViewById(R.id.output_tv);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email =email_et.getText().toString();
                if(email != null & email.trim().length()>0)
                {
                    if(Util.isOnline(ForgotPasswordActivity.this))
                    {
                        new ForgotPasswordTask().execute(email);
                    }
                    else
                    {
                        Util.showAlert(ForgotPasswordActivity.this);
                    }

                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter EmailID.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class ForgotPasswordTask extends AsyncTask<String,Void ,OPGForgotPassword>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected OPGForgotPassword doInBackground(String... strings) {
            OPGForgotPassword opgAuthenticate = null;
            try
            {
                OPGSDK opgsdk = Util.getOPGSDKInstance();
                // It will send the link on registered emailId of panelist to reset the password.
                opgAuthenticate = opgsdk.forgotPassword(strings[0],ForgotPasswordActivity.this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                opgAuthenticate.setStatusMessage(e.getMessage());
            }
            return opgAuthenticate;
        }

        @Override
        protected void onPostExecute(OPGForgotPassword opgAuthenticate)
        {
            if(progressDialog!=null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            if(opgAuthenticate!=null)
            {
                output_tv.setText("Message : "+opgAuthenticate.getStatusMessage());
            }
        }
    }
}
