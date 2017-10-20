/*
package OnePoint.CordovaPlugin;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.allatori.annotations.DoNotRename;
import com.opg.main.AndroidRuntimeInteracter;
import com.opg.main.OPGZipper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import OnePoint.Common.OPGSharedPreference;
import OnePoint.Logging.LogManager;
import OnePoint.POM.Model.PanelFactory;
import OnePoint.POM.Model.PanellistProfileFactory;
import OnePoint.PROM.Model.ISurvey;
import OnePoint.PROM.Model.SurveyFactory;
import OnePoint.Player.App.AppPlayer;
import OnePoint.Player.App.PlayerConstants;
import OnePoint.Player.Session.AppSession;
import OnePoint.Player.Session.InterviewSession;
import OnePoint.Runtime.Common.IOM.InterviewAction;
import OnePoint.Runtime.IPlayer;
import OnePoint.Runtime.Utils.FileUtility;

@DoNotRename
public class RuntimePlugin extends RootPlugin implements AndroidRuntimeInteracter
{
	static IPlayer player;
	static InterviewSession interviewSession;
	static AppSession appSession;
	static String callbackPage;
	private static String surveyID = "";
	private static String surveyName = "";
	private static String mediaFileFormat;
	private static String currentSurveyID;
	private static String currSurveyName, currSurveyID;
	private final String SURVEY = "script.class";
	private final String START = "start";
	private final String CONTINUE = "continue";
	Context context;
	private CallbackContext callback;


	private static IPlayer getPlayer() {
		return player;
	}


	public static void setPlayer(IPlayer player) {
		RuntimePlugin.player = player;
	}


	private static AppSession getAppSession() {
		return appSession;
	}


	public static void setAppSession(AppSession appSession) {
		RuntimePlugin.appSession = appSession;
	}


	private static InterviewSession getInterviewSession() {
		return interviewSession;
	}

	public static void setInterviewSession(InterviewSession interviewSession) {
		RuntimePlugin.interviewSession = interviewSession;
	}


	public static void setCurrentSurveyID(String currentSurveyID)
	{
		RuntimePlugin.currentSurveyID = currentSurveyID;
	}


	public static void setSurveyName(String surveyName) {
		RuntimePlugin.surveyName = surveyName;
	}


	public static void setSurveyID(String surveyID) {
		RuntimePlugin.surveyID = surveyID;
	}


	public static void setCurrSurveyID(String currSurveyID) {
		RuntimePlugin.currSurveyID = currSurveyID;
	}


	public static void setMediaFileFormat(String mediaFileFormat) {
		RuntimePlugin.mediaFileFormat = mediaFileFormat;
	}


	public static void setCurrSurveyName(String currSurveyName) {
		RuntimePlugin.currSurveyName = currSurveyName;
	}


	@Override@DoNotRename
	public boolean execute(String action, final CordovaArgs args, final CallbackContext callbackContext)
	{
		this.context = this.cordova.getActivity();
		init(context, callbackContext);
		this.callback = callbackContext;
		MediaPlugin.isOfflineMedia = true;
		System.out.print("Runtime Plugin ");
		try
		{
			if (action.equalsIgnoreCase(START))
			{
				Log.e("RuntimePlugin", "STARTING SURVEY");
				JSONObject obj = args.getJSONObject(0);
				surveyID = obj.get("surveyID").toString();
				surveyName = obj.get("surveyName").toString();

				callbackPage = obj.get("callbackPage").toString();
				// startSurvey(Long.parseLong(surveyID));
				new StartSurveyTask().execute();
			}
			else if (action.equalsIgnoreCase(CONTINUE))
			{
				Log.e("RuntimePlugin", "CONTINUE SURVEY");
				System.out.println("data : " + args.get(0).toString());
				JSONObject obj = new JSONObject(args.get(0).toString());
				processRequest(obj);
			}
			else
			{
				callback.error("Invalid Request");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LogManager.getLogger(getClass()).error(e.getMessage());
			callback.error("PLUGIN_ERROR:" + e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	private void startSurvey(long surveyID) throws Exception
	{
		PanellistProfileFactory panellistProfileFactory = new PanellistProfileFactory();
		PanelFactory panelFactory = new PanelFactory();
		SurveyFactory surveyFactory = new SurveyFactory();
		ISurvey survey = surveyFactory.findBySurveyID(surveyID).get(0);
		String surveyName = survey.getName();
		currSurveyName = surveyName;
		long panellistID = panellistProfileFactory.findAllObjects().get(0).getPanellistID();
		long panelID = panelFactory.findBySurvey(surveyID).get(0).getPanelID();
		long scriptID = survey.getScriptID();

		currentSurveyID = surveyID + "-" + panelID + "-" + panellistID;
		currSurveyID = currentSurveyID;

		OPGSharedPreference.setMediaFileFormat((Activity) context, currentSurveyID);

		File file = new File(Environment.getExternalStorageDirectory() + File.separator + "MySurveys" + File.separator + "Scripts" + File.separator + scriptID + ".opgs");

		String srcPath = file.getAbsolutePath();
		String destPath = context.getCacheDir() + "/" + surveyID + ".opgs";
		String destZip = destPath.replace(".opgs", ".zip");
		String destScript = destZip.replace(".zip", "");

		new OPGZipper().unzip(srcPath, destScript);

		callRuntime(surveyName, panellistID, surveyID, panelID);
	}


	public void writeFromSd(long surveyName)
	{
		try
		{
			File mySurveyFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MySurveys");
			if (!mySurveyFile.exists())
			{
				mySurveyFile.mkdir();
			}

			File myOPGDataFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MySurveys" + File.separator + "OPGData");

			if (!myOPGDataFile.exists())
			{
				myOPGDataFile.mkdir();
			}

			File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MySurveys" + File.separator + "OPGData" + File.separator + "opgsurveyIdData.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append("" + surveyName);
			myOutWriter.close();
			fOut.close();

		}
		catch (Exception e)
		{

		}
	}


	public void writeCacheToSd()
	{
		try
		{

			File mySurveyFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MySurveys");
			if (!mySurveyFile.exists())
			{
				mySurveyFile.mkdir();
			}

			File myOPGDataFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MySurveys" + File.separator + "OPGData");

			if (!myOPGDataFile.exists())
			{
				myOPGDataFile.mkdir();
			}

			File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MySurveys" + File.separator + "OPGData" + File.separator + "opgImgPathData.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append("" + context.getCacheDir());
			myOutWriter.close();
			fOut.close();

		}
		catch (Exception e)
		{

		}
	}


	private void callRuntime(String campaign, long panellistID, long surveyID, long panelID) throws Exception
	{
		writeCacheToSd();
		writeFromSd(surveyID);
		Log.e("RuntimePlugin", "CALLING RUNTIME");
		File file = null;

		file = new File(context.getCacheDir() + File.separator + surveyID + File.separator + SURVEY);

		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		for (int readNum; (readNum = fis.read(buf)) != -1;)
		{
			bos.write(buf, 0, readNum);
		}
		byte[] bytes = bos.toByteArray();
		AppPlayer.IS_LOCAL = false;
		player = new AppPlayer(context, this, context.getCacheDir() + File.separator + "TARGET_HTML.html");

		Log.e("RuntimePlugin", "PLAYER CREATED");
		appSession = new AppSession();
		interviewSession = appSession.createSession(campaign, bytes, context.getCacheDir() + File.separator + surveyID, FileUtility.loadLongFromAssetsStrings(PlayerConstants.LONG_STRING, context), player, panellistID,
				surveyID, panelID);
		Log.e("RuntimePlugin", "INTERVIEW SESSION CREATED");

		interviewSession.execute();
		Log.e("RuntimePlugin", "DONE");
		fis.close();
	}


	private void processRequest(JSONObject json) throws Exception
	{
		System.out.println("data : " + json.toString());
		JSONArray array = new JSONArray();
		for (int i = 0; i < json.names().length(); i++)
		{
			String name = json.names().getString(i);
			JSONObject temp = new JSONObject();
			temp.put(name, json.get(name));
			array.put(temp);
		}
		((AppPlayer) player).setAppResponse(array);
		player.updateContext(((AppPlayer) player).getAppContext());
		if (interviewSession.validate())
		{
			if (interviewSession.getHandler().getAction() == InterviewAction.Terminate)
			{

				((AppPlayer) player).updateUrl(true);
*/
/*
			File panellistCompleteDir = new File(Environment.getExternalStorageDirectory() + "/MySurveys/Completed/Completed_" + readPanelListCachePathFromDevice() + File.separator);
			int surveyResultConut = 0;
			if (panellistCompleteDir.exists() & panellistCompleteDir.isDirectory())
			{
				for (String fileName : panellistCompleteDir.list())
				{
					if (fileName.startsWith(currSurveyID))
					{
						surveyResultConut++;
					}
				}
			}*//*

				//OPGSharedPreference.storeCurrentSurvey((Activity) context, currSurveyID, currSurveyName, String.valueOf(surveyResultConut),readPanelListCachePathFromDevice());
				// OPGSharedPreference.setMediaCount((Activity)context, "0");
			}
			else
			{
				((AppPlayer) player).updateUrl(false);
			}
		}
	}

	@DoNotRename
	@Override
	public void postURL(String url)
	{
		System.out.println("@@@@@@@ RESULT @@@@" + url);
		if (url.endsWith("status=3"))
		{
			// Toast.makeText(context, "Status 3", Toast.LENGTH_SHORT).show();
			this.webView.loadUrl("file:///android_asset/www/applicationPage.html");
			// sendResult(callbackPage);
			// sendResult(url);
		}
		else
		{
			sendResult(url);
		}

	}


	class StartSurveyTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... arg0)
		{
			String response = "";
			try
			{
				startSurvey(Long.parseLong(surveyID));
			}
			catch (NumberFormatException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				// PluginResult pluginResult = new PluginResult(
				// PluginResult.Status.ERROR,
				// "Script is not available for this survey");
				// pluginResult.setKeepCallback(true);
				// callback.sendPluginResult(pluginResult);
				// e.printStackTrace();
				return response;
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);

		}
	}
}*/
