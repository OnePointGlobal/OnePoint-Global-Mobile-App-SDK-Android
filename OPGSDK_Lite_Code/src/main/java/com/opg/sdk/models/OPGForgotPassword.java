package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;
import com.google.gson.annotations.SerializedName;

/**
 * This class is the model class for the OPGForgotPassword.
 */
@DoNotRename
public class OPGForgotPassword implements Parcelable {
    @DoNotRename
    public static final Creator<OPGForgotPassword> CREATOR = new Creator<OPGForgotPassword>() {
        @Override
        public OPGForgotPassword createFromParcel(Parcel in) {
            return new OPGForgotPassword(in);
        }

        @Override
        public OPGForgotPassword[] newArray(int size) {
            return new OPGForgotPassword[size];
        }
    };
    private String statusMessage;
    private boolean isSuccess;
@SerializedName("HttpStatusCode")
private long httpStatusCode;

    public OPGForgotPassword(String statusMessage, boolean isSuccess) {
        super();
        this.statusMessage = statusMessage;
        this.isSuccess = isSuccess;
    }


    public OPGForgotPassword() {
        super();
    }

    private OPGForgotPassword(Parcel in) {
        statusMessage = in.readString();
        isSuccess = in.readByte() != 0;
        httpStatusCode = in.readLong();
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
public long getHttpStatusCode() {
    return httpStatusCode;
}

@DoNotRename
public void setHttpStatusCode(long httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
}

    @Override
    @DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override
    @DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(statusMessage);
        parcel.writeByte((byte) (isSuccess ? 1 : 0));
        parcel.writeLong(httpStatusCode);
    }
}
