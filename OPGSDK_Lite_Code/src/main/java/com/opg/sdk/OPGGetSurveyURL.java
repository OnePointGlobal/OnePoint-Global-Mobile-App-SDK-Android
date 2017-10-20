package com.opg.sdk;

import android.content.Context;
import android.provider.Settings.Secure;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.opg.sdk.OPGSDKConstant.AND;
import static com.opg.sdk.OPGSDKConstant.ANDROID;
import static com.opg.sdk.OPGSDKConstant.DATA;
import static com.opg.sdk.OPGSDKConstant.DATA1;
import static com.opg.sdk.OPGSDKConstant.DEVICE_ID;
import static com.opg.sdk.OPGSDKConstant.EMPTY_STRING;
import static com.opg.sdk.OPGSDKConstant.EQUAL;
import static com.opg.sdk.OPGSDKConstant.PANELID;
import static com.opg.sdk.OPGSDKConstant.PANELLISTID;
import static com.opg.sdk.OPGSDKConstant.PLATFORM;
import static com.opg.sdk.OPGSDKConstant.SEV;
import static com.opg.sdk.OPGSDKConstant.SEV_VALUE;
import static com.opg.sdk.OPGSDKConstant.SURVEY_REFERENCE;

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
 * OPGGetSurveyURL class is used to create a survey url.
 */
public class OPGGetSurveyURL {



	@SuppressWarnings("HardwareIds")
	protected String takeSurveyUrl(Context context, String surveyReference, long panelID, long panellistID, HashMap<String,String> queryParams)
	{
		String deviceID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		JSONObject json = new JSONObject();
		StringBuilder finalString = new StringBuilder();
		try {
			json.put(SURVEY_REFERENCE, surveyReference);
			if(panellistID!=0) {
				json.put(PANELLISTID, panellistID);
			}
			if(panelID!=0) {
				json.put(PANELID, panelID);
			}
			json.put(SEV, SEV_VALUE);
			json.put(PLATFORM, ANDROID);
			json.put(DEVICE_ID, deviceID);
			finalString.append(OPGPreference.getInterviewURL(context));
			finalString.append(DATA1);
			finalString.append(Aes256.encrypt(json.toString()));
			if(queryParams!=null){
				StringBuilder queryParamsStr = new StringBuilder();
				List<String> queryParamKeys = new ArrayList<>(queryParams.keySet());
				for (String key:queryParamKeys) {
					if(queryParamsStr.length()==0)
						queryParamsStr.append(key).append(EQUAL).append(queryParams.get(key));
					else
						queryParamsStr.append(AND).append(key).append(EQUAL).append(queryParams.get(key));
				}
				if(queryParamsStr.length()!=0){
					finalString.append(AND).append(queryParamsStr);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return finalString.toString().replace(OPGSDKConstant.NEW_LINE,EMPTY_STRING);
	}

}
