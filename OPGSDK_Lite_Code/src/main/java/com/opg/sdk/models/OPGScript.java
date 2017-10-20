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
 * This class is the model class for the OPGScript.
 */
@DoNotRename
public class OPGScript implements Parcelable{
    @DoNotRename
    public static final Creator<OPGScript> CREATOR = new Creator<OPGScript>() {
        @Override
        public OPGScript createFromParcel(Parcel in) {
            return new OPGScript(in);
        }

        @Override
        public OPGScript[] newArray(int size) {
            return new OPGScript[size];
        }
    };
    private String scriptFilePath;
    private boolean isSuccess;
    private String statusMessage;
    private String surveyRef;

    private OPGScript(Parcel in) {
        scriptFilePath = in.readString();
        isSuccess = in.readByte() != 0;
        statusMessage = in.readString();
        surveyRef = in.readString();
    }
    public  OPGScript()
    {

    }

    @DoNotRename
    public String getSurveyRef() {
        return surveyRef;
    }

    @DoNotRename
    public void setSurveyRef(String surveyRef) {
        this.surveyRef = surveyRef;
    }

    @DoNotRename
    public String getScriptFilePath() {
        return scriptFilePath;
    }

    /**
     * @param scriptFilePath the scriptFilePath to set
     */
    @DoNotRename
    public void setScriptFilePath(String scriptFilePath) {
        this.scriptFilePath = scriptFilePath;
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

    @Override@DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override@DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(scriptFilePath);
        parcel.writeByte((byte) (isSuccess ? 1 : 0));
        parcel.writeString(statusMessage);
        parcel.writeString(surveyRef);
    }
}
