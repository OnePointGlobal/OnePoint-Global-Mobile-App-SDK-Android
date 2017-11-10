package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;

/**
 * Created by kiran on 06-01-2017.
 */

@DoNotRename
public class OPGUploadResult implements Parcelable
{
private  boolean isSuccess;
private String statusMessage;

private OPGUploadResult(Parcel in) {
    isSuccess = in.readByte() != 0;
    statusMessage = in.readString();
}
public  OPGUploadResult()
{

}

@DoNotRename
public static final Creator<OPGUploadResult> CREATOR = new Creator<OPGUploadResult>() {
    @Override
    public OPGUploadResult createFromParcel(Parcel in) {
        return new OPGUploadResult(in);
    }

    @Override
    public OPGUploadResult[] newArray(int size) {
        return new OPGUploadResult[size];
    }
};
@DoNotRename
public String getMessage() {
    return statusMessage;
}

@DoNotRename
public void setMessage(String message) {
    this.statusMessage = message;
}

@DoNotRename
public boolean isSuccess() {
    return isSuccess;
}

@DoNotRename
public void setSuccess(boolean success) {
    isSuccess = success;
}

@Override
public int describeContents() {
    return 0;
}

@Override
public void writeToParcel(Parcel parcel, int i) {
    parcel.writeByte((byte) (isSuccess ? 1 : 0));
    parcel.writeString(statusMessage);
}
}
