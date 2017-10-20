/**
 *
 */
package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;
import com.google.gson.annotations.SerializedName;

/**
 * @author Neeraj
 */

/**
 * This class is the model class for the OPGChangePassword.
 */
@DoNotRename
public class OPGChangePassword implements Parcelable {
    @DoNotRename
    public static final Creator<OPGChangePassword> CREATOR = new Creator<OPGChangePassword>() {
        @Override
        public OPGChangePassword createFromParcel(Parcel in) {
            return new OPGChangePassword(in);
        }

        @Override
        public OPGChangePassword[] newArray(int size) {
            return new OPGChangePassword[size];
        }
    };
    // private String message;
    private String statusMessage;
    private boolean isSuccess;



@SerializedName("HttpStatusCode")
    private long httpStatusCode;

    private OPGChangePassword(Parcel in) {
        statusMessage = in.readString();
        isSuccess = in.readByte() != 0;
        httpStatusCode = in.readLong();
    }

    public OPGChangePassword(){

    }

    /**
     * @return the statusMessage
     */
    @DoNotRename
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    @DoNotRename
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * @return the isSuccess
     */
    @DoNotRename
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * @param isSuccess the isSuccess to set
     */
    @DoNotRename
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override@DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override@DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(statusMessage);
        parcel.writeByte((byte) (isSuccess ? 1 : 0));
        parcel.writeLong(httpStatusCode);
    }

@DoNotRename
public long getHttpStatusCode() {
    return httpStatusCode;
}

@DoNotRename
public void setHttpStatusCode(long httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
}
}
