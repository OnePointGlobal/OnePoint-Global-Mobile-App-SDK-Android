package com.opg.sdk.restclient;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.opg.sdk.BuildConfig;
import com.opg.sdk.OPGPreference;
import com.opg.sdk.OPGSDKConstant;
import com.opg.sdk.OPGUploadProgress;
import com.opg.sdk.exceptions.OPGException;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.opg.sdk.OPGSDKConstant.BACK_SLASH;
import static com.opg.sdk.OPGSDKConstant.BOUNDARY;
import static com.opg.sdk.OPGSDKConstant.COLON;
import static com.opg.sdk.OPGSDKConstant.EMPTY_STRING;
import static com.opg.sdk.OPGSDKConstant.FORWARD_SLASH;
import static com.opg.sdk.OPGSDKConstant.HYPHEN;
import static com.opg.sdk.OPGSDKConstant.LINE_END;
import static com.opg.sdk.OPGSDKConstant.NEW_LINE;
import static com.opg.sdk.OPGSDKConstant.UTF;

public class OPGNetworkRequest {

	private static final int BUFFER_SIZE = 4096;
	private static final int CONNECT_TIMEOUT = 360000;
	private static final int READ_TIMEOUT = 360000;

	/**
	 * Creating an OPGHttpUrlRequest which has the parameters required for making the HttpURLConnection request
	 * @param reqEntity
	 * @param apiRoute
	 * @return
	 * @throws Exception
	 */
	public static OPGHttpUrlRequest createRequestParams(Context mContext,JSONObject reqEntity,String apiRoute) throws Exception {
		String url = OPGPreference.getApiURL(mContext)+ apiRoute;
		JSONObject data = new JSONObject();
		data.put(OPGSDKConstant.DATA, reqEntity.toString());
		String dataString    = data.toString();
		String contentLength = String.valueOf(dataString.getBytes().length);
		String contentType = null;
		if (apiRoute.equalsIgnoreCase(OPGSDKConstant.MEDIA_PROFILE_MEDIA))
		{
			contentType= OPGSDKConstant.APPLICATION_JSON_CHARSET;
		}
		else
		{
			contentType= OPGSDKConstant.APPLICATION_JSON;
		}

		String base64auth = getBase64Auth(mContext);
		return new OPGHttpUrlRequest(url, dataString, base64auth, contentType, contentLength);
	}

	/**
	 * If this is an mysurveys app we won't get the base 64 string.Else an base64 encode string is returned
	 * @param mContext
	 * @return
	 */
	public static  String getBase64Auth(Context mContext)
	{
		String authorisation = OPGPreference.getUsername(mContext)+COLON+ OPGPreference.getSharedKey(mContext);
		return OPGSDKConstant.BASIC+((Base64.encodeToString(authorisation.getBytes(),Base64.DEFAULT)).replace(NEW_LINE, EMPTY_STRING));
	}

	public static String performRequest(OPGHttpUrlRequest opgRequest) throws IOException, OPGException {
		String result = null;
		String output = null;
		URL url = new URL(opgRequest.getUrl());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(CONNECT_TIMEOUT);
		connection.setReadTimeout(READ_TIMEOUT);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod(OPGSDKConstant.POST);
		connection.setRequestProperty(OPGSDKConstant.USER_AGENT, OPGSDKConstant.USER_AGENT_NAME);
		connection.setRequestProperty(OPGSDKConstant.ACCEPT_LANGUAGE, OPGSDKConstant.ACCEPT_LANGUAGE_NAME);

		if(opgRequest.getAuthKey() != null)
		{
			connection.setRequestProperty(OPGSDKConstant.AUTHORIZATION, opgRequest.getAuthKey());
		}
		connection.setRequestProperty(OPGSDKConstant.CONTENT_TYPE, opgRequest.getContentType());
		connection.setRequestProperty(OPGSDKConstant.CONTENT_LENGTH, opgRequest.getContentLength());
		connection.setUseCaches (false);

		DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
		//wr.writeBytes(opgRequest.getData());
		wr.write(opgRequest.getData().getBytes(UTF));
		wr.flush();
		wr.close();

		int responseCode = connection.getResponseCode();
		/*if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
		}*/
		;
		if (responseCode >= HttpURLConnection.HTTP_OK && responseCode<HttpURLConnection.HTTP_BAD_REQUEST) {
			InputStream instream = connection.getInputStream();
			result = convertStreamToString(instream);
			output = result;
			instream.close();
		}
		else if(responseCode >= HttpURLConnection.HTTP_BAD_REQUEST)
		{
			InputStream instream = connection.getErrorStream();
			result = convertStreamToString(instream);
			output = result;
			instream.close();

		}
		else{
			throw new OPGException(OPGSDKConstant.UNKNOWN_EXCEPTION);
		}
		connection.disconnect();
		return output;
	}

	public static String uploadMediaRequest(Context mContext ,String apiRoute ,String AUTH_KEY ,String selectedFilePath,OPGUploadProgress uploadProgress,long folderSize) throws Exception {
		String output = null;
		String SERVER_URL = OPGPreference.getApiURL(mContext)+ apiRoute;
		HttpURLConnection connection = null;
		FileInputStream fileInputStream = null;
		DataOutputStream dataOutputStream = null;
		String lineEnd = LINE_END;
		String twoHyphens = HYPHEN+HYPHEN;
		String boundary = BOUNDARY;


		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File selectedFile = new File(selectedFilePath);
		selectedFilePath  = selectedFile.getAbsolutePath();
		long totalSize = folderSize;//(int)selectedFile.length();
		if(totalSize == 0)
		{
			totalSize = selectedFile.length();
		}


		String[] parts = selectedFilePath.split(FORWARD_SLASH);
		final String fileName = parts[parts.length - 1];
		try {
			fileInputStream = new FileInputStream(selectedFile);
			URL url = new URL(SERVER_URL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(CONNECT_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
			connection.setDoInput(true);//Allow Inputs
			connection.setDoOutput(true);//Allow Outputs
			connection.setUseCaches(false);//Don't use a cached Copy
			connection.setChunkedStreamingMode(1024);
			connection.setRequestMethod(OPGSDKConstant.POST);
			connection.setRequestProperty(OPGSDKConstant.CONNECTION, OPGSDKConstant.CONNECTION_TYPE);
			connection.setRequestProperty(OPGSDKConstant.ENCTYPE, OPGSDKConstant.ENCTYPE_NAME);
			connection.setRequestProperty(OPGSDKConstant.CONTENT_TYPE, OPGSDKConstant.CONTENT_TYPE_FOR_FILE_UPLOAD + boundary);
			connection.setRequestProperty(OPGSDKConstant.FILE, selectedFilePath);

			if(AUTH_KEY != null)
			{
				connection.setRequestProperty(OPGSDKConstant.AUTHORIZATION,AUTH_KEY);
			}

			//creating new dataoutputstream
			dataOutputStream = new DataOutputStream(connection.getOutputStream());

			//writing bytes to data outputstream
			dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
			dataOutputStream.writeBytes(OPGSDKConstant.CONTENT_DISPOSITION+ fileName + BACK_SLASH + lineEnd);

			dataOutputStream.writeBytes(lineEnd);

			//returns no. of bytes present in fileInputStream
			bytesAvailable = fileInputStream.available();

			//selecting the buffer size as minimum of available bytes or 1 MB
			bufferSize =  Math.min(bytesAvailable, maxBufferSize);
			//setting the buffer as byte array of size of bufferSize
			buffer = new byte[bufferSize];

			//reads bytes from FileInputStream(from 0th index of buffer to buffersize)
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			//loop repeats till bytesRead = -1, i.e., no bytes are left to read
			while (bytesRead > 0){
				//write the bytes read from inputstream
				dataOutputStream.write(buffer,0,bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable,maxBufferSize);
				bytesRead = fileInputStream.read(buffer,0,bufferSize);
			}

			dataOutputStream.writeBytes(lineEnd);
			dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			int responseCode = connection.getResponseCode();

			if (responseCode >= HttpURLConnection.HTTP_OK && responseCode<HttpURLConnection.HTTP_BAD_REQUEST) {
				InputStream instream = connection.getInputStream();
				output = convertStreamToString(instream);
				instream.close();
			}
			else if(responseCode >= HttpURLConnection.HTTP_BAD_REQUEST)
			{
				InputStream instream = connection.getErrorStream();
				output = convertStreamToString(instream);
				instream.close();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;

		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(connection!=null){
				connection.disconnect();
			}
			//closing the input and output streams
			if(fileInputStream!=null) {
				fileInputStream.close();
			}
			if(dataOutputStream!=null) {
				dataOutputStream.flush();
				dataOutputStream.close();
			}
		}
		return output;
	}

	/**
	 * Download the file
	 * @param mContext
	 * @param mediaID
	 * @param mediaType
	 * @param fileName
	 * @param filePath
	 * @return
	 * @throws Exception
	 */

	public static boolean downloadMediaRequest(Context mContext ,String mediaID,String mediaType ,String fileName,String filePath) throws Exception
	{
		boolean status= false;
		String fileURL = OPGPreference.getDownloadURL(mContext)+OPGSDKConstant.MEDIA_ID+mediaID+OPGSDKConstant.MEDIA_TYPE+mediaType;
		URL url        = new URL(fileURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setConnectTimeout(CONNECT_TIMEOUT);
		httpConn.setReadTimeout(READ_TIMEOUT);
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK)
		{
			String disposition = httpConn.getHeaderField(OPGSDKConstant.CONTENT_DISPOSITION);
			String contentType = httpConn.getContentType();
			int contentLength  = httpConn.getContentLength();
			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();

			if(contentType.contains(OPGSDKConstant.IMAGE) || contentType.contains(OPGSDKConstant.VIDEO) || contentType.contains(OPGSDKConstant.AUDIO))
			{
				if(inputStream!=null ){
					File pathFile = new File(filePath);
					if(!pathFile.exists()){
						pathFile.mkdirs();
					}

					String saveFilePath = pathFile.getAbsolutePath() + File.separator + fileName;

					// opens an output stream to save into file
					FileOutputStream outputStream = new FileOutputStream(saveFilePath);

					int bytesRead = -1;
					byte[] buffer = new byte[BUFFER_SIZE];
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}

					outputStream.close();
					inputStream.close();
					status = true;
				}
			}
			else
			{
				status = false;
				String str = convertStreamToString(inputStream);
				if (BuildConfig.DEBUG) {
					Log.i(EMPTY_STRING, str);
				}
			}
		} else {
			status = false;
		}
		httpConn.disconnect();
		return status;
	}

	/**
	 * Converting the inputstream to string
	 * @param is
	 * @return
	 */
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + NEW_LINE);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}


}
