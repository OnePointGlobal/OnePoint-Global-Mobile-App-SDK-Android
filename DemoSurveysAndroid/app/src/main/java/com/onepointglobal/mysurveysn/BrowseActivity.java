package com.onepointglobal.mysurveysn;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.opg.sdk.OPGActivity;
import com.opg.sdk.OPGSurveyInterface;

import java.util.HashMap;

/**
 * The type Browse activity.
 */
public class BrowseActivity extends OPGActivity implements OPGSurveyInterface {

    /**
     * The Survey reference.
     */
    String  surveyReference;
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
        HashMap<String,String> queryParams = new HashMap<>();

        queryParams.put("user1","Hi");
        queryParams.put("user2","I");
        queryParams.put("user3","am");
        queryParams.put("user4","Prabeen");
        queryParams.put("user5","1234");
        //Loading the questions for  particular survey and passing the  queryParams
        super.loadSurvey(this,surveyReference,queryParams);
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

