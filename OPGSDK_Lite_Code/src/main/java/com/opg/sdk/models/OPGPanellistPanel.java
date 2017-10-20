package com.opg.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.allatori.annotations.DoNotRename;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Neeraj on 17-10-2016.
 */

/**
 * This class contains array of OPGPanelPanellist,OPGPanel,OPGTheme and OPGSurveyPanel.
 */

@DoNotRename
public class OPGPanellistPanel implements Parcelable {
    @DoNotRename
    public static final Creator<OPGPanellistPanel> CREATOR = new Creator<OPGPanellistPanel>() {
        @Override
        public OPGPanellistPanel createFromParcel(Parcel in) {
            return new OPGPanellistPanel(in);
        }

        @Override
        public OPGPanellistPanel[] newArray(int size) {
            return new OPGPanellistPanel[size];
        }
    };
    private String statusMessage;
    private boolean isSuccess;
    private List<OPGPanelPanellist> panelPanellistArray = new ArrayList<>();
    private List<OPGPanel> panelArray = new ArrayList<>();
    private List<OPGTheme> themeArray = new ArrayList<>();
    private List<OPGSurveyPanel> surveyPanelArray = new ArrayList<>();

    private OPGPanellistPanel(Parcel in) {
        panelPanellistArray = Arrays.asList(in.createTypedArray(OPGPanelPanellist.CREATOR));
        panelArray = Arrays.asList(in.createTypedArray(OPGPanel.CREATOR));
        themeArray = Arrays.asList(in.createTypedArray(OPGTheme.CREATOR));
        surveyPanelArray = Arrays.asList(in.createTypedArray(OPGSurveyPanel.CREATOR));
    }

    public OPGPanellistPanel() {

    }

    @DoNotRename
    public String getStatusMessage() {
        return statusMessage;
    }

    @DoNotRename
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @DoNotRename
    public boolean isSuccess() {
        return isSuccess;
    }

    @DoNotRename
    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @DoNotRename
    public List<OPGPanelPanellist> getPanelPanellistArray() {
        return panelPanellistArray;
    }

    @DoNotRename
    public void setPanelPanellistArray(List<OPGPanelPanellist> panelPanellistArray) {
        this.panelPanellistArray = panelPanellistArray;
    }

    @DoNotRename
    public List<OPGPanel> getPanelArray() {
        return panelArray;
    }

    @DoNotRename
    public void setPanelArray(List<OPGPanel> panelArray) {
        this.panelArray = panelArray;
    }

    @DoNotRename
    public List<OPGTheme> getThemeArray() {
        return themeArray;
    }

    @DoNotRename
    public void setThemeArray(List<OPGTheme> themeArray) {
        this.themeArray = themeArray;
    }

    @DoNotRename
    public List<OPGSurveyPanel> getSurveyPanelArray() {
        return surveyPanelArray;
    }

    @DoNotRename
    public void setSurveyPanelArray(List<OPGSurveyPanel> surveyPanelArray) {
        this.surveyPanelArray = surveyPanelArray;
    }

    @Override@DoNotRename
    public int describeContents() {
        return 0;
    }

    @Override
    @DoNotRename
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeArray(panelPanellistArray.toArray());
        parcel.writeArray(panelArray.toArray());
        parcel.writeArray(themeArray.toArray());
        parcel.writeArray(surveyPanelArray.toArray());
    }
}
