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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.opg.sdk.OPGSDK;
import com.opg.sdk.models.OPGPanel;
import com.opg.sdk.models.OPGPanelPanellist;
import com.opg.sdk.models.OPGPanellistPanel;
import com.opg.sdk.models.OPGSurvey;
import com.opg.sdk.models.OPGSurveyPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Survey list activity.
 */
public class SurveyListActivity extends AppCompatActivity
{
private ListView listView;
private ProgressDialog pDialog;
private CheckBox cb;
private Context context;
@Override
protected void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.survey_list);
    listView = (ListView) findViewById(R.id.list);
    cb =(CheckBox) findViewById(R.id.panelid_cb);
    context = this;
    pDialog = new ProgressDialog(this);
    pDialog.setMessage("Loading Surveys...");

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
            if(params.length > 0 )
            {
                if(params[0] != null)
                {
                    // Getting the list survey for particular panelID
                    //case 1 : getting the list of surveys for particular panel
                    surveyList = Util.getOPGSDKInstance().getSurveys(SurveyListActivity.this,params[0]);
                    Util.panelID = Long.parseLong(params[0]);
                }
            }
            else
            {

                /*OPGPanellistPanel opgPanellistPanel =  Util.getOPGSDKInstance().getPanellistPanel(getApplicationContext());
                if(opgPanellistPanel.isSuccess())
                {
                    List<OPGPanel> panelList = opgPanellistPanel.getPanelArray();
                    if(panelList.size() > 0)
                    {
                        Util.panelID = panelList.get(0).getPanelID();
                    }
                    //fetching the list of surveys for first panel
                     if(Util.panelID >0)
                     {
                         surveyList =  Util.getOPGSDKInstance().getSurveys(getApplicationContext(),Util.panelID+"");
                     }
                     else
                     {
                         throw  new Exception("panel");
                     }


                }*/
                if(params.length > 0 && params[0] != null)
                {
                    surveyList = Util.getOPGSDKInstance().getSurveys(SurveyListActivity.this,params[0]);
                    Util.panelID = Long.parseLong(params[0]);

                }
                else

                // Getting the list of surveys
                //Case 2 : When authentication is done and unique id is generated for this panellist
                if(Util.getOPGSDKInstance().getUniqueID(getApplicationContext()) != null)
                {
                    surveyList = Util.getOPGSDKInstance().getUserSurveyList(SurveyListActivity.this);
                }
                else
                {
                    //Case 3 : When authentication is not  done and  unique id is not generated for this panellist
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
        if(exceptionObj == null)
        {
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
                            //OpgSDkLite supports only online surveys.
                            if(surveys.isOffline())
                            {
                                Toast.makeText(getApplicationContext(), "This is a lite opgsdk. Offline survey will not work.", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                String surveyReference = surveys.getSurveyReference();
                                Toast.makeText(getApplicationContext(), "" + surveyReference, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SurveyListActivity.this, BrowseActivity.class);
                                intent.putExtra("surveyReference", surveyReference);
                                startActivity(intent);
                            }
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

}
