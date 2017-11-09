package com.onepointglobal.mysurveysn;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opg.sdk.models.OPGScript;
import com.opg.sdk.models.OPGSurveyPanel;

import java.util.List;

/**
 * The type Get script activity.
 */
public class GetScriptActivity extends AppCompatActivity
{
private ProgressDialog progressDialog;
private TextView   script_path;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_get_script);
    script_path = (TextView) findViewById(R.id.script_path);
    progressDialog   = new ProgressDialog(this);
    progressDialog.setIndeterminate(true);
    progressDialog.setCancelable(true);
    progressDialog.setMessage("Loading...");

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

    /**
     * On script click.
     *
     * @param view the view
     */
    public  void onScriptClick(View view)
{
    String surveyRefence =((EditText) findViewById(R.id.surveyidET)).getText().toString();
    if(surveyRefence != null & surveyRefence.trim().length()>0)
    {
        if(Util.isOnline(GetScriptActivity.this))
        {
            // download the script file for particular survey reference
            new ScriptTask().execute(surveyRefence);
        }
        else
        {
            Util.showAlert(GetScriptActivity.this);
        }

    }
    else
    {
        Toast.makeText(this,"Please enter SurveyReference.",Toast.LENGTH_SHORT).show();
    }
}
private class ScriptTask extends AsyncTask<String,Void,OPGScript>
{
    @Override
    protected void onPreExecute()
    {
        progressDialog.show();
    }

    @Override
    protected OPGScript doInBackground(String... params)
    {
        OPGScript opgScript = new OPGScript();
        try
        {
            //opgScript = Util.getOPGSDKInstance().getScript(GetScriptActivity.this,params[0]);
        }catch (Exception ex)
        {
            Log.i(GetScriptActivity.class.getName(),ex.getLocalizedMessage());
            opgScript.setStatusMessage(ex.getMessage());
        }
        return opgScript;
    }

    @Override
    protected void onPostExecute(OPGScript opgScript)
    {
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        if(opgScript != null)
        {
            if(opgScript.isSuccess())
            {
                script_path.setText("/Status Message : "+opgScript.getStatusMessage()+", Script Path : "+opgScript.getScriptFilePath());
            }
            else
            {
                script_path.setText("Status Message : "+opgScript.getStatusMessage());
            }
        }
        else
        {
            script_path.setText("Some thing went wrong.");
        }

    }
}
}
