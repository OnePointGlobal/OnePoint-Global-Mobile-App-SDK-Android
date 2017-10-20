package com.opg.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.allatori.annotations.DoNotRename;

import org.apache.cordova.OPGBaseActivity;

import java.util.HashMap;

import OnePoint.CordovaPlugin.MediaPlugin;

import static com.opg.sdk.OPGSDKConstant.MSG_SDK_LITE_KEY;
import static com.opg.sdk.OPGSDKConstant.SDK_LITE_KEY;
import static com.opg.sdk.OPGSDKConstant.STRING;



///////////////////////////////////////////////////////////////////////////////
//
//Copyright (c) 2016 OnePoint Global Ltd. All rights reserved.
//
//This code is licensed under the OnePoint Global License.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.
//
///////////////////////////////////////////////////////////////////////////////

/**
 * This activity is the base activity for displaying the survey.
 * User activity need to extend this activity to display the survey.
 */
@SuppressLint({"SetJavaScriptEnabled", "NewApi"})
@DoNotRename
public class OPGActivity extends OPGBaseActivity{

    private Context context;
    private String surveyUrl;
    private OPGGetSurveyURL opgSurveyUrl;
    private OPGSurveyInterface opgViewInterface;
    private String surveyReference;
    private long panellistId=0,panelId=0;
    private HashMap<String, String> queryParams = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init();
        context = this;
    }

    /***
     * This is method is called once the survey starts loading
     */
    @Override
    protected void onQuestionStartLoading() {
        // TODO Auto-generated method stub
        if(opgViewInterface != null) {
            opgViewInterface.didSurveyStartLoad();
        }
    }

    /***
     * This is method is called once the survey stops loading
     */
    @Override
    protected void onQuestionStopLoading() {
        // TODO Auto-generated method stub
        if(opgViewInterface != null) {
            opgViewInterface.didSurveyFinishLoad();
        }
    }

    /***
     * This is method is called once the survey is completed
     */
    @Override
    protected void onSurveyCompleted() {
        // TODO Auto-generated method stub
        if(opgViewInterface != null) {
            opgViewInterface.didSurveyFinishLoad();
            opgViewInterface.didSurveyCompleted();
        }
    }

    /**
     * This method loads the URL to run the survey.
     *
     * @param opgViewInterface
     * @param surveyReference  This parameter is used to construct the URL which is used to load the survey.
     */
    @DoNotRename
    public void loadOnlineSurvey(OPGSurveyInterface opgViewInterface, String surveyReference) {
        MediaPlugin.isOfflineMedia = false;
        this.opgViewInterface = opgViewInterface;
        this.surveyReference = surveyReference;
        loadSurveyLink();
    }

    /**
     * This method loads the URL to run the survey.
     * @param opgViewInterface
     * @param surveyReference
     * @param panelID
     * @param panellistID
     */
    @DoNotRename
    public void loadOnlineSurvey(OPGSurveyInterface opgViewInterface, String surveyReference, long panelID, long panellistID) {
        MediaPlugin.isOfflineMedia = false;
        this.opgViewInterface = opgViewInterface;
        this.surveyReference = surveyReference;
        this.panelId = panelID;
        this.panellistId = panellistID;
        loadSurveyLink();
    }

    /**
     * This method loads the URL to run the survey.
     *
     * @param opgViewInterface
     * @param surveyReference
     * @param queryParams
     */
    @DoNotRename
    public void loadOnlineSurvey(OPGSurveyInterface opgViewInterface, String surveyReference, HashMap<String, String> queryParams) {
        MediaPlugin.isOfflineMedia = false;
        this.opgViewInterface = opgViewInterface;
        this.surveyReference = surveyReference;
        this.queryParams = queryParams;
        loadSurveyLink();
    }

    /**
     * This method loads the URL to run the survey .
     * @param opgViewInterface
     * @param surveyReference
     * @param panelID
     * @param panellistID
     * @param queryParams
     */
    @DoNotRename
    public void loadOnlineSurvey(OPGSurveyInterface opgViewInterface, String surveyReference, long panelID, long panellistID, HashMap<String, String> queryParams) {
        MediaPlugin.isOfflineMedia = false;
        this.opgViewInterface = opgViewInterface;
        this.surveyReference = surveyReference;
        this.queryParams = queryParams;
        this.panelId = panelID;
        this.panellistId = panellistID;
        loadSurveyLink();
    }


    @DoNotRename
    public void loadOfflineSurvey(OPGSurveyInterface opgViewInterface, Context context, String surveyName, String scriptPath, long surveyID, long panellistId, long panelId)
    {
    }

    private void loadSurveyLink(){
            opgSurveyUrl = new OPGGetSurveyURL();
            surveyUrl = opgSurveyUrl.takeSurveyUrl(context, surveyReference, panelId, panellistId, queryParams);
            loadUrl(surveyUrl);
    }
}
