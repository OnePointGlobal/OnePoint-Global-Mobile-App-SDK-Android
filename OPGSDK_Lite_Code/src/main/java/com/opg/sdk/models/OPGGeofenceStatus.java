package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;

/**
 * Created by Padmavathi on 21/12/2016.
 */
@DoNotRename
public class OPGGeofenceStatus implements Parcelable{

    public static final Creator<OPGGeofenceStatus> CREATOR = new Creator<OPGGeofenceStatus>() {
        @Override
        public OPGGeofenceStatus createFromParcel(Parcel in) {
            return new OPGGeofenceStatus(in);
        }

        @Override
        public OPGGeofenceStatus[] newArray(int size) {
            return new OPGGeofenceStatus[size];
        }
    };
    boolean isSuccess,isMonitoring;
    String  message;

    public OPGGeofenceStatus() {
    }

    protected OPGGeofenceStatus(Parcel in) {
        isSuccess = in.readByte() != 0;
        isSuccess = in.readByte() != 0;
        message = in.readString();
    }

    @DoNotRename
    public boolean isSuccess() {
        return isSuccess;
    }

    @DoNotRename
    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @DoNotRename
    public boolean isMonitoring() {
        return isMonitoring;
    }

    @DoNotRename
    public void setMonitoring(boolean monitoring) {
        isMonitoring = monitoring;
    }

    @DoNotRename
    public String getMessage() {
        return message;
    }

    @DoNotRename
    public void setMessage(String message) {
        this.message = message;
    }



    @Override@DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override@DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isSuccess ? 1 : 0));
        parcel.writeByte((byte) (isMonitoring ? 1 : 0));
        parcel.writeString(message);
    }
}
