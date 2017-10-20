package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Padmavathi on 26/11/2016.
 */

@DoNotRename
public class OPGGeofenceSurvey implements Parcelable{


    public static final Creator<OPGGeofenceSurvey> CREATOR = new Creator<OPGGeofenceSurvey>() {
        @Override
        public OPGGeofenceSurvey createFromParcel(Parcel in) {
            return new OPGGeofenceSurvey(in);
        }

        @Override
        public OPGGeofenceSurvey[] newArray(int size) {
            return new OPGGeofenceSurvey[size];
        }
    };
@SerializedName("SurveyName")
private String surveyName;
    @SerializedName("SurveyID")
    private long surveyID;
    @SerializedName("SurveyReference")
    private String surveyReference;// this survey reference is passed to OPGViewController to load Survey
    @SerializedName("Address")
    private String address;
    @SerializedName("AddressID")
    private long addressID;
    @SerializedName("Latitude")
    private double latitude;
    @SerializedName("Longitude")
    private double longitude;
    @SerializedName("Geocode")
    private String geocode;
    @SerializedName("CreatedDate")
    private Date createdDate;
    @SerializedName("LastUpdatedDate")
    private Date lastUpdatedDate;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("Distance")
    private float distance;
    @SerializedName("Range")
    private long range;

    public OPGGeofenceSurvey() {
    }

    protected OPGGeofenceSurvey(Parcel in) {
        surveyName = in.readString();
        surveyID = in.readLong();
        surveyReference = in.readString();
        address = in.readString();
        addressID = in.readLong();
        latitude = in.readFloat();
        longitude = in.readFloat();
        geocode = in.readString();
        isDeleted = in.readByte() != 0;
        distance = in.readFloat();
        range = in.readLong();
        createdDate = (Date) in.readSerializable();
        lastUpdatedDate = (Date) in.readSerializable();
    }

@DoNotRename
public String getSurveyName() {
    return surveyName;
}

@DoNotRename
public void setSurveyName(String surveyName) {
    this.surveyName = surveyName;
}

    @DoNotRename
    public long getSurveyID() {
        return surveyID;
    }

    @DoNotRename
    public void setSurveyID(long surveyID) {
        this.surveyID = surveyID;
    }

    @DoNotRename
    public String getSurveyReference() {
        return surveyReference;
    }

    @DoNotRename
    public void setSurveyReference(String surveyReference) {
        this.surveyReference = surveyReference;
    }

    @DoNotRename
    public String getAddress() {
        return address;
    }

    @DoNotRename
    public void setAddress(String address) {
        this.address = address;
    }

    @DoNotRename
    public long getAddressID() {
        return addressID;
    }

    @DoNotRename
    public void setAddressID(long addressID) {
        this.addressID = addressID;
    }

    @DoNotRename
    public double getLatitude() {
        return latitude;
    }

    @DoNotRename
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @DoNotRename
    public double getLongitude() {
        return longitude;
    }

    @DoNotRename
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @DoNotRename
    public String getGeocode() {
        return geocode;
    }

    @DoNotRename
    public void setGeocode(String geocode) {
        this.geocode = geocode;
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
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    @DoNotRename
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
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
    public float getDistance() {
        return distance;
    }

    @DoNotRename
    public void setDistance(float distance) {
        this.distance = distance;
    }

    @DoNotRename
    public long getRange() {
        return range;
    }

    @DoNotRename
    public void setRange(long range) {
        this.range = range;
    }

    @Override@DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override@DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(surveyName);
        parcel.writeLong(surveyID);
        parcel.writeString(surveyReference);
        parcel.writeString(address);
        parcel.writeLong(addressID);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(geocode);
        parcel.writeByte((byte) (isDeleted ? 1 : 0));
        parcel.writeFloat(distance);
        parcel.writeLong(range);
        parcel.writeSerializable(createdDate);
        parcel.writeSerializable(lastUpdatedDate);
    }
}
