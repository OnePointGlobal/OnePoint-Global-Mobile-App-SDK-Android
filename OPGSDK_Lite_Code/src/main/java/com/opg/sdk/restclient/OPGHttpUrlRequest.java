package com.opg.sdk.restclient;

/**
 * 
 * This is model class for the OPGHttpUrlRequest .It contains all the details for making a HttpURLConnection request.
 *
 */
public class OPGHttpUrlRequest {

	String url,data,authKey,contentType,contentLength, sessionID;


	public OPGHttpUrlRequest(String url, String data, String authKey,
			String contentType, String contentLength, String sessionID) {
		super();
		this.url = url;
		this.data = data;
		this.authKey = authKey;
		this.contentType = contentType;
		this.contentLength = contentLength;
		this.sessionID=sessionID;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentLength() {
		return contentLength;
	}

	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}
