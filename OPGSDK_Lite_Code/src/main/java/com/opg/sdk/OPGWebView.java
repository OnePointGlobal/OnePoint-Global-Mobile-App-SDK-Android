package com.opg.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;

import static com.opg.sdk.OPGSDKConstant.STATUS_3;
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
 *
 * OPGWebview class.
 *
 */

@SuppressLint("SetJavaScriptEnabled")
public class OPGWebView extends WebView {
private Context context;
private String surveyUrl;
OPGGetSurveyURL opgSurveyUrl;
OPGSurveyInterface opgViewInterface;
OPGWebView opgWebView;

public OPGWebView(Context context) {
	super(context);
	this.context = context;

}

public OPGWebView(Context context, AttributeSet attrs) {
	// TODO Auto-generated constructor stub
	super(context, attrs);
	this.context = context;

}

/**
 * This method loads the URL to run the survey.
 * @param opgViewInterface
 * @param surveyReference This parameter is used to construct the URL which will be later loaded on to the WebView.
 */
public void loadOPGWebView(OPGSurveyInterface opgViewInterface,
					  String surveyReference) {
	this.opgViewInterface = opgViewInterface;
	getSurveyLink(surveyReference,null);
}

/**
 * This method loads the URL to run the survey.
 * @param opgViewInterface
 * @param surveyReference
 * @param params
 */
public void loadOPGWebView(OPGSurveyInterface opgViewInterface,
					  String surveyReference, HashMap<String,String> params) {
	this.opgViewInterface = opgViewInterface;
	getSurveyLink(surveyReference,params);
}

private void getSurveyLink(String surveyReference ,HashMap<String,String> params) {
	opgSurveyUrl = new OPGGetSurveyURL();
	surveyUrl    = opgSurveyUrl.takeSurveyUrl(context, surveyReference, 0, 0, params);
	this.loadUrl(surveyUrl);

	this.getSettings().setJavaScriptEnabled(true);
	this.setWebViewClient(new OPGWebViewClient(opgViewInterface));
}

public class OPGWebViewClient extends WebViewClient {
	OPGSurveyInterface opginterface;

	public OPGWebViewClient(OPGSurveyInterface opgViewInterface) {
		this.opginterface = opgViewInterface;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		return false;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		opginterface.didSurveyStartLoad();

	}

	@Override
	public void onPageFinished(WebView view, String url) {
		if (url.contains(STATUS_3)) {
			opginterface.didSurveyCompleted();
		} else {
			opginterface.didSurveyFinishLoad();
		}

	}

}
}
