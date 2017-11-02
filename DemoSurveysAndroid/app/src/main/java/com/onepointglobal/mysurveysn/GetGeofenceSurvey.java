package com.onepointglobal.mysurveysn;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.allatori.annotations.DoNotRename;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.location.LocationServices;
import com.opg.sdk.models.OPGGeofenceSurvey;

import java.util.ArrayList;
import java.util.List;

public class GetGeofenceSurvey extends AppCompatActivity implements LocationListener,ConnectionCallbacks, OnConnectionFailedListener {
    private ProgressDialog pDialog;
    private  ListView listView;
    private TextView tvLocation;
    protected GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    String lang;
    String lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_geofence_survey);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        listView = (ListView) findViewById(R.id.list);
        tvLocation = (TextView)findViewById(R.id.tv);

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
    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mCurrentLocation != null)
        {
            lang = Double.toString(mCurrentLocation.getLongitude()) ;
            lat = Double.toString(mCurrentLocation.getLatitude());
            tvLocation.setText("Location : "+lang+","+lat);
        }
    }
    protected void onStart() {
        if(mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    protected void onStop() {

        if(mGoogleApiClient != null)
        {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        lang = Double.toString(location.getLongitude()) ;
        lat = Double.toString(location.getLatitude());
        tvLocation.setText("Location : "+lang+","+lat);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override@DoNotRename
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override@DoNotRename
    public void onConnected(Bundle arg0) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        lang = Double.toString(mCurrentLocation.getLongitude()) ;
        lat = Double.toString(mCurrentLocation.getLatitude());
        tvLocation.setText("My Location : "+lang+","+lat);

    }

    @Override@DoNotRename
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();

    }
    public void onSubmitClick(View view)
    {
        if (Util.isOnline(this))
        {
            listView.invalidate();
            new GetGeofenceSurvey.SurveyListTask().execute(lat,lang);

        }
        else
        {
            Util.showAlert(this);
        }

    }
    private class SurveyListTask extends AsyncTask<String, Void, List<OPGGeofenceSurvey>>
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
        protected List<OPGGeofenceSurvey> doInBackground(String... params)
        {
            List<OPGGeofenceSurvey> surveyList =new ArrayList<OPGGeofenceSurvey>();
            try
            {
                if(params.length > 0 && params[0] != null &&  params[1] != null )
                {
                    surveyList = Util.getOPGSDKInstance().getGeofenceSurveys(getApplicationContext(),Float.parseFloat(params[0]),Float.parseFloat(params[1]));
                }

            }
            catch (final Exception e)
            {
                e.printStackTrace();
                exceptionObj = e;
            }
            return surveyList;
        }

        protected void onPostExecute(final List<OPGGeofenceSurvey> opgSurveys)
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
                    listView.setAdapter(new GeofenceSurveyAdapter(getApplicationContext(),(ArrayList<OPGGeofenceSurvey>) opgSurveys));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapter, View v, int position,
                                                long arg3) {
                            OPGGeofenceSurvey surveys = opgSurveys.get(position);
                            String surveyReference = surveys.getSurveyReference();
                            Toast.makeText(getApplicationContext(), "" + surveyReference, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), BrowseActivity.class);
                            intent.putExtra("surveyReference", surveyReference);
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Surveys found in this location",Toast.LENGTH_SHORT).show();
                   // ((TextView)findViewById(R.id.get_survey_output)).setText("No Surveys found for these credentials");
                    //listView.setAdapter(new SurveyAdapter(getApplicationContext(),(ArrayList<OPGGeofenceSurvey>) opgSurveys));
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(),"No Surveys found in this location",Toast.LENGTH_SHORT).show();
               // ((TextView)findViewById(R.id.get_survey_output)).setText("No Surveys found for these credentials");
            }

        }
    }

    private class GeofenceSurveyAdapter extends ArrayAdapter<OPGGeofenceSurvey>
    {
        private  Context context;
        private ArrayList<OPGGeofenceSurvey> opgSurveys;
        GeofenceSurveyAdapter(Context context, ArrayList<OPGGeofenceSurvey> opgSurveys)
        {
            super(context, 0, opgSurveys);
            this.context = context;
            this.opgSurveys = opgSurveys;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            OPGGeofenceSurvey surveys = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_surveys, parent, false);
            }
            // Lookup view for data population
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            description.setText("Address : "+surveys.getAddress());
            TextView surveyid = (TextView) convertView.findViewById(R.id.surveyid);
            TextView updateddate = (TextView) convertView.findViewById(R.id.lastupdated);
            updateddate.setText("");
            ImageView imv  = (ImageView) convertView.findViewById(R.id.thumbnail);
            TextView osOnline = (TextView) convertView.findViewById(R.id.isOnline);
            osOnline.setText("");
            // Populate the data into the template view using the data object
            name.setText(surveys.getSurveyName());
            surveyid.setText(surveys.getSurveyReference());
            //updateddate.setText(surveys.getLastUpdatedDate());
            imv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bkgnd));
            // Return the completed view to render on screen
            return convertView;
        }

    }
}
