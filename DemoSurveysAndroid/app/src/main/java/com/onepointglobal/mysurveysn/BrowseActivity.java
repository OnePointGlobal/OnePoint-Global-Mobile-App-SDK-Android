package com.onepointglobal.mysurveysn;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.opg.sdk.OPGActivity;
import com.opg.sdk.OPGSurveyInterface;
import com.opg.sdk.exceptions.OPGException;
import com.opg.sdk.models.OPGPanellistPanel;
import com.opg.sdk.models.OPGSurvey;
import com.opg.sdk.models.OPGSurveyPanel;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * The type Browse activity.
 */
public class BrowseActivity extends OPGActivity implements OPGSurveyInterface {

    /**
     * The Survey reference.
     */
    private String  surveyReference;
    /**
     * The M context.
     */
    Context mContext;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Get SurveyId from Intent and pass it loadOnePointWebView as a params
         */

        mContext        = this;
        mProgressDialog = new ProgressDialog(mContext);
        surveyReference = getIntent().getStringExtra("surveyReference");

        mProgressDialog.setMessage("Loading question...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);

        new StartSurvey(surveyReference).execute();
    }

    /**
     * This Asynctask is used to load the online survey based on the panelID
     * and panellistID
     */
    public class StartSurvey extends AsyncTask<Void,Void,Boolean> {
        String surveyReference;
        long panelID = 0 , panellistID= 0;

        public StartSurvey(String surveyReference) {
            this.surveyReference = surveyReference;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Fecthing the panelID and panellistID...");
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //One can not use the methods of sdk without authentication
            if(Util.getOPGSDKInstance().getUniqueID(mContext) != null)
            {
                long surveyID = getSurveyID(surveyReference);
                panelID  = getPanelID(surveyID);
                panellistID = getPanellistID();
                Log.i("SurveyInfo","surveyReference : "+surveyReference+ ", surveyID : "+surveyID+", panelID : "+panelID+", panellistID : "+panellistID);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
                mProgressDialog.setMessage("Loading question...");
            }
            if (aBoolean) {
                loadOnlineSurvey(surveyReference, panelID, panellistID);
            } else {
                if (panelID == 0 || panellistID == 0) {
                    Toast.makeText(mContext, "PanelID or PanellistID is zero", Toast.LENGTH_LONG).show();
                    loadOnlineSurvey(surveyReference);

                } else {
                    Toast.makeText(mContext, "Unknown Exception", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    //Restoring of survey will not work in this case
    public void loadOnlineSurvey(String surveyReference){
        super.loadOnlineSurvey(this,surveyReference);
    }

    //Restoring will work
    public void loadOnlineSurvey(String surveyReference ,long panelID ,long panellistID){
        super.loadOnlineSurvey(this,surveyReference,panelID,panellistID);
    }

    /**
     * This method is used to get the PanelID based on the SurveyID.
     * It is recommended to call this method inside the async task as it may take some time
     * @param surveyID
     * @return
     */
    private long getPanelID(long surveyID) {
        OPGPanellistPanel opgPanellistPanel = Util.getOpgPanellistPanel();
        if(opgPanellistPanel != null)
        {
            opgPanellistPanel = Util.getOPGSDKInstance().getPanellistPanel(mContext);
            Util.setOpgPanellistPanel(opgPanellistPanel);
        }
        if(opgPanellistPanel != null)
        {
            List<OPGSurveyPanel> opgSurveyPanels = opgPanellistPanel.getSurveyPanelArray();
            for (OPGSurveyPanel opgSurveyPanel:opgSurveyPanels){
                if(opgSurveyPanel.getSurveyID() == surveyID){
                    return opgSurveyPanel.getPanelID();
                }
            }
        }
        return 0;
    }
    /**
     * This method is used to get the SurveyID based on the SurveyReference.
     * It is recommended to call this method inside the async task as it may take some time
     * @param surveyReference
     * @return
     */
    public long getSurveyID(String surveyReference){
        try {
            List<OPGSurvey> surveyList = Util.getOPGSDKInstance().getUserSurveyList(mContext);
            for (OPGSurvey opgSurvey:surveyList){
                if(opgSurvey.getSurveyReference().equals(surveyReference)){
                    return opgSurvey.getSurveyID();
                }
            }
        } catch (OPGException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * This method is used how to get the PanellistId using the opgsdk object
     * It is recommended to call this method inside the async task as it may take some time
     * @return
     */
    private long getPanellistID(){
        return Util.getOPGSDKInstance().getPanellistProfile(mContext).getPanellistID();
    }
    /**
     * Close progress dialog.
     */
    public void closeProgressDialog(){
        if(mProgressDialog!=null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    /**
     *  didSurveyCompleted  method called when done with all the questions.
     *  User can do required action steps after survey is completed.
     */

    public void didSurveyCompleted() {
        closeProgressDialog();
        Toast.makeText(getApplicationContext(), "Survey Completed", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * didSurveyFinishLoad called when webpage finished loading.
     */

    public void didSurveyFinishLoad() {
        closeProgressDialog();
    }

    /**
     * didSurveyStartLoad called when webpage is stated loading.
     */
    public void didSurveyStartLoad() {
        if(mProgressDialog!=null)
            mProgressDialog.show();
    }

@Override
public void onBackPressed() {
    super.onBackPressed();
    finish();
}

}

