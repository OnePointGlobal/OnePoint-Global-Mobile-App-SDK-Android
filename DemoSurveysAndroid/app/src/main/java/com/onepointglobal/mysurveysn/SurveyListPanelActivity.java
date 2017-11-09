package com.onepointglobal.mysurveysn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opg.sdk.models.OPGPanel;
import com.opg.sdk.models.OPGPanellistPanel;
import com.opg.sdk.models.OPGSurvey;

import java.util.ArrayList;
import java.util.List;

public class SurveyListPanelActivity extends AppCompatActivity {
    private ListView listView;
    private ProgressDialog pDialog;
    private CheckBox cb;
    private Context context;
    private long panelId = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list_panel);
        listView = (ListView) findViewById(R.id.list);
        context = this;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Surveys...");
        panelId = getIntent().getLongExtra("panelId",0);
        if(panelId != 0)
        {
            new SurveyListTask().execute();
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
                Util.getOPGSDKInstance().getSurveys(context,panelId+"");
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
                        listView.setAdapter(new SurveyAdapter(SurveyListPanelActivity.this,(ArrayList<OPGSurvey>) opgSurveys));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                                    long arg3) {

                                OPGSurvey surveys = opgSurveys.get(position);
                                //OpgSDkLite supports only online surveys.
                                if(surveys.isOffline())
                                {
                                    Toast.makeText(getApplicationContext(), "This is lite opgsdk. Offline survey will not work.", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    String surveyReference = surveys.getSurveyReference();
                                    Toast.makeText(getApplicationContext(), "" + surveyReference, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SurveyListPanelActivity.this, BrowseActivity.class);
                                    intent.putExtra("surveyReference", surveyReference);
                                    intent.putExtra("panelId",panelId);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                    else
                    {
                        ((TextView)findViewById(R.id.get_survey_output)).setText("No Surveys found for these credentials");
                        listView.setAdapter(new SurveyAdapter(SurveyListPanelActivity.this,(ArrayList<OPGSurvey>) opgSurveys));
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
