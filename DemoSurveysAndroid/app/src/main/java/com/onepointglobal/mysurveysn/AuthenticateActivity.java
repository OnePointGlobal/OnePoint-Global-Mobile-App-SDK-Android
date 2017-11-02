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
    private EditText appLogInUserNameEdt, appLogInPwdEdt, etAdminUserName, etAdminSharedKey,etAppVersion;
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

        etAdminUserName = (EditText)findViewById(R.id.etAdminUserName);
        etAdminSharedKey = (EditText)findViewById(R.id.etAdminSharedKey);
        etAdminUserName.setSingleLine(true);
        etAdminSharedKey.setSingleLine(true);
        appLogInUserNameEdt = (EditText) findViewById(R.id.app_username);
        appLogInPwdEdt = (EditText) findViewById(R.id.app_password);
        etAppVersion = (EditText) findViewById(R.id.app_version);
        appLogInUserNameEdt.setSingleLine(true);
        appLogInPwdEdt.setSingleLine(true);
        etAppVersion.setSingleLine(true);

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
        String adminUserName,adminSharedKey, appVersion, appLoginPwd,  appLoginUserName;

        /**
         * Instantiates a new Authenticate task.
         */
        public AuthenticateTask(){
            if(appLogInUserNameEdt.getText() != null)
                appLoginUserName = appLogInUserNameEdt.getText().toString().trim();
            if(appLogInPwdEdt.getText() != null)
                appLoginPwd = appLogInPwdEdt.getText().toString().trim();
            if(etAdminUserName.getText() != null)
                adminUserName = etAdminUserName.getText().toString().trim();
            if(etAdminSharedKey.getText() != null)
                adminSharedKey = etAdminSharedKey.getText().toString().trim();
            if(etAppVersion.getText() != null)
                appVersion = etAppVersion.getText().toString().trim();
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
                //Initialising the OPGSDK with adminUsername and adminSharedKey
                OPGSDK.initialize(adminUserName , adminSharedKey, getApplicationContext());
                opgsdk.setAppVersion(appVersion,getApplicationContext());
                //authenticate the panellist
                opgAuthenticate = opgsdk.authenticate(appLoginUserName, appLoginPwd,AuthenticateActivity.this);

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
