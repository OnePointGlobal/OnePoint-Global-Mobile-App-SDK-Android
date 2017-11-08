package com.onepointglobal.mysurveysn;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.opg.sdk.models.OPGPanellistProfile;
import com.opg.sdk.models.OPGUpdatePanellistProfile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * The type Update panelist profile activity.
 */
public class UpdatePanellistProfileActivity extends AppCompatActivity {

    private EditText et_firstName, et_lastName, et_email, et_mobileNo, et_address1, et_address2, et_postalcode, et_maritalStatus;

    /**
     * The Spn title.
     */
    Spinner spn_title, /**
     * The Spn gender.
     */
    spn_gender, /**
     * The Spn country code.
     */
    spn_countryCode;
    /**
     * The Output tv.
     */
//Button submit_btn;
    TextView output_tv, /**
     * The Dob tv.
     */
    dob_tv;
    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;
    private OPGPanellistProfile panelistProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_panelist_profile);
        spn_title = (Spinner) findViewById(R.id.title_spn);
        et_firstName = (EditText) findViewById(R.id.firstName_et);
        et_lastName = (EditText) findViewById(R.id.lastName_et);
        et_email = (EditText) findViewById(R.id.email_et);
        et_mobileNo = (EditText) findViewById(R.id.mobile_et);
        et_address1 = (EditText) findViewById(R.id.address1_et);
        et_address2 = (EditText) findViewById(R.id.address2_et);
        dob_tv = (TextView) findViewById(R.id.dob_tv);
        spn_gender = (Spinner) findViewById(R.id.gender_spn);
        et_postalcode = (EditText) findViewById(R.id.postal_et);
        output_tv = (TextView) findViewById(R.id.output_tv);
        et_email.setEnabled(false);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");

        if(Util.isOnline(UpdatePanellistProfileActivity.this))
        {
            //Getting the panelist profile
            new GetPanelistProfileTask().execute();
        }
        else
        {
            Util.showAlert(UpdatePanellistProfileActivity.this);
        }



        dob_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                showDatePicker(mYear, mMonth, mDay);
            }
        });
    }

    /**
     * Show date picker.
     *
     * @param mYear  the m year
     * @param mMonth the m month
     * @param mDay   the m day
     */
    public void showDatePicker(int mYear, int mMonth, int mDay) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdatePanellistProfileActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dob_tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private class GetPanelistProfileTask extends AsyncTask<Void, Void, OPGPanellistProfile> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected OPGPanellistProfile doInBackground(Void... params)
        {
            OPGPanellistProfile profile = null;
            try
            {
                profile = Util.getOPGSDKInstance().getPanellistProfile(UpdatePanellistProfileActivity.this);
            }
            catch (Exception ex)
            {
                Log.i(UpdatePanellistProfileActivity.class.getName(), ex.getMessage());
            }
            return profile;
        }

        @Override
        protected void onPostExecute(OPGPanellistProfile opgPanelistProfile)
        {
            if(progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if(opgPanelistProfile != null )
            {
                if(opgPanelistProfile.isSuccess())
                {
                    panelistProfile = opgPanelistProfile;
                    System.out.print(opgPanelistProfile.getTitle());
                    spn_title.setSelection(Integer.parseInt(opgPanelistProfile.getTitle()));
                    et_firstName.setText(opgPanelistProfile.getFirstName());
                    et_lastName.setText(opgPanelistProfile.getLastName());
                    et_email.setText(opgPanelistProfile.getEmailID());
                    et_mobileNo.setText(opgPanelistProfile.getMobileNumber());
                    et_address1.setText(opgPanelistProfile.getAddress1());
                    et_address2.setText(opgPanelistProfile.getAddress2());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    dob_tv.setText( dateFormat.format(opgPanelistProfile.getDob()));
                    spn_gender.setSelection(opgPanelistProfile.getGender());
                    et_postalcode.setText(opgPanelistProfile.getPostalCode());
                }
                else
                {
                    output_tv.setText("Message : "+opgPanelistProfile.getErrorMessage());
                }
            }
            else
            {

            }
        }

    }

    /**
     * On update click.
     *
     * @param view the view
     */
    public void onUpdateClick(View view)
    {

        if(panelistProfile != null)
        {
            int  title       = spn_title.getSelectedItemPosition();
            String firstName = et_firstName.getText().toString().trim();
            String lastName = et_lastName.getText().toString().trim();
            String email = et_email.getText().toString().trim();
            String mobileNo = et_mobileNo.getText().toString().trim();
            String address1 = et_address1.getText().toString().trim();
            String address2 = et_address2.getText().toString().trim();
            String dob = dob_tv.getText().toString().trim();
            int gender = spn_gender.getSelectedItemPosition();
            String postalCode = et_postalcode.getText().toString().trim();
            if(title==0){
                Toast.makeText(getApplicationContext(),getResources().getText(R.string.title_error),Toast.LENGTH_LONG).show();
                return;
            }
            if(gender==0){
                Toast.makeText(getApplicationContext(),getResources().getText(R.string.gender_error),Toast.LENGTH_LONG).show();
                return;
            }

            panelistProfile.setTitle(String.valueOf(title));
            panelistProfile.setFirstName(firstName);
            panelistProfile.setLastName(lastName);
            panelistProfile.setEmailID(email);
            panelistProfile.setMobileNumber(mobileNo);
            panelistProfile.setAddress1(address1);
            panelistProfile.setAddress2(address2);
            panelistProfile.setGender(gender);
            try {
                panelistProfile.setDob((new SimpleDateFormat("dd-MM-yyyy")).parse(dob));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            panelistProfile.setPostalCode(postalCode);

            new UpdatePanelistProfiletask().execute(panelistProfile);

        }
    }

    private class UpdatePanelistProfiletask extends AsyncTask<OPGPanellistProfile,Void,OPGUpdatePanellistProfile>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.setMessage("Updating...");
            progressDialog.show();
        }

        @Override
        protected OPGUpdatePanellistProfile doInBackground(OPGPanellistProfile... params)
        {
            // Updating the panelist profile
            return Util.getOPGSDKInstance().updatePanellistProfile(UpdatePanellistProfileActivity.this, params[0]);
        }

        @Override
        protected void onPostExecute(OPGUpdatePanellistProfile opgUpdatePanelistProfile)
        {
            super.onPostExecute(opgUpdatePanelistProfile);
            if(progressDialog != null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            if(opgUpdatePanelistProfile != null)
            {
                StringBuilder builder = new StringBuilder();
                builder.append("\nMessage : ").append(opgUpdatePanelistProfile.getStatusMessage());
                output_tv.setText(builder.toString());
            }
        }
    }
}
