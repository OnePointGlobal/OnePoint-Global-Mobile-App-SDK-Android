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
import android.widget.ListView;
import android.widget.TextView;

import com.opg.sdk.models.OPGPanelPanellist;
import com.opg.sdk.models.OPGPanellistPanel;

import java.util.List;

/**
 * The type Get panelist panel.
 */
public class GetPanellistPanel extends AppCompatActivity {
    private TextView panelist_panel_output;
    private ProgressDialog progressDialog;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_panelist_panel);
        panelist_panel_output = (TextView) findViewById(R.id.panelist_panel_output);
        listView =(ListView) findViewById(R.id.panel_panelist_list);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        if(Util.isOnline(GetPanellistPanel.this))
        {
            new PanelistPanelTask().execute();
        }
        else
        {
            Util.showAlert(GetPanellistPanel.this);
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
    private class PanelistPanelTask extends AsyncTask<Void, Void, OPGPanellistPanel>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected OPGPanellistPanel doInBackground(Void... params)
        {
            try
            {
                // return the list of PanlistPanel
                if(Util.getOpgPanellistPanel() == null)
                {
                    OPGPanellistPanel panelPanelist = Util.getOPGSDKInstance().getPanellistPanel(getApplicationContext());
                    Util.setOpgPanellistPanel(panelPanelist);
                }

            }
            catch (final Exception ex)
            {
                Log.i("DemoApp", ex.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ((TextView)findViewById(R.id.panelist_panel_output)).setText(ex.getLocalizedMessage());
                    }
                });
            }
            return Util.getOpgPanellistPanel();
        }

        @Override
        protected void onPostExecute(  OPGPanellistPanel panelPanelist) {
            if(progressDialog != null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            if(panelPanelist != null)
            {
                if(panelPanelist.getPanelPanellistArray() != null && panelPanelist.getPanelPanellistArray().size()>0)
                {
                    listView.setAdapter(new PanelPanelistAdapter(GetPanellistPanel.this,panelPanelist.getPanelPanellistArray()));
                }
                else
                {
                    ((TextView)findViewById(R.id.panelist_panel_output)).setText(panelPanelist.getStatusMessage());
                }
            }
        }
    }
    private class  PanelPanelistAdapter extends ArrayAdapter<OPGPanelPanellist>
    {
        /**
         * The Opg themes.
         */
        List<OPGPanelPanellist> opgThemes;
        private Context context;

        /**
         * Instantiates a new Panel panelist adapter.
         *
         * @param context   the context
         * @param opgThemes the opg themes
         */
        public  PanelPanelistAdapter(Context context,  List<OPGPanelPanellist> opgThemes)
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
            OPGPanelPanellist panelPanelist = getItem(position);
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

            name.setText("PanelID :"+panelPanelist.getPanelID());
            description.setText("PanellistID : "+panelPanelist.getPanellistID());
            surveyid.setText("PanelPanellistID :"+panelPanelist.getPanelPanellistID());
            updateddate.setText("LastUpdated : "+panelPanelist.getLastUpdatedDate());
            // Return the completed view to render on screen
            return convertView;
        }
    }

}
