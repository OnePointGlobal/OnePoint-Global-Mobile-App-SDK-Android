package OnePoint.CordovaPlugin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.allatori.annotations.DoNotRename;
import com.opg.sdk.BuildConfig;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import OnePoint.Common.OPGSharedPreference;
import OnePoint.CordovaPlugin.Utils.FileUtils;

import static com.opg.sdk.OPGSDKConstant.BLANK_SPACE;
import static com.opg.sdk.OPGSDKConstant.COLON;
import static com.opg.sdk.OPGSDKConstant.DOT;
import static com.opg.sdk.OPGSDKConstant.FORWARD_SLASH;
import static com.opg.sdk.OPGSDKConstant.HYPHEN;
import static com.opg.sdk.OPGSDKConstant.OPGSDK;
import static com.opg.sdk.OPGSDKConstant.ZERO;

//import org.apache.commons.io.FilenameUtils;

/*
import com.opg.main.OPGDBHelper;
*/
/*import OnePoint.Exceptions.InvalidMediaTypeException;
import OnePoint.Exceptions.InvalidSessionEception;
import OnePoint.RestClient.INetworkRequest.PARAM_TYPE;
import OnePoint.RestClient.Interfaces.IMediaDownloadRequest.MediaType;
import OnePoint.RestClient.NetworkError;
import OnePoint.RestClient.NetworkResult;
import OnePoint.RestClient.Requests.MediaDownloadRequest;
import OnePoint.RestClient.Requests.MediaDownloadResponse;
import OnePoint.RestClient.Requests.MediaUploadRequest;
import OnePoint.RestClient.Requests.MediaUploadResponse;*/

//import OnePoint.Logging.LogManager;
@DoNotRename
public class MediaPlugin extends RootPlugin implements ProgressUpdater {
    public static final String MEDIA_PREFERENCES = "MediaPrefs";
    public static final String MEDIA_FORMAT_KEY = "mediaformat";
    public static boolean isOfflineMedia = false;
    public static String MediaFileFormat;
    private final String MEDIA_PATH_KEY = "mediaPath";
	private final String COMMENTS_KEY = "comments";
	private final String MEDIA_TYPE_KEY = "mediaType";
	private final String MEDIA_ID_KEY = "mediaID";
	private final String ANDROID_IMAGE_FORMATS[] = { "jpg", "gif", "png", "bmp", "webp" };
	private final String ANDROID_AUDIO_FORMATS[] = { "3gp", "mp4", "m4a", "3ga", "3gpp", "aac", "ts", "flac", "mp3",
			"ogg", "mkv", "mid", "xmf", "mxmf", "rtttl", "rtx", "ota", "imy", "wav" };
	private final String ANDROID_VIDEO_FORMATS[] = { "3gp", "mp4", "ts", "mkv", "webm", };
    private final String URI = "uri";
    private final String OPG_SURVEYS_MEDIA = "/OPG_Surveys_Media/";
    private final String MEDIAID = "MediaID";
    private final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd_hh_mm_ss";
    private final String FAILED = "Failed";
    private final String PERCENT = "Percent";
    Context context;
	PluginResult pluginResult;
	// for upload
	String mediaPath;
	String comments;
	// for download
	String mediaType;
	String meidaID;
	SharedPreferences mediaFilePreferences;
    private CallbackContext callback;
    private ProgressDialog pDialog;

	@Override@DoNotRename
	public boolean execute(String action, final CordovaArgs args, final CallbackContext callbackContext) {
		this.context = this.cordova.getActivity();
		//OPGDBHelper.mContext = this.context;
		this.callback = callbackContext;

		try {
			JSONObject obj = args.getJSONObject(0);
			//System.out.println(obj.toString());
			if (action.equalsIgnoreCase(ACTION_MEDIA_UPLOAD + ACTION_NETWORK)) {
				if (isOfflineMedia)
					offlineUpload(obj);
				else
					onlineUpload(obj);
			} else if (action.equalsIgnoreCase(ACTION_MEDIA_UPLOAD + ACTION_DATABASE)) {
				if (isOfflineMedia)
					offlineUpload(obj);
				else
					onlineUpload(obj);
			} else if (action.equalsIgnoreCase(ACTION_MEDIA_DOWNLOAD + ACTION_NETWORK)) {
				// ONLINE DOWNLOAD
				if (isOfflineMedia)
					offlineDownload(obj);
				else
					onlineDownload(obj);
			} else if (action.equalsIgnoreCase(ACTION_MEDIA_DOWNLOAD + ACTION_DATABASE)) {
				// OFFLINE DOWNLOAD
				if (isOfflineMedia)
					offlineDownload(obj);
				else
					onlineDownload(obj);
			} else if (action.equalsIgnoreCase(ACTION_MEDIA_START_PLAYING_RECORDED_AUDIO)) {
				playRecording((Uri) obj.get(URI));
			} else if (action.equalsIgnoreCase(ACTION_MEDIA_UPLOAD + ACTION_FETCH_ABSELUTE_PATH)) {
				this.mediaPath = obj.get(MEDIA_PATH_KEY).toString();
				try {
					// mediaPath =
					// OnePoint.CordovaPlugin.Utils.FileUtils.getPath(
					// context, Uri.parse(mediaPath));
				} catch (Exception e) {
					mediaPath = mediaPath.replaceFirst(mediaPath.substring(0, mediaPath.indexOf(COLON)), "");
					mediaPath = mediaPath.replaceAll(COLON, "");
				}

				//PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,OPEN_CURLY_BRACKET+BACK_SLASH+PATH+BACK_SLASH+COLON+BACK_SLASH+ mediaPath + BACK_SLASH+CLOSE_CURLY_BRACKET);
				PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, FileUtils.getJSONPathObj(mediaPath));
				pluginResult.setKeepCallback(true);
				callback.sendPluginResult(pluginResult);
				isOfflineMedia =false;

			} else {
				callback.error(getReplyJsonString(102));
			}
		} catch (JSONException e) {
			//LogManager.getLogger(getClass()).error(e.getMessage());
			callback.error(getReplyJsonString(102));
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			//LogManager.getLogger(getClass()).error(e.getMessage());
			callback.error(e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	private void onlineUpload(JSONObject obj) throws JSONException {
		// ONLINE UPLOAD
		this.mediaPath = obj.get(MEDIA_PATH_KEY).toString();
		this.comments = obj.get(COMMENTS_KEY).toString();
		if (isOnline()) {
			new UploadOnlineMediaTask().execute();
		} else
			callback.error(getReplyJsonString(101));
	}

	private void onlineDownload(JSONObject obj) throws JSONException {
		// ONLINE DOWNLOAD
		this.mediaType = obj.get(MEDIA_TYPE_KEY).toString();
		this.meidaID = obj.get(MEDIA_ID_KEY).toString();
		if (meidaID.equalsIgnoreCase(ZERO)) {
			callback.error(getReplyJsonString(101));
		} else {
			if (!OPGSharedPreference.getMedia(meidaID, (Activity) context).equalsIgnoreCase("")) {
				//pluginResult = new PluginResult(PluginResult.Status.OK, OPEN_CURLY_BRACKET+BACK_SLASH+PATH+BACK_SLASH+COLON+BACK_SLASH+/* "{\"path\":\"" +*/ OPGSharedPreference.getMedia(meidaID, (Activity) context) + BACK_SLASH+CLOSE_CURLY_BRACKET /*"\"}"*/);
				pluginResult = new PluginResult(PluginResult.Status.OK, FileUtils.getJSONPathObj(OPGSharedPreference.getMedia(meidaID, (Activity) context)));
				pluginResult.setKeepCallback(true);
				callback.sendPluginResult(pluginResult);
			} else if (isOnline()) {
				//new GetMediaTask().execute();

			} else
				callback.error(getReplyJsonString(101));
		}
	}

	private void offlineUpload(JSONObject obj) throws JSONException {
		// OFFLINE UPLOAD
		this.mediaPath = obj.get(MEDIA_PATH_KEY).toString();
		this.comments = obj.get(COMMENTS_KEY).toString();
		// UploadOfflineMedia();

		File savDir = new File(context.getCacheDir() + OPG_SURVEYS_MEDIA);

		File srce = new File(mediaPath);
		if (!savDir.exists())
			savDir.mkdirs();
		String localMediaID = moveFile(srce.getParent() + FORWARD_SLASH, srce.getName(), savDir.getPath() + FORWARD_SLASH);
		JSONObject mediajson = new JSONObject();
		mediajson.put(MEDIAID, localMediaID);
		PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, mediajson.toString());
		pluginResult.setKeepCallback(true);
		callback.sendPluginResult(pluginResult);
	}

	public String moveFile(String inputPath, String inputFile, String outputPath) {

		InputStream in = null;
		OutputStream out = null;
		mediaFilePreferences = context.getSharedPreferences(MEDIA_PREFERENCES, Context.MODE_PRIVATE);

		String extn ="";// FilenameUtils.getExtension(inputFile);
		String mediaIdCallBack = null;

		try {

			File dir = new File(outputPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String mediaFile = OPGSharedPreference.getMediaFileFormat((Activity) context);
			Date today = new Date();
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH);
			String date = DATE_FORMAT.format(today);
			String newFileFormat = mediaFile + HYPHEN + UUID.randomUUID().toString().toUpperCase(Locale.US) + HYPHEN + date + DOT + extn;

			in = new FileInputStream(inputPath + inputFile);
			out = new FileOutputStream(outputPath + inputFile);
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;

			// write the output file
			out.flush();
			out.close();
			out = null;
			File from = new File(outputPath, inputFile);
			File to = new File(outputPath, newFileFormat);
			from.renameTo(to);

			// delete the original file
			new File(inputPath + inputFile).delete();
			//LogManager.getLogger(OPGJsonToModelParser.class).debug("PARSER - MediaAttachment: NORMAL");
			mediaIdCallBack =   newFileFormat;

		}

		catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(OPGSDK, e.getMessage());
            }
            callback.error(e.getLocalizedMessage());
		}
		return mediaIdCallBack;

	}

	private void offlineDownload(JSONObject obj) throws JSONException {
		// OFFLINE DOWNLOAD
		this.mediaType = obj.get(MEDIA_TYPE_KEY).toString();
		this.meidaID = obj.get(MEDIA_ID_KEY).toString();
		DownloadOfflineMedia();
	}


	private void DownloadOfflineMedia() {
		try {
			if (meidaID.equalsIgnoreCase(ZERO)) {
				callback.error(getReplyJsonString(101));
			} else {
				if (!OPGSharedPreference.getMedia(meidaID, (Activity) context).equalsIgnoreCase("")) {
					//pluginResult = new PluginResult(PluginResult.Status.OK, OPEN_CURLY_BRACKET + BACK_SLASH + PATH + BACK_SLASH + COLON + BACK_SLASH + /*"{\"path\":\"" +*/ OPGSharedPreference.getMedia(meidaID, (Activity) context) + BACK_SLASH + CLOSE_CURLY_BRACKET /*"\"}"*/);
					pluginResult = new PluginResult(PluginResult.Status.OK, FileUtils.getJSONPathObj(OPGSharedPreference.getMedia(meidaID, (Activity) context)));
					pluginResult.setKeepCallback(true);
					callback.sendPluginResult(pluginResult);
				} else
					callback.error(getReplyJsonString(101));
			}

		} catch (Exception e) {
			e.printStackTrace();
			//LogManager.getLogger(getClass()).error(e.getMessage());
			callback.error(e.getLocalizedMessage());
		} finally {
			//OPGDBHelper.setDatabaseName(OPGDBHelper.FRAMEWORK_DB);
		}
	}

    public void saveImage(String fileName, Bitmap bitmap) {
        // TODO Auto-generated method stub

	}

/*	// ONLINE_DOWNLOAD
	class GetMediaTask extends AsyncTask<Void, Void, MediaDownloadResponse> {

		@Override
		protected MediaDownloadResponse doInBackground(Void... arg0) {
			MediaDownloadResponse res = new MediaDownloadResponse();
			MediaDownloadRequest req = new MediaDownloadRequest(context);
			try {
				performDownload(req, res);
				OPGSharedPreference.setMedia(meidaID, res.getMediaPath(), (Activity) context);

			} catch (InvalidSessionEception e) {
				try {
					req.refreshSession();
					res.setError(new NetworkError(InvalidSessionEception.STATUS_CODE, e.getMessage()));
					//new GetMediaTask().execute();
				} catch (Exception e1) {
					e1.printStackTrace();
					callback.error("Failed : " + e.getLocalizedMessage());
				}

			} catch (Exception e) {
				res.setError(new NetworkError(999, e.getMessage()));
				e.printStackTrace();
			}
			return res;
		}

		@Override
		protected void onPostExecute(MediaDownloadResponse result) {
			super.onPostExecute(result);
			if (result.getError() == null) {

				pluginResult = new PluginResult(PluginResult.Status.OK, "{\"path\":\"" + result.getMediaPath() + "\"}");
				pluginResult.setKeepCallback(true);
				callback.sendPluginResult(pluginResult);
			} else if (result.getStatusCode() == 406) {
				new GetMediaTask().execute();
			} else if (result.getError().getErrorCode() != InvalidSessionEception.STATUS_CODE) {
				callback.error("Failed : " + result.getError().getMessage());
			}
		}

		private void performDownload(MediaDownloadRequest req, MediaDownloadResponse res) throws Exception {
			req.setSessionID(OPGSharedPreference.getSessionId((Activity) context));
			setMediaType(req);
			req.setMediaID(meidaID);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("Data", req.getEntity());
			req.setParams(PARAM_TYPE.QUERY, params);

			req.createRequestForDownload();

			//LogManager.getLogger(getClass()).debug("Media Download Result:" + req.getUrl());
			InputStream in = new java.net.URL(req.getUrl()).openStream();
			final Bitmap mIcon11 = BitmapFactory.decodeStream(in);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mIcon11.compress(Bitmap.CompressFormat.PNG, 75, stream);
			byte[] byteArray = stream.toByteArray();
			File file = getNewFile();
			FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
			fos.write(byteArray);
			fos.close();
			res.setMediaPath(file.getAbsolutePath());
		}

		private void setMediaType(MediaDownloadRequest req) throws Exception {
			if (mediaType != null && !mediaType.equalsIgnoreCase("")) {
				if (mediaType.equalsIgnoreCase("IMAGE")) {
					req.setMediaType(MediaType.IMAGE);
				} else if (mediaType.equalsIgnoreCase("AUDIO")) {
					req.setMediaType(MediaType.AUDIO);
				} else if (mediaType.equalsIgnoreCase("VIDEO")) {
					req.setMediaType(MediaType.VIDEO);
				} else {
					throw new InvalidMediaTypeException();
				}
			} else {
				throw new InvalidMediaTypeException();
			}
		}
	}

	private File getNewFile() throws InvalidMediaTypeException {
		File vitaccessMediaDir = new File(Environment.getExternalStorageDirectory() + "/Vitaccess/Media/");
		File vitaccessMediaImageDir = new File(vitaccessMediaDir + "/Opg Image/");
		if (!vitaccessMediaImageDir.exists()) {
			vitaccessMediaImageDir.mkdir();
		}
		String format = String.format(Locale.ENGLISH, "%%0%dd", 3);
		int count = OPGSharedPreference.getCameraFileCount((Activity) this.context);
		Date today = new Date();
		SimpleDateFormat DATE_FORMAT_T = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
		String date = DATE_FORMAT_T.format(today);
		String fileName = "IMG-" + date + "-OPG" + String.format(format, count);

		if (mediaType != null && !mediaType.equalsIgnoreCase("")) {
			if (mediaType.equalsIgnoreCase("IMAGE")) {
				fileName = fileName + ".jpeg";
			} else if (mediaType.equalsIgnoreCase("AUDIO")) {
				fileName = fileName + ".mp4";
			} else if (mediaType.equalsIgnoreCase("VIDEO")) {
				fileName = fileName + ".mp4";
			} else {
				throw new InvalidMediaTypeException();
			}
		}
		File file = new File(vitaccessMediaImageDir, fileName);
		OPGSharedPreference.incrementCameraFileCount((Activity) this.context);
		return file;
	}*/

	@Override
	public void updateProgress(String update, long progress, long totalSize) {
		int percent = ((int) ((progress / (float) totalSize) * 100));
		//System.out.println("MediaPlugin-" + update + percent + " %");
		if (callback != null) {
			JSONObject resultJSON = new JSONObject();
			try {
				resultJSON.put(PERCENT,percent);
			}catch (Exception e){

			}
			//pluginResult = new PluginResult(PluginResult.Status.OK,  OPEN_CURLY_BRACKET + BACK_SLASH+PERCENT+BACK_SLASH+COLON+BACK_SLASH+COLON /*"{\"Percent\":\""*/ + percent + BACK_SLASH+CLOSE_CURLY_BRACKET /*"\"}"*/);
			pluginResult = new PluginResult(PluginResult.Status.OK,  resultJSON.toString());
			pluginResult.setKeepCallback(true);
			callback.sendPluginResult(pluginResult);
			// callback.success("update:"+percent);
		}
	}

	private void playRecording(Uri audioFileUri) {
		MediaPlayer mediaPlayer = MediaPlayer.create(context, audioFileUri);
		mediaPlayer.start();
		callback.success(getReplyJsonString(100));
    }

    // ONLINE_UPLOAD
    class UploadOnlineMediaTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(Void... arg0) {

            String response = null;
            try {
                String mediaID = new com.opg.sdk.OPGSDK().uploadMediaFile(mediaPath, context);
                response = mediaID + COLON + mediaPath;
            } catch (Exception e) {
                exception = e;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                String[] response = result.split(COLON);
                if (response.length == 2) {
                    /*PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, OPEN_CURLY_BRACKET+BACK_SLASH+MEDIAID+BACK_SLASH+COLON *//*"{\"MediaID\":\""*//* + response[0]
							+  BACK_SLASH+COMA +BACK_SLASH+PATH+COLON+BACK_SLASH *//*"\", \"path\":\""*//* + response[1] + BACK_SLASH+CLOSE_CURLY_BRACKET  *//*"\"}"*//*);*/
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, FileUtils.getJSONMediaPathObj(response[0], response[1]));
                    pluginResult.setKeepCallback(true);
                    callback.sendPluginResult(pluginResult);
                } else {
                    callback.error(FAILED + BLANK_SPACE + COLON + result);
                }

            } else {
                if (exception != null) {
                    callback.error(FAILED + BLANK_SPACE + COLON + exception.getMessage());
                }
            }
        }
	}
}
