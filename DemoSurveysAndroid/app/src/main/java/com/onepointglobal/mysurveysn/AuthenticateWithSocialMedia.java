package com.onepointglobal.mysurveysn;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.opg.sdk.OPGSDK;
import com.opg.sdk.models.OPGAuthenticate;

/**
 * The type Authenticate with social media.
 */
public class AuthenticateWithSocialMedia extends AppCompatActivity {

    private EditText facebook_token_et,google_token_et;
    private TextView facebook_output_tv,google_output_tv;

    private AlertDialog.Builder alertDialog;

    private ProgressDialog progressDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenicate_with_social_media);

        facebook_token_et = (EditText) findViewById(R.id.facebook_token_et);

        google_token_et    = (EditText) findViewById(R.id.google_token_et);
        facebook_output_tv = (TextView) findViewById(R.id.facebook_output);
        google_output_tv   = (TextView) findViewById(R.id.google_output);

        mContext    = this;

        alertDialog = new AlertDialog.Builder(this);


        progressDialog = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");

    }

    /**
     * Facebook authenciate.
     *
     * @param v the v
     */
    public void  facebook_authenciate(View v){
        if(Util.isOnline(AuthenticateWithSocialMedia.this))
        {
            String facebook_token =facebook_token_et.getText().toString().trim();
           if(!facebook_token.isEmpty()){
               new  AuthenticateTask(facebook_token,"").execute();
           }else{
               facebook_token_et.setError("Facebook token should not be empty!!!!");
           }
        }
        else
        {
            Util.showAlert(AuthenticateWithSocialMedia.this);
        }
    }

    /**
     * Google authenciate.
     *
     * @param v the v
     */
    public void  google_authenciate(View v){
        if(Util.isOnline(AuthenticateWithSocialMedia.this))
        {
            String google_token =google_token_et.getText().toString().trim();
            if(!google_token.isEmpty()){
                new  AuthenticateTask("",google_token).execute();
            }else{
                google_token_et.setError("Google token should not be empty!!!!");
            }
        }
        else
        {
            Util.showAlert(AuthenticateWithSocialMedia.this);
        }
    }

    private class AuthenticateTask extends AsyncTask<Void, Void, OPGAuthenticate>
    {

        /**
         * The Facebook token.
         */
        String facebook_token, /**
     * The Google token.
     */
    google_token;


        /**
         * Instantiates a new Authenticate task.
         *
         * @param facebook_token the facebook token
         * @param google_token   the google token
         */
        public AuthenticateTask(String facebook_token, String google_token) {
            this.facebook_token = facebook_token;
            this.google_token = google_token;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected OPGAuthenticate doInBackground(Void... params)
        {
            OPGAuthenticate opgAuthenticate = new OPGAuthenticate();
            try {
                OPGSDK opgsdk = new OPGSDK();
                if(google_token.isEmpty()){
                    opgAuthenticate = opgsdk.authenticateWithFacebook(facebook_token,AuthenticateWithSocialMedia.this);
                }else if(facebook_token.isEmpty()){
                    opgAuthenticate = opgsdk.authenticateWithGoogle(google_token,AuthenticateWithSocialMedia.this);
                }
            }
            catch (Exception ex) {
                Log.i("DemoApp", ex.getMessage());
                opgAuthenticate.setStatusMessage(ex.getMessage());
            }
            return opgAuthenticate;
        }

        @Override
        protected void onPostExecute(OPGAuthenticate opgAuthenticate) {
            super.onPostExecute(opgAuthenticate);
            if(progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if(opgAuthenticate != null)
            {
                StringBuilder builder = new StringBuilder();
                if(opgAuthenticate.isSuccess()) {
                    builder.append("UniqueID : ").append(opgAuthenticate.getUniqueID()).append("\nMessage : ").append(opgAuthenticate.getStatusMessage());
                }
                else {
                    builder.append("Message : ").append(opgAuthenticate.getStatusMessage());
                }
                if(google_token.isEmpty())
                    facebook_output_tv.setText(builder.toString());

                if(facebook_token.isEmpty())
                        google_output_tv.setText(builder.toString());

            }
        }
    }

}
