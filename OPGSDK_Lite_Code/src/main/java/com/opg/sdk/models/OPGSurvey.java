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
 * This class is the model class for the Survey.It contains all the variables a
 * survey contains.
 *
 * @author Neeraj
 */
@DoNotRename
public class OPGSurvey implements Parcelable {
    // Creator
    @DoNotRename
    public static final Parcelable.Creator CREATOR
            = new Creator() {
        public OPGSurvey createFromParcel(Parcel in) {
            return new OPGSurvey(in);
        }

        public OPGSurvey[] newArray(int size) {
            return new OPGSurvey[size];
        }
    };
    @SerializedName("IsGeoFencing")
    private boolean isGeofencing;
    @SerializedName("Name")
    private String name;
    @SerializedName("Description")
    private String description;
    @SerializedName("ScriptID")
    private long scriptID;
    @SerializedName("SurveyReference")
    private String surveyReference;// this survey reference is passed to OPGViewController to load Survey
    @SerializedName("SurveyID")
    private long surveyID;
    @SerializedName("LastUpdatedDate")
    private Date lastUpdatedDate;
    @SerializedName("CreatedDate")
    private Date createdDate;
    @SerializedName("IsOffline")
    private boolean isOffline;
    @SerializedName("ErrorMessage")
    private String errorMessage;
    @SerializedName("EstimatedTime")
    private long estimatedTime;
    @SerializedName("DeadLine")
    private Date deadLine;
    @SerializedName("StartDate")
    private Date startDate;
    @SerializedName("EndDate")
    private Date endDate;

    private String status;//this variable is used internally in the mysurveys app.

    // "De-parcel object
    private OPGSurvey(Parcel in) {
        isGeofencing = in.readByte() != 0;     //isGeofencing == true if byte != 0
        name = in.readString();
        description = in.readString();
        scriptID = in.readLong();
        surveyReference = in.readString();
        surveyID = in.readLong();
        createdDate = (Date) in.readSerializable();
        lastUpdatedDate = (Date) in.readSerializable();
        isOffline = in.readByte() != 0;     //isOffline == true if byte != 0
        estimatedTime = in.readLong();
        deadLine = (Date) in.readSerializable();
        errorMessage = in.readString();
        status = in.readString();
        startDate = (Date) in.readSerializable();
        endDate = (Date) in.readSerializable();
    }

    public OPGSurvey() {

    }

    @DoNotRename
    public String getStatus() {
        return status;
    }

    @DoNotRename
    public void setStatus(String status) {
        this.status = status;
    }

    @DoNotRename
    public Date getCreatedDate() {
        return createdDate;
    }

    @DoNotRename
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @DoNotRename
    public long getEstimatedTime() {
        return estimatedTime;
    }

    @DoNotRename
    public void setEstimatedTime(long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    @DoNotRename
    public Date getDeadLine() {
        return deadLine;
    }

    @DoNotRename
    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    /**
     * @return the errorMessage
     */
    @DoNotRename
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    @DoNotRename
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the isGeofencing
     */
    @DoNotRename
    public boolean isGeofencing() {
        return isGeofencing;
    }

    /**
     * @param isGeofencing the isGeofencing to set
     */
    @DoNotRename
    public void setGeofencing(boolean isGeofencing) {
        this.isGeofencing = isGeofencing;
    }

    /**
     * @return the name
     */
    @DoNotRename
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    @DoNotRename
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    @DoNotRename
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    @DoNotRename
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the scriptID
     */
    @DoNotRename
    public long getScriptID() {
        return scriptID;
    }

    /**
     * @param scriptID the scriptID to set
     */
    @DoNotRename
    public void setScriptID(long scriptID) {
        this.scriptID = scriptID;
    }

    /**
     * @return the surveyReference
     */
    @DoNotRename
    public String getSurveyReference() {
        return surveyReference;
    }

   /* public void setLastUpdatedDate(Date lastUpdatedDate)
    {
        this.lastUpdatedDate = lastUpdatedDate;
    }*/

    /**
     * @param surveyReference the survey reference
     */
    @DoNotRename
    public void setSurveyReference(String surveyReference) {
        this.surveyReference = surveyReference;
    }

    @DoNotRename
    public long getSurveyID() {
        return surveyID;
    }

    @DoNotRename
    public void setSurveyID(long surveyID) {
        this.surveyID = surveyID;
    }

    /**
     * @return the lastUpdatedDate
     */
    @DoNotRename
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    /**
     * @param lastUpdatedDate the lastUpdatedDate to set
     */
    @DoNotRename
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    /**
     * @return the isOffline
     */
    @DoNotRename
    public boolean isOffline() {
        return isOffline;
    }

    /**
     * @param isOffline the isOffline to set
     */
    @DoNotRename
    public void setOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    @Override
    @DoNotRename
    public int describeContents() {
        return 0;
    }

    @DoNotRename
    public Date getStartDate() {
        return startDate;
    }

    @DoNotRename
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @DoNotRename
    public Date getEndDate() {
        return endDate;
    }

    @DoNotRename
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    @DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isGeofencing ? 1 : 0));     //if isGeofencing == true, byte == 1
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeLong(scriptID);
        parcel.writeString(surveyReference);
        parcel.writeLong(surveyID);
        parcel.writeSerializable(createdDate);
        parcel.writeSerializable(lastUpdatedDate);
        parcel.writeByte((byte) (isOffline ? 1 : 0));     //if isOffline == true, byte == 1
        parcel.writeLong(estimatedTime);
        parcel.writeSerializable(deadLine);
        parcel.writeString(errorMessage);
        parcel.writeString(status);
        parcel.writeSerializable(startDate);
        parcel.writeSerializable(endDate);
    }
}
