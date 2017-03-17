package com.onepointglobal.mysurveysn;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.opg.sdk.OPGSDK;
import com.opg.sdk.models.OPGSurvey;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Survey list activity.
 */
public class SurveyListActivity extends AppCompatActivity
{
private  ListView listView;
private ProgressDialog pDialog;
private CheckBox cb;

@Override
protected void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.survey_list);
    listView = (ListView) findViewById(R.id.list);
    cb =(CheckBox) findViewById(R.id.panelid_cb);

    pDialog = new ProgressDialog(this);
    pDialog.setMessage("Loading Surveys...");
}

    /**
     * On submit click.
     *
     * @param view the view
     */
    public void onSubmitClick(View view)
{
    if (Util.isOnline(this))
    {
        listView.invalidate();
        if(cb.isChecked())
        {
            String panelID = ((EditText)findViewById(R.id.panelid_et)).getText().toString();
            if(panelID != null & !panelID.trim().isEmpty())
            {
                new SurveyListTask().execute(panelID);

            }
            else
            {
                Toast.makeText(this,"Please enter PanelId.",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            new SurveyListTask().execute();
        }

    }
    else
    {
        Util.showAlert(this);
    }

}

private class SurveyListTask extends AsyncTask<String, Void, List<OPGSurvey>>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.show();
    }

    /**
     * The Exception obj.
     */
    Exception exceptionObj = null;
    @Override
    protected List<OPGSurvey> doInBackground(String... params)
    {
        List<OPGSurvey> surveyList =new ArrayList<OPGSurvey>();
        try
        {
            if(params.length > 0 && params[0] != null)
            {
                // Getting the list survey for particular panelID
                surveyList = Util.getOPGSDKInstance().getSurveys(SurveyListActivity.this,params[0]);
            }
            else
            {
                // Getting the list survey
               /* if(Util.getOPGSDKInstance().getUniqueID(getApplicationContext()) != null)
                {
                    surveyList = Util.getOPGSDKInstance().getUserSurveyList(SurveyListActivity.this);
                }
                else*/
                {
                    //without unique id
                    surveyList = Util.getOPGSDKInstance().getSurveyList(SurveyListActivity.this);
                }

            }

        }
        catch (final Exception e)
        {
            e.printStackTrace();
            exceptionObj = e;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ((TextView)findViewById(R.id.get_survey_output)).setText(e.getLocalizedMessage());
                }
            });
        }
        return surveyList;
    }

    protected void onPostExecute(final List<OPGSurvey> opgSurveys)
    {
        if (pDialog != null & pDialog.isShowing())
        {
            pDialog.dismiss();
        }
        if(opgSurveys != null)
        {
            if(opgSurveys.size()>0)
            {
                ((TextView)findViewById(R.id.get_survey_output)).setText("SurveyList size : "+opgSurveys.size());
                listView.setAdapter(new SurveyAdapter(SurveyListActivity.this,(ArrayList<OPGSurvey>) opgSurveys));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                                   long arg3) {
                        OPGSurvey surveys = opgSurveys.get(position);
                        String surveyReference = surveys.getSurveyReference();
                        Toast.makeText(getApplicationContext(), "" + surveyReference, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SurveyListActivity.this, BrowseActivity.class);
                        intent.putExtra("surveyReference", surveyReference);
                        startActivity(intent);
                    }
                });
            }
            else
            {
                ((TextView)findViewById(R.id.get_survey_output)).setText("No Surveys found for these credentials");
                listView.setAdapter(new SurveyAdapter(SurveyListActivity.this,(ArrayList<OPGSurvey>) opgSurveys));
            }

        }
        else
        {
            ((TextView)findViewById(R.id.get_survey_output)).setText("No Surveys found for these credentials");
        }

    }
}

}
