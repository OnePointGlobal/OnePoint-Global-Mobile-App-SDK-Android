package com.onepointglobal.mysurveysn;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.opg.sdk.models.OPGAuthenticate;
import com.opg.sdk.models.OPGScript;

public class NotificationActivity extends AppCompatActivity {
   private String action = null;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        String deviceToken =  FirebaseInstanceId.getInstance().getToken();
        ((TextView)findViewById(R.id.tv_fcm_token)).setText("FCM Device Token : "+deviceToken);
        progressDialog   = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");

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
    public void onRegistrClick(View view)
    {
        action = "register";
        MyAsyncTask myAsyncTask = new  MyAsyncTask();
        if(myAsyncTask.getStatus() != AsyncTask.Status.RUNNING)
        {
            myAsyncTask.execute();
        }
        /*new AsyncTask<Void,Integer,String>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids)
            {
                String response = null;
                try
                {
                    String deviceToken =  FirebaseInstanceId.getInstance().getToken();
                   response =  Util.getOPGSDKInstance().register(getApplicationContext(),deviceToken);

                }catch (Exception e)
                {
                    response = e.getMessage();
                    Log.i("REG",e.getMessage());
                }

                return response;
            }


            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                 TextView textView = (TextView) findViewById(R.id.register_response);

                 textView.setText(textView.getText().toString()+response);
            }
        }.execute();*/
    }



    public void onUnRegisterClick(View view)
    {
        action = "unRegister" +
                "";
        MyAsyncTask myAsyncTask = new  MyAsyncTask();
        if(myAsyncTask.getStatus() != AsyncTask.Status.RUNNING)
        {
            myAsyncTask.execute();
        }
        /*new AsyncTask<Void,Void,String>()
        {
            @Override
            protected String doInBackground(Void... voids)
            {
                String res = "...";
                try
                {
                    String deviceToken =  FirebaseInstanceId.getInstance().getToken();
                    res =  Util.getOPGSDKInstance().unRegister(getApplicationContext(),deviceToken);
                }catch (Exception e)
                {
                    res = e.getMessage();
                    Log.i("REG",e.getMessage());
                }

                return res;
            }

            @Override
            protected void onPostExecute(String res) {
                super.onPostExecute(res);
                TextView textView = (TextView) findViewById(R.id.un_register_response);
                 textView.setText("UnRegisterResponse : "+res);
            }
        }.execute();*/
    }

    private class MyAsyncTask extends AsyncTask<Void,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            String res = "...";
            try
            {
                String deviceToken =  FirebaseInstanceId.getInstance().getToken();
                if(action.equals("register"))
                {
                    res =  Util.getOPGSDKInstance().registerNotifications(getApplicationContext(),deviceToken);
                }
                else if(action.equals("unRegister"))
                {
                    res =  Util.getOPGSDKInstance().unRegisterNotifications(getApplicationContext(),deviceToken);
                }


            }catch (Exception e)
            {
                res = e.getMessage();
                Log.i("REG",e.getMessage());
            }

            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if(progressDialog!=null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            if(action.equals("register"))
            {
                TextView textView = (TextView) findViewById(R.id.register_response);

                textView.setText("Response : "+res);
            }
            else if(action.equals("unRegister"))
            {
                TextView textView = (TextView) findViewById(R.id.un_register_response);
                textView.setText("UnRegisterResponse : "+res);
            }

        }
    }
}

