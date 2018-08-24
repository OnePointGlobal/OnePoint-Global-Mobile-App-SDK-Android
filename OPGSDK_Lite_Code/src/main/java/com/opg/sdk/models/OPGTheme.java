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
 * This class is the model class for the OPGTheme.
 * @author Neeraj
 *
 */
@DoNotRename
public class OPGTheme implements Parcelable
{
    @DoNotRename
    public static final Creator<OPGTheme> CREATOR = new Creator<OPGTheme>() {
        @Override
        public OPGTheme createFromParcel(Parcel in) {
            return new OPGTheme(in);
        }

        @Override
        public OPGTheme[] newArray(int size) {
            return new OPGTheme[size];
        }
    };
    @SerializedName("ThemeID")
    private long themeID;
    @SerializedName("ThemeTemplateID")
    private long themeTemplateID;
    @SerializedName("ThemeElementTypeID")
    private long themeElementTypeID;
    @SerializedName("Name")
    private String name;
    @SerializedName("Value")
    private String value;
    @SerializedName("CreatedDate")//Hexadecimal color code
    private Date createdDate;
    @SerializedName("LastUpdatedDate")
    private Date lastUpdatedDate;
    @SerializedName("IsDeleted")
    private boolean  isDeleted;
    private String statusMessage;

    private OPGTheme(Parcel in) {
        themeID = in.readLong();
        themeTemplateID = in.readLong();
        themeElementTypeID = in.readLong();
        name = in.readString();
        value = in.readString();
        createdDate = (Date)in.readSerializable();
        lastUpdatedDate = (Date)in.readSerializable();
        isDeleted = in.readByte() != 0;
        statusMessage = in.readString();
    }

    public OPGTheme(){

    }

    /**
     * @return the statusMessage
     */
    @DoNotRename
    public String getStatusMessage()
    {
        return statusMessage;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    @DoNotRename
    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    /**
     * @return the themeID
     */
    @DoNotRename
    public long getThemeID()
    {
        return themeID;
    }

    @DoNotRename
    public void setThemeID(long themeID) {
        this.themeID = themeID;
    }

    /**
     * @return the themeTemplateID
     */
    @DoNotRename
    public long getThemeTemplateID()
    {
        return themeTemplateID;
    }

    @DoNotRename
    public void setThemeTemplateID(long themeTemplateID) {
        this.themeTemplateID = themeTemplateID;
    }

    /**
     * @return the themeElementTypeID
     */
    @DoNotRename
    public long getThemeElementTypeID()
    {
        return themeElementTypeID;
    }

    @DoNotRename
    public void setThemeElementTypeID(long themeElementTypeID) {
        this.themeElementTypeID = themeElementTypeID;
    }

    /**
     * @return the isSuccess
     */
   /* private boolean isSuccess()
    {
        return isSuccess;
    }*/

    /**
     * @return the name
     */
    @DoNotRename
    public String getName()
    {
        return name;
    }

    /**
     * @param //isSuccess the isSuccess to set
     */
    /*private void setSuccess(boolean isSuccess)
    {
        this.isSuccess = isSuccess;
    }*/

    //  private boolean isSuccess = true;

    @DoNotRename
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    @DoNotRename
    public String getValue()
    {
        return value;
    }

    @DoNotRename
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the createdDate
     */
    @DoNotRename
    public Date getCreatedDate()
    {
        return createdDate;
    }

    @DoNotRename
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the lastUpdatedDate
     */
    @DoNotRename
    public Date getLastUpdatedDate()
    {
        return lastUpdatedDate;
    }

    @DoNotRename
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    /**
     * @return the isDeleted
     */
    @DoNotRename
    public boolean isDeleted()
    {
        return isDeleted;
    }

    @DoNotRename
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override@DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override
    @DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(themeID);
        parcel.writeLong(themeTemplateID);
        parcel.writeLong(themeElementTypeID);
        parcel.writeString(name);
        parcel.writeString(value);
        parcel.writeSerializable(createdDate);
        parcel.writeSerializable(lastUpdatedDate);
        parcel.writeByte((byte) (isDeleted ? 1 : 0));
        parcel.writeString(statusMessage);
    }
}
