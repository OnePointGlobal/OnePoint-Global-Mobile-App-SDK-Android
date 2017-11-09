package com.onepointglobal.mysurveysn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.opg.sdk.OPGSDK;
import com.opg.sdk.models.OPGPanellistPanel;

/**
 * Created by kiran on 08-09-2016.
 */
public class Util
{
   private static OPGSDK opgsdk;
    public static long panelID;




private static OPGPanellistPanel opgPanellistPanel;

    /**
     * Gets opg panellist panel.
     *
     * @return the opg panellist panel
     */
    public static OPGPanellistPanel getOpgPanellistPanel() {
    return opgPanellistPanel;
}

    /**
     * Sets opg panellist panel.
     *
     * @param opgPanellistPanel the opg panellist panel
     */
    public static void setOpgPanellistPanel(OPGPanellistPanel opgPanellistPanel) {
    Util.opgPanellistPanel = opgPanellistPanel;
}

    /**
     * Get opgsdk instance opgsdk.
     *
     * @return the opgsdk
     */
    public static OPGSDK getOPGSDKInstance(){
    if(opgsdk == null){
        opgsdk = new OPGSDK();
    }
    return opgsdk;
}

    /**
     * Is online boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static  boolean isOnline(Context context)
{
    ConnectivityManager cm =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
}

    /**
     * Show alert.
     *
     * @param context the context
     */
    public static void showAlert(final Context context) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

    // Setting Dialog Title
    alertDialog.setTitle("No Network...");

    // Setting Dialog Message
    alertDialog.setMessage("Please check the network connection...");

    // Setting Icon to Dialog
    alertDialog.setIcon(R.drawable.info);

    // Setting Positive "Yes" Button
    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
            ((Activity)context).finish();
        }
    });

    alertDialog.show();
}
}
