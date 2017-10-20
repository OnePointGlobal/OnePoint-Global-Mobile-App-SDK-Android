package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by kiran on 09-11-2016.
 */

/**
 * This class is the model class for the OPGCountry.
 */
@DoNotRename
public class OPGCountry implements Parcelable
{

    @DoNotRename
    public static final Creator<OPGCountry> CREATOR = new Creator<OPGCountry>() {
        @Override
        public OPGCountry createFromParcel(Parcel in) {
            return new OPGCountry(in);
        }

        @Override
        public OPGCountry[] newArray(int size) {
            return new OPGCountry[size];
        }
    };

    @SerializedName("CountryID")
    private int countryID;

    @SerializedName("Name")
    private String countryName;

    @SerializedName("Std")
    private String std;

    @SerializedName("CountryCode")
    private String countryCode;

    @SerializedName("Gmt")
    private Date gmt;

    @SerializedName("CreditRate")
    private double creditRate;

    @SerializedName("isDeleted")
    private boolean isDeleted;


    public OPGCountry(Parcel in)
    {
        countryID = in.readInt();
        countryName = in.readString();
        std = in.readString();
        countryCode = in.readString();
        gmt = (Date) in.readSerializable();;
        creditRate = in.readDouble();
        isDeleted = in.readByte() != 0;
    }

    public OPGCountry()
    {

    }

    @Override@DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override@DoNotRename
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(countryID);
        parcel.writeString(countryName);
        parcel.writeString(std);
        parcel.writeString(countryCode);
        parcel.writeSerializable(gmt);
        parcel.writeDouble(creditRate);
        parcel.writeByte((byte) (isDeleted ? 1 : 0));
    }
    @DoNotRename
    public int getCountryID() {
        return countryID;
    }

    @DoNotRename
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    @DoNotRename
    public String getCountryName() {
        return countryName;
    }

    @DoNotRename
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @DoNotRename
    public String getStd() {
        return std;
    }

    @DoNotRename
    public void setStd(String std) {
        this.std = std;
    }

    @DoNotRename
    public String getCountryCode() {
        return countryCode;
    }

    @DoNotRename
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @DoNotRename
    public Date getGmt() {
        return gmt;
    }

    @DoNotRename
    public void setGmt(Date gmt) {
        this.gmt = gmt;
    }

    @DoNotRename
    public double getCreditRate() {
        return creditRate;
    }

    @DoNotRename
    public void setCreditRate(double creditRate) {
        this.creditRate = creditRate;
    }

    @DoNotRename
    public boolean isDeleted() {
        return isDeleted;
    }

    @DoNotRename
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


}
