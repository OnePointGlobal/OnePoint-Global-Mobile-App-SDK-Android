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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opg.sdk.models.OPGPanel;
import com.opg.sdk.models.OPGPanelPanellist;
import com.opg.sdk.models.OPGPanellistPanel;
import com.opg.sdk.models.OPGTheme;

import java.util.List;

/**
 * The type Get panelist panel.
 */
public class GetPanelistPanel extends AppCompatActivity {
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
        if(Util.isOnline(GetPanelistPanel.this))
        {
            new PanelistPanelTask().execute();
        }
        else
        {
            Util.showAlert(GetPanelistPanel.this);
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
                    listView.setAdapter(new PanelPanelistAdapter(GetPanelistPanel.this,panelPanelist.getPanelPanellistArray()));
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
            description.setText("PanelistID : "+panelPanelist.getPanellistID());
            surveyid.setText("PanelPanelistID :"+panelPanelist.getPanelPanellistID());
            updateddate.setText("LastUpdated : "+panelPanelist.getLastUpdatedDate());
            // Return the completed view to render on screen
            return convertView;
        }
    }

}
