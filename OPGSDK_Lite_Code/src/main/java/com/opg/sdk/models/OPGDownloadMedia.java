package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;

/**
 * This class is the model class for the OPGDownloadMedia.
 */
@DoNotRename
public class OPGDownloadMedia implements Parcelable {
    @DoNotRename
    public static final Creator<OPGDownloadMedia> CREATOR = new Creator<OPGDownloadMedia>() {
        @Override
        public OPGDownloadMedia createFromParcel(Parcel in) {
            return new OPGDownloadMedia(in);
        }

        @Override
        public OPGDownloadMedia[] newArray(int size) {
            return new OPGDownloadMedia[size];
        }
    };
    private String mediaPath;
    private String statusMessage;
    private boolean isSuccess;

    public OPGDownloadMedia(String mediaPath, String statusMessage, boolean isSuccess) {
        this.mediaPath = mediaPath;
        this.statusMessage = statusMessage;
        this.isSuccess = isSuccess;
    }

    public OPGDownloadMedia() {
    }

    private OPGDownloadMedia(Parcel in) {
        mediaPath = in.readString();
        statusMessage = in.readString();
        isSuccess = in.readByte() != 0;
    }

    @DoNotRename
    public String getMediaPath() {
        return mediaPath;
    }

    @DoNotRename
    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
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
    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override@DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override@DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mediaPath);
        parcel.writeString(statusMessage);
        parcel.writeByte((byte) (isSuccess ? 1 : 0));
    }
}
