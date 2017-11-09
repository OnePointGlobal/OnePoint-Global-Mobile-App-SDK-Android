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
import com.opg.sdk.models.OPGChangePassword;

/**
 * The type Change password activity.
 */
public class ChangePasswordActivity extends AppCompatActivity {


    /**
     * The Current pwd et.
     */
    EditText current_pwd_et, /**
     * The New pwd et.
     */
    new_pwd_et;
    /**
     * The Submit btn.
     */
    Button submit_btn;
    /**
     * The Output tv.
     */
    TextView output_tv;
    /**
     * The Opgsdk.
     */
    OPGSDK opgsdk;
    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);
    opgsdk            = Util.getOPGSDKInstance();
    progressDialog    = new ProgressDialog(this);
    current_pwd_et    = (EditText)findViewById(R.id.currentpwd_et);
    new_pwd_et        = (EditText)findViewById(R.id.newpwd_et);
    submit_btn        = (Button) findViewById(R.id.submit_btn);
    output_tv         = (TextView) findViewById(R.id.output_tv);
    progressDialog.setIndeterminate(true);
    progressDialog.setCancelable(true);
    progressDialog.setMessage("Loading");

    submit_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String currentPwd = current_pwd_et.getText().toString();
            String newPwd     = new_pwd_et.getText().toString();
            if(Util.isOnline(ChangePasswordActivity.this))
            {
                if(currentPwd != null & currentPwd.trim().length()>0 & newPwd != null & newPwd.trim().length()>0)
                {
                    new ChangePasswordTask().execute(currentPwd,newPwd);
                }
                else
                {
                    Toast.makeText(ChangePasswordActivity.this,"Either Current or New password is empty.",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Util.showAlert(ChangePasswordActivity.this);
            }
        }
    });

}


private class ChangePasswordTask extends AsyncTask<String ,Void ,OPGChangePassword>
{
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected OPGChangePassword doInBackground(String... params)
    {
        try
        {
            // to change the password of panelist
            return opgsdk.changePassword(ChangePasswordActivity.this,params[0],params[1]);
        } catch (Exception e)
        {
            return  null;
        }
    }

    @Override
    protected void onPostExecute(OPGChangePassword opgChangePassword)
    {
        super.onPostExecute(opgChangePassword);
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if(opgChangePassword!=null){
            StringBuilder builder = new StringBuilder();
            builder.append("\nMessage : ").append(opgChangePassword.getStatusMessage());
            output_tv.setText(builder.toString());
        }
    }
}
}
