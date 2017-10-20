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
 * This class is the model class for the OPGPanel.
 */
@DoNotRename
public class OPGPanel implements Parcelable {
    @DoNotRename
    public static final Creator<OPGPanel> CREATOR = new Creator<OPGPanel>() {
        @Override
        public OPGPanel createFromParcel(Parcel in) {
            return new OPGPanel(in);
        }

        @Override
        public OPGPanel[] newArray(int size) {
            return new OPGPanel[size];
        }
    };
    @SerializedName("PanelType")
    private byte panelType; // 1 indicates Normal and 2 indicates CAPI survey
    @SerializedName("PanelID")
    private long panelID;
    @SerializedName("ThemeTemplateID")
    private long themeTemplateID;
    @SerializedName("ThemeTemplateIDSpecified")
    private boolean themeTemplateIDSpecified;
    @SerializedName("Name")
    private String name;
    @SerializedName("Description")
    private String description;
    @SerializedName("SearchTag")
    private String searchTag;
    @SerializedName("Remark")
    private String remark;
    @SerializedName("CreatedDate")
    private Date createdDate;
    @SerializedName("LastUpdatedDate")
    private Date lastUpdatedDate;
    // failure.
    @SerializedName("IsDeleted")
    private boolean isDeleted; // bool value of 1 indicates success and 0 indicates
    @SerializedName("UserID")
    private long userID;
    private String statusMessage;

    @SerializedName("MediaID")
    private long mediaID;
    @SerializedName("MediaIDSpecified")
    private boolean isMediaIDSpecified;
    @SerializedName("MediaUrl")
    private String mediaUrl;

    @SerializedName("LogoID")
    private long logoID;
    @SerializedName("LogoIDSpecified")
    private boolean isLogoIDSpecified;
    @SerializedName("LogoUrl")
    private String logoUrl;

    public OPGPanel() {

    }

    private OPGPanel(Parcel in) {
        panelID = in.readLong();
        themeTemplateID = in.readLong();
        themeTemplateIDSpecified = in.readByte() != 0;
        name = in.readString();
        description = in.readString();
        panelType = in.readByte();
        searchTag = in.readString();
        remark = in.readString();
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        createdDate = (Date) in.readSerializable();
        lastUpdatedDate = (Date) in.readSerializable();
        isDeleted = in.readByte() != 0;
        userID = in.readLong();
        statusMessage = in.readString();
        mediaID = in.readLong();
        isMediaIDSpecified = in.readByte() != 0 ;
        mediaUrl = in.readString();
        logoID = in.readLong();
        isLogoIDSpecified = in.readByte() != 0 ;
        logoUrl = in.readString();
    }

    @DoNotRename
    public boolean isThemeTemplateIDSpecified() {
        return themeTemplateIDSpecified;
    }

    @DoNotRename
    public void setThemeTemplateIDSpecified(boolean themeTemplateIDSpecified) {
        this.themeTemplateIDSpecified = themeTemplateIDSpecified;
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
     * @return the themeTemplateID
     */
    @DoNotRename
    public long getThemeTemplateID() {
        return themeTemplateID;
    }

    @DoNotRename
    public void setThemeTemplateID(long themeTemplateID) {
        this.themeTemplateID = themeTemplateID;
    }

    /**
     * @return the name
     */
    @DoNotRename
    public String getName() {
        return name;
    }

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

    @DoNotRename
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the isDeleted
     */

    /**
     * @return the panelType
     */
    @DoNotRename
    public byte getPanelType() {
        return panelType;
    }

    @DoNotRename
    public void setPanelType(byte panelType) {
        this.panelType = panelType;
    }

    /**
     * @return the searchTag
     */
    @DoNotRename
    public String getSearchTag() {
        return searchTag;
    }

    @DoNotRename
    public void setSearchTag(String searchTag) {
        this.searchTag = searchTag;
    }

    /**
     * @return the remark
     */
    @DoNotRename
    public String getRemark() {
        return remark;
    }

    @DoNotRename
    public void setRemark(String remark) {
        this.remark = remark;
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
     * @return the lastUpdatedDate
     */
    @DoNotRename
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    @DoNotRename
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    /**
     * @return the userID
     */
    @DoNotRename
    public long getUserID() {
        return userID;
    }

    @DoNotRename
    public void setUserID(long userID) {
        this.userID = userID;
    }

    @Override
    @DoNotRename
    public int describeContents() {
        return 0;
    }

    @DoNotRename
    public boolean isDeleted() {
        return isDeleted;
    }

    @DoNotRename
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

@DoNotRename
public long getMediaID() {
    return mediaID;
}

@DoNotRename
public void setMediaID(long mediaID) {
    this.mediaID = mediaID;
}

@DoNotRename
public boolean isMediaIDSpecified() {
    return isMediaIDSpecified;
}

@DoNotRename
public void setMediaIDSpecified(boolean mediaIDSpecified) {
    isMediaIDSpecified = mediaIDSpecified;
}

@DoNotRename
public String getMediaUrl() {
    return mediaUrl;
}

@DoNotRename
public void setMediaUrl(String mediaUrl) {
    this.mediaUrl = mediaUrl;
}

@DoNotRename
public long getLogoID() {
    return logoID;
}

@DoNotRename
public void setLogoID(long logoID) {
    this.logoID = logoID;
}

@DoNotRename
public boolean isLogoIDSpecified() {
    return isLogoIDSpecified;
}

@DoNotRename
public void setLogoIDSpecified(boolean logoIDSpecified) {
    isLogoIDSpecified = logoIDSpecified;
}

@DoNotRename
public String getLogoUrl() {
    return logoUrl;
}

@DoNotRename
public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
}

    @Override
    @DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(panelID);
        parcel.writeLong(themeTemplateID);
        parcel.writeByte((byte) (themeTemplateIDSpecified ? 1 : 0));
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeByte(panelType);
        parcel.writeString(searchTag);
        parcel.writeString(remark);
        parcel.writeSerializable(createdDate);
        parcel.writeSerializable(lastUpdatedDate);
        parcel.writeByte((byte) (isDeleted ? 1 : 0));
        parcel.writeLong(userID);
        parcel.writeString(statusMessage);
        parcel.writeLong(mediaID);
        parcel.writeByte((byte) (isMediaIDSpecified ? 1 : 0));
        parcel.writeString(mediaUrl);
        parcel.writeLong(logoID);
        parcel.writeByte((byte) (isLogoIDSpecified ? 1 : 0));
        parcel.writeString(logoUrl);

    }
}
