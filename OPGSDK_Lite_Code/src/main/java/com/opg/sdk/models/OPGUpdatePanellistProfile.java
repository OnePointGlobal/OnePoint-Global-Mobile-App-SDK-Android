/**
 *
 */
package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;

/**
 * @author Neeraj
 */

/**
 * This class is the model class for the OPGUpdatePanellistProfile.
 */
@DoNotRename
public class OPGUpdatePanellistProfile implements Parcelable {
    @DoNotRename
    public static final Creator<OPGUpdatePanellistProfile> CREATOR = new Creator<OPGUpdatePanellistProfile>() {
        @Override
        public OPGUpdatePanellistProfile createFromParcel(Parcel in) {
            return new OPGUpdatePanellistProfile(in);
        }

        @Override
        public OPGUpdatePanellistProfile[] newArray(int size) {
            return new OPGUpdatePanellistProfile[size];
        }
    };
    private boolean isSuccess;
    private String statusMessage;

    private OPGUpdatePanellistProfile(Parcel in) {
        isSuccess = in.readByte() != 0;
        statusMessage = in.readString();
    }

    public OPGUpdatePanellistProfile() {

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

    @Override
    @DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override
    @DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isSuccess ? 1 : 0));
        parcel.writeString(statusMessage);
    }
}
