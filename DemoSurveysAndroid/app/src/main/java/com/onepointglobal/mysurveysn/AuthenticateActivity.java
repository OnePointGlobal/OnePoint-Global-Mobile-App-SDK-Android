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
 * The type Authenticate activity.
 */
public class AuthenticateActivity extends AppCompatActivity
{
    private EditText appLogInUserNameEdt, appLogInPwdEdt;
    private AlertDialog.Builder alertDialog;

    private TextView output_tv;
    private ProgressDialog progressDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);
        mContext    = this;

        appLogInUserNameEdt = (EditText) findViewById(R.id.app_username);
        appLogInPwdEdt = (EditText) findViewById(R.id.app_password);

        output_tv = (TextView) findViewById(R.id.output_tv);
        alertDialog = new AlertDialog.Builder(this);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");

    }

    /**
     * Method called when clicked on signin button
     *
     * @param view the view
     */
    public void signInClick(View view)
    {

        if(Util.isOnline(AuthenticateActivity.this))
        {
            new AuthenticateTask().execute();
        }
        else
        {
            Util.showAlert(AuthenticateActivity.this);
        }

    }
    private class AuthenticateTask extends AsyncTask<Void, Void, OPGAuthenticate>
    {

        /**
         * The Username.
         */
        String username, /**
     * The Shared key.
     */
    sharedKey, /**
     * The App version.
     */
    appVersion/*,uniqueID*/, /**
     * The App login pwd.
     */
    appLoginPwd, /**
     * The App login user name.
     */
    appLoginUserName;

        /**
         * Instantiates a new Authenticate task.
         */
        public AuthenticateTask(){
            if(appLogInUserNameEdt.getText() != null)
                appLoginUserName = appLogInUserNameEdt.getText().toString().trim();
            if(appLogInPwdEdt.getText() != null)
                appLoginPwd = appLogInPwdEdt.getText().toString().trim();
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
                //authenticate the panelist
                opgAuthenticate = opgsdk.authenticate(appLoginUserName, appLoginPwd,AuthenticateActivity.this);
                // opgsdk.setUniqueID(uniqueID,getApplicationContext());
                //opgAuthenticate.setSuccess(true);
                //opgAuthenticate.setUniqueID(uniqueID);
                //opgAuthenticate.setStatusMessage("Success");
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
                output_tv.setText(builder.toString());
            }
        }
    }
}
