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
 * This class is the model class for the OPGPanelPanellist.
 */
@DoNotRename
public class OPGPanelPanellist implements Parcelable {
    @DoNotRename
    public static final Creator<OPGPanelPanellist> CREATOR = new Creator<OPGPanelPanellist>() {
        @Override
        public OPGPanelPanellist createFromParcel(Parcel in) {
            return new OPGPanelPanellist(in);
        }

        @Override
        public OPGPanelPanellist[] newArray(int size) {
            return new OPGPanelPanellist[size];
        }
    };
    @SerializedName("PanelPanellistID")
    private long panelPanellistID;
    @SerializedName("PanelID")
    private long panelID;
    @SerializedName("PanellistID")
    private long panellistID;
    @SerializedName("CreatedDate")
    private Date createdDate;
    @SerializedName("LastUpdatedDate")
    private Date lastUpdatedDate;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("Included")
    private boolean included;
    @SerializedName("IncludedSpecified")
    private boolean includedSpecified;

    public OPGPanelPanellist() {

    }

    @DoNotRename
    public void setPanelPanellistID(long panelPanellistID) {
        this.panelPanellistID = panelPanellistID;
    }

    @DoNotRename
    public void setPanelID(long panelID) {
        this.panelID = panelID;
    }

    @DoNotRename
    public void setPanellistID(long panellistID) {
        this.panellistID = panellistID;
    }

    @DoNotRename
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @DoNotRename
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @DoNotRename
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @DoNotRename
    public void setIncluded(boolean included) {
        this.included = included;
    }

    @DoNotRename
    public void setIncludedSpecified(boolean includedSpecified) {
        this.includedSpecified = includedSpecified;
    }

    private OPGPanelPanellist(Parcel in) {
        panelPanellistID = in.readLong();
        panelID = in.readLong();
        panellistID = in.readLong();
        included = in.readByte() != 0;
        includedSpecified = in.readByte() != 0;
        createdDate = (Date) in.readSerializable();
        isDeleted = in.readByte() != 0;
        lastUpdatedDate = (Date) in.readSerializable();
    }

    @DoNotRename
    public boolean isIncluded() {
        return included;
    }

    @DoNotRename
    public boolean isIncludedSpecified() {
        return includedSpecified;
    }

    /**
     * @return the panelPanellistID
     */
    @DoNotRename
    public long getPanelPanellistID() {
        return panelPanellistID;
    }

    /**
     * @return the panelID
     */
    @DoNotRename
    public long getPanelID() {
        return panelID;
    }

    /**
     * @return the panellistID
     */
    @DoNotRename
    public long getPanellistID() {
        return panellistID;
    }

    /**
     * @return the createdDate
     */
    @DoNotRename
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @return the lastUpdatedDate
     */
    @DoNotRename
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    /**
     * @return the isDeleted
     */
    @DoNotRename
    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override@DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(panelPanellistID);
        parcel.writeLong(panelID);
        parcel.writeLong(panellistID);
        parcel.writeByte((byte) (included ? 1 : 0));
        parcel.writeByte((byte) (includedSpecified ? 1 : 0));
        parcel.writeSerializable(createdDate.toString());
        parcel.writeByte((byte) (isDeleted ? 1 : 0));
        parcel.writeSerializable(lastUpdatedDate);
    }
}
