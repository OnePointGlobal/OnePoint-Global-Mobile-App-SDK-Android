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
 * This class is the model class for the OPGPanellistProfile.
 * It has all the property of Panelist Profile.
 */
@DoNotRename
public class OPGPanellistProfile implements Parcelable {

    // Creator
    @DoNotRename
    public static final Parcelable.Creator CREATOR
            = new Creator() {
        public OPGPanellistProfile createFromParcel(Parcel in) {
            return new OPGPanellistProfile(in);
        }

        public OPGPanellistProfile[] newArray(int size) {
            return new OPGPanellistProfile[size];
        }
    };
    private String statusMessage;
    private boolean isSuccess;
    @SerializedName("ErrorMessage")
    private String errorMessage;
    @SerializedName("PanellistID")
    private long panellistID;
    @SerializedName("Title")
    private String title;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("Address1")
    private String address1;
    @SerializedName("Address2")
    private String address2;
    @SerializedName("PostalCode")
    private String postalCode;
    @SerializedName("Email")
    private String emailID;
    @SerializedName("MobileNumber")
    private String mobileNumber;
    @SerializedName("DOB")
    private Date dob;
    @SerializedName("MediaID")
    private String mediaID;
    @SerializedName("UserName")
    private String userName;

    private String countryName;
    private String std;
    // private int maritalStatus;
    // private String website;
    // private String countryCode;
    // private String mediaID;
    @SerializedName("Gender")
    private int gender;

    // "De-parcel object
    private OPGPanellistProfile(Parcel in) {
        statusMessage = in.readString();
        isSuccess = in.readByte() != 0;     //isSuccess == true if byte != 0
        errorMessage = in.readString();
        panellistID = in.readLong();
        title = in.readString();
        userName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        postalCode = in.readString();
        emailID = in.readString();
        mobileNumber = in.readString();
        dob = (Date) in.readSerializable();
        gender = in.readInt();
        mediaID = in.readString();
        countryName = in.readString();
        std = in.readString();
    }

    public OPGPanellistProfile() {
    }

    /**
     *
     * @return String
     */
    @DoNotRename
    public String getStd() {
        return std;
    }

    /**
     * Get the std code
     * @param std
     */
    @DoNotRename
    public void setStd(String std) {
        this.std = std;
    }

    /**
     *
     * @return
     */
    @DoNotRename
    public String getCountryName() {
        return countryName;
    }

    /**
     *
     * @param countryName
     */
    @DoNotRename
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @DoNotRename
    public String getUserName() {
        return userName;
    }

    @DoNotRename
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @DoNotRename
    public String getMediaID() {
        return mediaID;
    }

    @DoNotRename
    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
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
     * @return the firstName
     */
    @DoNotRename
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    @DoNotRename
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    @DoNotRename
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    @DoNotRename
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the address1
     */
    @DoNotRename
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    @DoNotRename
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    /**
     * @return the email
     */

    /**
     * @return the address2
     */
    @DoNotRename
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    @DoNotRename
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the postalCode
     */
    @DoNotRename
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    @DoNotRename
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the mobileNumber
     */
    @DoNotRename

    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * @param mobileNumber the mobileNumber to set
     */
    @DoNotRename
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @return the title
     */
    @DoNotRename
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    @DoNotRename
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the emailID
     */
    @DoNotRename
    public String getEmailID() {
        return emailID;
    }

    /**
     * @param emailID the emailID to set
     */
    @DoNotRename
    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    /**
     * @return the dob
     */
    @DoNotRename
    public Date getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    @DoNotRename
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * @return the gender
     */
    @DoNotRename
    public int getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    @DoNotRename
    public void setGender(int gender) {
        this.gender = gender;
    }


    @Override
    @DoNotRename
    public int describeContents() {
        return 0;
    }

@DoNotRename
    public long getPanellistID() {
      return panellistID;
}
@DoNotRename
    public void setPanellistID(long panellistID) {
    this.panellistID = panellistID;
}


    @Override
    @DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(statusMessage);
        parcel.writeByte((byte) (isSuccess ? 1 : 0));     //if isSuccess == true, byte == 1
        parcel.writeString(errorMessage);
        parcel.writeLong(panellistID);
        parcel.writeString(title);
        parcel.writeString(userName);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(address1);
        parcel.writeString(address2);
        parcel.writeString(postalCode);
        parcel.writeString(emailID);
        parcel.writeString(mobileNumber);
        parcel.writeSerializable(dob);
        parcel.writeInt(gender);
        parcel.writeString(mediaID);
        parcel.writeString(countryName);
        parcel.writeString(std);
    }
}
