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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.opg.sdk.models.OPGPanellistPanel;
import com.opg.sdk.models.OPGTheme;

import java.util.List;

/**
 * The type Get theme activity.
 */
public class GetThemeActivity extends AppCompatActivity {
private ListView listView;
private ProgressDialog progressDialog;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_get_theme);
    listView = (ListView) findViewById(R.id.themelist);
    progressDialog   = new ProgressDialog(this);
    progressDialog.setIndeterminate(true);
    progressDialog.setCancelable(true);
    progressDialog.setMessage("Loading...");
    if(Util.isOnline(GetThemeActivity.this))
    {
        new ThemeTask().execute();
    }
    else
    {
        Util.showAlert(GetThemeActivity.this);
    }

}

private class ThemeTask extends AsyncTask<Void,Void,OPGPanellistPanel>
{
    @Override
    protected void onPreExecute()
    {
        progressDialog.show();
    }

    @Override
    protected OPGPanellistPanel doInBackground(Void... params)
    {
        try
        {
            //to get the theme list
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

                    ((TextView)findViewById(R.id.theme_response)).setText(ex.getLocalizedMessage());
                }
            });
            Log.i(GetThemeActivity.class.getName(),ex.getMessage());
        }

        return Util.getOpgPanellistPanel();
    }

    @Override
    protected void onPostExecute( OPGPanellistPanel panelPanelist )
    {
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        if(panelPanelist != null)
        {
            if(panelPanelist.getThemeArray() != null && panelPanelist.getThemeArray().size()>0)
            {
                listView.setAdapter(new ThemeAdapter(GetThemeActivity.this,panelPanelist.getThemeArray()));
            }
            else
            {
                ((TextView)findViewById(R.id.theme_response)).setText(panelPanelist.getStatusMessage());
            }
        }

    }
}
private class  ThemeAdapter extends ArrayAdapter<OPGTheme>
{
    /**
     * The Opg themes.
     */
    List<OPGTheme> opgThemes;
    private Context context;

    /**
     * Instantiates a new Theme adapter.
     *
     * @param context   the context
     * @param opgThemes the opg themes
     */
    public  ThemeAdapter(Context context,  List<OPGTheme> opgThemes)
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
        OPGTheme theme = getItem(position);
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

        name.setText("Name : "+theme.getName());
        description.setText("Value : "+theme.getValue());
        surveyid.setText("themeID : "+theme.getThemeID()+"");
        updateddate.setText("updated : "+theme.getLastUpdatedDate());
        // Return the completed view to render on screen
        return convertView;
    }
}


}
