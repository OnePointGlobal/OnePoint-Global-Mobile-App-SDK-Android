package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;
import com.google.gson.annotations.SerializedName;

/**
 * This class is the model class for the OPGAuthenticate.
 */
@DoNotRename
final public class OPGAuthenticate implements Parcelable {
    @DoNotRename
    public static final Creator<OPGAuthenticate> CREATOR = new Creator<OPGAuthenticate>() {
        @Override
        public OPGAuthenticate createFromParcel(Parcel in) {
            return new OPGAuthenticate(in);
        }

        @Override
        public OPGAuthenticate[] newArray(int size) {
            return new OPGAuthenticate[size];
        }
    };
    @SerializedName("Url")
    private String url;
    @SerializedName("InterviewUrl")
    private String interviewUrl;
    @SerializedName("UniqueID")
    private String uniqueID;
    @SerializedName("Message")
    private String message;
    @SerializedName("HttpStatusCode")
    private long httpStatusCode;

    private String statusMessage;
    private boolean isSuccess;

    private OPGAuthenticate(Parcel in) {
        uniqueID = in.readString();
        url = in.readString();
        interviewUrl = in.readString();
        message = in.readString();
        statusMessage = in.readString();
        isSuccess = in.readByte() != 0;
        httpStatusCode = in.readLong();
    }

    public OPGAuthenticate() {

    }

    @DoNotRename
    public String getUniqueID() {
        return uniqueID;
    }

    @DoNotRename
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    @DoNotRename
    protected String getUrl() {
        return url;
    }

    @DoNotRename
    protected void setUrl(String url) {
        this.url = url;
    }

    @DoNotRename
    public String getStatusMessage() {
        return statusMessage;
    }

    @DoNotRename
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @DoNotRename
    public boolean isSuccess() {
        return isSuccess;
    }

    @DoNotRename
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @DoNotRename
    protected String getInterviewUrl() {
        return interviewUrl;
    }

    @DoNotRename
    protected void setInterviewUrl(String interviewUrl) {
        this.interviewUrl = interviewUrl;
    }

    @DoNotRename
    private String getMessage() {
        return message;
    }

    @DoNotRename
    private void setMessage(String message) {
        this.message = message;
    }

    @Override
    @DoNotRename
    public int describeContents() {
        return 0;
    }

    @DoNotRename
    public long getHttpStatusCode() {
        return httpStatusCode;
    }

    @DoNotRename
    public void setHttpStatusCode(long httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    @DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uniqueID);
        parcel.writeString(url);
        parcel.writeString(interviewUrl);
        parcel.writeString(message);
        parcel.writeString(statusMessage);
        parcel.writeByte((byte) (isSuccess ? 1 : 0));
        parcel.writeLong(httpStatusCode);
    }
}
