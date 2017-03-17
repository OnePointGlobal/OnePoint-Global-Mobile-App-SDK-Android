package com.onepointglobal.mysurveysn;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.opg.sdk.models.OPGPanel;
import com.opg.sdk.models.OPGPanellistPanel;
import com.opg.sdk.models.OPGSurveyPanel;

import java.util.List;

/**
 * The type Get survey panel activity.
 */
public class GetSurveyPanelActivity extends AppCompatActivity {

private ListView listView;
private TextView get_survey_panel_output;
private ProgressDialog progressDialog;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_get_survey_panel);
    listView = (ListView) findViewById(R.id.survey_panel_list);
    get_survey_panel_output = (TextView)findViewById(R.id.get_survey_panel_output);
    progressDialog   = new ProgressDialog(this);
    progressDialog.setIndeterminate(true);
    progressDialog.setCancelable(true);
    progressDialog.setMessage("Loading...");
    if(Util.isOnline(GetSurveyPanelActivity.this))
    {
        new SurveyPanelTask().execute();
    }
    else
    {
        Util.showAlert(GetSurveyPanelActivity.this);
    }

}

private class SurveyPanelTask extends AsyncTask<Void,Void,OPGPanellistPanel>
{
    @Override
    protected void onPreExecute()
    {
        progressDialog.show();
    }

    @Override
    protected OPGPanellistPanel doInBackground(Void... params)
    {
        List<OPGSurveyPanel>opgSurveyPanelArray= null;
        try
        {
            //get the list of survey panel
            if(Util.getOpgPanellistPanel() == null)
            {
                OPGPanellistPanel panelPanelist = Util.getOPGSDKInstance().getPanellistPanel(getApplicationContext());
                Util.setOpgPanellistPanel(panelPanelist);
            }
        }
        catch (final Exception ex)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    get_survey_panel_output.setText(ex.getLocalizedMessage());
                }
            });
            Log.i(GetSurveyPanelActivity.class.getName(),ex.getMessage());
        }

        return Util.getOpgPanellistPanel();
    }

    @Override
    protected void onPostExecute( OPGPanellistPanel panelPanelist)
    {
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        if(panelPanelist != null)
        {
            if(panelPanelist.getSurveyPanelArray() != null && panelPanelist.getSurveyPanelArray().size()>0)
            {
                listView.setAdapter(new SurveyPanelAdapter(GetSurveyPanelActivity.this,panelPanelist.getSurveyPanelArray()));
            }
            else
            {
                get_survey_panel_output.setText(panelPanelist.getStatusMessage());
            }
        }

    }
}
private class  SurveyPanelAdapter extends ArrayAdapter<OPGSurveyPanel>
{
    /**
     * The Opg themes.
     */
    List<OPGSurveyPanel> opgThemes;
    private Context context;

    /**
     * Instantiates a new Survey panel adapter.
     *
     * @param context   the context
     * @param opgThemes the opg themes
     */
    public  SurveyPanelAdapter(Context context,  List<OPGSurveyPanel> opgThemes)
    {
        super(context, 0, opgThemes);
        this.context=context;
        this.opgThemes = opgThemes;
    }
    @Override
    public int getCount()
    {
        return opgThemes.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        OPGSurveyPanel theme = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_theme, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.theme_name);
        TextView description = (TextView) convertView.findViewById(R.id.theme_description);
        TextView surveyid = (TextView) convertView.findViewById(R.id.themeid);
        TextView updateddate = (TextView) convertView.findViewById(R.id.theme_lastupdated);

        name.setText("PanelID : "+theme.getPanelID());
        description.setText("SurveyID :"+theme.getSurveyID());
        surveyid.setText("SurveyPanelID : "+theme.getSurveyPanelID());
        updateddate.setText("Created Date : "+theme.getCreatedDate());
        // Return the completed view to render on screen
        return convertView;
    }
}
}
