package com.onepointglobal.mysurveysn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opg.sdk.models.OPGPanel;
import com.opg.sdk.models.OPGPanellistPanel;
import com.opg.sdk.models.OPGSurvey;
import com.opg.sdk.models.OPGTheme;

import java.util.List;

/**
 * The type Get panel activity.
 */
public class GetPanelActivity extends AppCompatActivity {

    private ListView listView;
    private TextView textView;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_panel);
        textView =(TextView) findViewById(R.id.panel_output);
        progressDialog   = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        listView = (ListView) findViewById(R.id.panellist);
        if(Util.isOnline(GetPanelActivity.this))
        {
            new PanelTask().execute();
        }
        else
        {
            Util.showAlert(GetPanelActivity.this);
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
    private class PanelTask extends AsyncTask<Void,Void,OPGPanellistPanel>
    {
        @Override
        protected void onPreExecute()
        {
            progressDialog.show();
        }

        @Override
        protected OPGPanellistPanel doInBackground(Void... params)
        {
            List<OPGPanel>opgPanelList = null;
            try
            {
                // return the list panels
                if(Util.getOpgPanellistPanel() == null)
                {
                    OPGPanellistPanel panelPanelist = Util.getOPGSDKInstance().getPanellistPanel(getApplicationContext());
                    Util.setOpgPanellistPanel(panelPanelist);
                }
            }
            catch (final Exception ex)
            {
                Log.i(GetPanelActivity.class.getName(),ex.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        textView.setText(ex.getLocalizedMessage());
                    }
                });
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
                final List<OPGPanel> opgPanels = panelPanelist.getPanelArray();
                if( panelPanelist.getThemeArray() != null && opgPanels.size() >0)
                {

                    listView.setAdapter(new PanelAdapter(GetPanelActivity.this,panelPanelist.getPanelArray()));
                    /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapter, View v, int position,long arg3) {

                            OPGPanel  opgPanel = opgPanels.get(position);
                            Intent intent = new Intent(GetPanelActivity.this,SurveyListPanelActivity.class);
                            intent.putExtra("panelId",opgPanel.getPanelID());
                            startActivity(intent);

                        }
                    });*/
                }
                else
                {
                    textView.setText(panelPanelist.getStatusMessage());
                }
            }
        }
    }
    private class  PanelAdapter extends ArrayAdapter<OPGPanel>
    {
        /**
         * The Opg themes.
         */
        List<OPGPanel> opgThemes;
        private Context context;

        /**
         * Instantiates a new Panel adapter.
         *
         * @param context   the context
         * @param opgThemes the opg themes
         */
        public  PanelAdapter(Context context,  List<OPGPanel> opgThemes)
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
            OPGPanel theme = getItem(position);
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

            name.setText("Name :"+theme.getName());
            description.setText("Panel Type :"+theme.getPanelType());
            surveyid.setText("Remark : "+theme.getRemark());
            updateddate.setText("Last updated : "+theme.getLastUpdatedDate());
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
