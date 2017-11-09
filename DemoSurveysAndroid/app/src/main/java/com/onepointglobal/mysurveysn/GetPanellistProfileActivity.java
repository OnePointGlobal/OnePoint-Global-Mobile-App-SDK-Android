package com.onepointglobal.mysurveysn;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.opg.sdk.OPGSDK;
import com.opg.sdk.models.OPGPanellistProfile;

import java.text.SimpleDateFormat;

/**
 * The type Get panelist profile activity.
 */
public class GetPanellistProfileActivity extends AppCompatActivity
{

    private TextView output_tv;
    private OPGSDK opgsdk;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_planelist_profile);
        opgsdk            = Util.getOPGSDKInstance();
        progressDialog    = new ProgressDialog(this);
        output_tv         = (TextView) findViewById(R.id.output_tv);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        if(Util.isOnline(GetPanellistProfileActivity.this))
        {
            new GetPanelistProfileTask().execute();
        }
        else
        {
            Util.showAlert(GetPanellistProfileActivity.this);
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
    private class GetPanelistProfileTask extends AsyncTask<Void, Void, OPGPanellistProfile>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected OPGPanellistProfile doInBackground(Void... params)
        {
            OPGPanellistProfile profile = null;
            try
            {
                // To get the profile of panelist
                profile =  opgsdk.getPanellistProfile(GetPanellistProfileActivity.this);
                String str = profile.getErrorMessage();
            }
            catch (Exception ex)
            {
                Log.i(GetPanellistProfileActivity.class.getName(),ex.getMessage());
                profile.setErrorMessage(ex.getMessage());
            }
            return profile;
        }

        @Override
        protected void onPostExecute(OPGPanellistProfile profile) {
            super.onPostExecute(profile);
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(profile!=null )
            {
                StringBuilder builder = new StringBuilder();
                if(profile.isSuccess())
                {
                    builder.append("\nStatusMessage : ").append(profile.getStatusMessage());
                    builder.append("\nFirstName : ").append(profile.getFirstName());
                    builder.append("\nLastName : ").append(profile.getLastName());
                    builder.append("\nEmailId : ").append(profile.getEmailID());
                    builder.append("\nTitle : ").append(getTitle(Integer.parseInt(profile.getTitle())));
                    builder.append("\nAddress1 : ").append(profile.getAddress1());
                    builder.append("\nAddress2 : ").append(profile.getAddress2());
                    builder.append("\nGender : ").append(getGender(profile.getGender()));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    builder.append("\nDateOfBirth : ").append(dateFormat.format(profile.getDob()));
                    builder.append("\nMobileNo : ").append(profile.getMobileNumber());
                    builder.append("\nPostalCode : ").append(profile.getPostalCode());
                }else{
                    builder.append("\nStatusMessage : ").append(profile.getStatusMessage());
                    builder.append("\nErrorMessage : ").append(profile.getErrorMessage());
                }
                output_tv.setText(builder.toString());
            }
        }
    }
    private static String getGender(int index)
    {
        switch (index)
        {
            case 1 : return "Male";
            case 2 :return "Female";
            default:return index+"";
        }


    }
    private static String getTitle(int index)
    {
        switch (index)
        {
            case 1 : return "Mr";
            case 2 :return "Ms";
            case 3 : return "Mrs";
            default:return index+"";
        }

    }
}
