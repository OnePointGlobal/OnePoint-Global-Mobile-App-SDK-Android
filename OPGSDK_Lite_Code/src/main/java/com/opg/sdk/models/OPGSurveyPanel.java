/**
 *
 */
package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Neeraj
 */

/**
 * This class is the model class for the OPGSurveyPanel.
 */
@DoNotRename
public class OPGSurveyPanel implements Parcelable {
    @DoNotRename
    public static final Creator<OPGSurveyPanel> CREATOR = new Creator<OPGSurveyPanel>() {
        @Override
        public OPGSurveyPanel createFromParcel(Parcel in) {
            return new OPGSurveyPanel(in);
        }

        @Override
        public OPGSurveyPanel[] newArray(int size) {
            return new OPGSurveyPanel[size];
        }
    };
    @SerializedName("SurveyPanelID")
    private long surveyPanelID;
    @SerializedName("SurveyID")
    private long surveyID;
    @SerializedName("PanelID")
    private long panelID;
    @SerializedName("CreatedDate")
    private Date createdDate;
    @SerializedName("LastUpdatedDate")
    private Date lastUpdatedDate;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("Excluded")
    private boolean excluded;
    @SerializedName("ExcludedSpecified")
    private boolean excludedSpecified;
    private String statusMessage;

    public OPGSurveyPanel() {

    }

    private OPGSurveyPanel(Parcel in) {
        surveyPanelID = in.readLong();
        surveyID = in.readLong();
        panelID = in.readLong();
        excluded = in.readByte() != 0;
        excludedSpecified = in.readByte() != 0;
        isDeleted = in.readByte() != 0;
        statusMessage = in.readString();
        createdDate = (Date) in.readSerializable();
        lastUpdatedDate = (Date) in.readSerializable();
    }

    @DoNotRename
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    @DoNotRename
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @DoNotRename
    public boolean isExcluded() {
        return excluded;
    }

    @DoNotRename
    public void setExcluded(boolean excluded) {
        this.excluded = excluded;
    }

    @DoNotRename
    public boolean isExcludedSpecified() {
        return excludedSpecified;
    }

    @DoNotRename
    public void setExcludedSpecified(boolean excludedSpecified) {
        this.excludedSpecified = excludedSpecified;
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
     * @return the surveyPanelID
     */
    @DoNotRename
    public long getSurveyPanelID() {
        return surveyPanelID;
    }

    @DoNotRename
    public void setSurveyPanelID(long surveyPanelID) {
        this.surveyPanelID = surveyPanelID;
    }

    /**
     * @return the surveyID
     */
    @DoNotRename
    public long getSurveyID() {
        return surveyID;
    }

    @DoNotRename
    public void setSurveyID(long surveyID) {
        this.surveyID = surveyID;
    }

    /**
     * @return the panelID
     */
    @DoNotRename
    public long getPanelID() {
        return panelID;
    }

    @DoNotRename
    public void setPanelID(long panelID) {
        this.panelID = panelID;
    }

    /**
     * @return the createdDate
     */
    @DoNotRename
    public Date getCreatedDate() {
        return createdDate;
    }

    @DoNotRename
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the isDeleted
     */
    @DoNotRename
    public boolean isDeleted() {
        return isDeleted;
    }

    @DoNotRename
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    @DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override
    @DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(surveyPanelID);
        parcel.writeLong(surveyID);
        parcel.writeLong(panelID);
        parcel.writeByte((byte) (excluded ? 1 : 0));
        parcel.writeByte((byte) (excludedSpecified ? 1 : 0));
        parcel.writeByte((byte) (isDeleted ? 1 : 0));
        parcel.writeString(statusMessage);
        parcel.writeSerializable(createdDate);
        parcel.writeSerializable(lastUpdatedDate);
    }
}
