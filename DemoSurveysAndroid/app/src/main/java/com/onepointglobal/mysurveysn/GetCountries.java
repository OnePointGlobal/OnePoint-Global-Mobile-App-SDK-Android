package com.onepointglobal.mysurveysn;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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

import com.opg.sdk.exceptions.OPGException;
import com.opg.sdk.models.OPGCountry;
import com.opg.sdk.models.OPGPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Get countries.
 */
public class GetCountries extends AppCompatActivity {
    private ListView listView;
    private TextView textView;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_countries);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        textView =(TextView) findViewById(R.id.country_output);
        progressDialog   = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        listView = (ListView) findViewById(R.id.countriesLV);
        if(Util.isOnline(GetCountries.this))
        {
            new GetCountriesTask().execute();
        }
        else
        {
            Util.showAlert(GetCountries.this);
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
    private class GetCountriesTask extends AsyncTask<Void,Void,ArrayList<OPGCountry>>{

        /**
         * The Exception.
         */
        OPGException exception;

        @Override
        protected void onPreExecute()
        {
            progressDialog.show();
        }

        @Override
        protected ArrayList<OPGCountry> doInBackground(Void... params) {
            ArrayList<OPGCountry> countries = null;
            try {
                countries = (ArrayList<OPGCountry>) Util.getOPGSDKInstance().getCountries(getApplicationContext());
            } catch (final OPGException ex) {
                ex.printStackTrace();
                Log.i(GetPanelActivity.class.getName(),ex.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        textView.setText(ex.getLocalizedMessage());
                    }
                });
            }
            return countries;
        }

        @Override
        protected void onPostExecute(ArrayList<OPGCountry> opgCountries) {
            super.onPostExecute(opgCountries);
            if(progressDialog!=null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            if(opgCountries != null)
            {
                if(opgCountries.size() > 0)
                {
                    listView.setAdapter(new CountryAdapter(getApplicationContext(),opgCountries));
                }
                else
                {
                    textView.setText("No countries found...");
                }
            }
        }
    }

    private class CountryAdapter extends ArrayAdapter<OPGCountry>{
        /**
         * The Opg countries.
         */
        List<OPGCountry> opgCountries;
        private Context context;

        /**
         * Instantiates a new Country adapter.
         *
         * @param context      the context
         * @param opgCountries the opg countries
         */
        public CountryAdapter(Context context, List<OPGCountry> opgCountries) {
            super(context, 0, opgCountries);
            this.opgCountries = opgCountries;
            this.context = context;
        }

        @Override
        public int getCount() {
            return opgCountries.size();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            {
                // Get the data item for this position
                OPGCountry theme = getItem(position);
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null)
                {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_listitem, parent, false);
                }
                // Lookup view for data population
                TextView name = (TextView) convertView.findViewById(R.id.textview1);
                name.setText(theme.getCountryName());
                return convertView;
            }
        }
    }

}
