package OnePoint.Common;

import android.content.Context;
import android.content.pm.ApplicationInfo;


import com.allatori.annotations.DoNotRename;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.opg.sdk.OPGSDKConstant.AMP_KEY;
import static com.opg.sdk.OPGSDKConstant.ASTERIC_KEY;
import static com.opg.sdk.OPGSDKConstant.BACKSLASH_KEY;
import static com.opg.sdk.OPGSDKConstant.BLANK_SPACE;
import static com.opg.sdk.OPGSDKConstant.CAP_KEY;
import static com.opg.sdk.OPGSDKConstant.CARRIAGE_RETURN;
import static com.opg.sdk.OPGSDKConstant.CLOSE_BRACE1_KEY;
import static com.opg.sdk.OPGSDKConstant.CLOSE_BRACE2_KEY;
import static com.opg.sdk.OPGSDKConstant.CLOSE_BRACE3_KEY;
import static com.opg.sdk.OPGSDKConstant.COLON;
import static com.opg.sdk.OPGSDKConstant.COMA;
import static com.opg.sdk.OPGSDKConstant.DATE_FORMAT;
import static com.opg.sdk.OPGSDKConstant.DATE_FORMAT_T;
import static com.opg.sdk.OPGSDKConstant.DOT_KEY_KEY;
import static com.opg.sdk.OPGSDKConstant.EMPTY_STRING;
import static com.opg.sdk.OPGSDKConstant.FORWARD_SLASH;
import static com.opg.sdk.OPGSDKConstant.HYPHEN;
import static com.opg.sdk.OPGSDKConstant.NEW_LINE;
import static com.opg.sdk.OPGSDKConstant.OPEN_BRACE1_KEY;
import static com.opg.sdk.OPGSDKConstant.OPEN_BRACE2_KEY;
import static com.opg.sdk.OPGSDKConstant.OPEN_BRACE3_KEY;
import static com.opg.sdk.OPGSDKConstant.PIPE_KEY;
import static com.opg.sdk.OPGSDKConstant.PLUS_KEY;
import static com.opg.sdk.OPGSDKConstant.QUE_KEY;
import static com.opg.sdk.OPGSDKConstant.SEMI_COLON;
import static com.opg.sdk.OPGSDKConstant.SINGLE_QUOTE;
import static com.opg.sdk.OPGSDKConstant.T;
import static com.opg.sdk.OPGSDKConstant.UTC;

@DoNotRename
public class Utils {

    @SuppressWarnings("SimpleDateFormat" )
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.UK);
    @SuppressWarnings("SimpleDateFormat" )
    private static DateFormat iso = new SimpleDateFormat(DATE_FORMAT_T);
    @DoNotRename
    public static String getCurrentDate() {
        return dateFormat.format(new Date());
    }
    @DoNotRename
    public static String convertToUTCFromDate(Date date) {
        return dateFormat.format(date);
    }

    @DoNotRename
    public static String convertToISOFromDate(Date date) {
        TimeZone utc = TimeZone.getTimeZone(UTC);
        iso.setTimeZone(utc);
        //  date.setDate(date.getDate());
        Date today = new Date();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_T, Locale.ENGLISH);
//		     DATE_FORMAT_T.setTimeZone();
        String date2 = DATE_FORMAT.format(date);
        //String dat = iso.format(date);
        return date2;
    }
    @DoNotRename
    public static Date convertToDateFromUTC(String dateString) {
        dateString = dateString.replace(T, BLANK_SPACE);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*public static void writePanellistIdtoDevice(Context context, long panelListId) {
        try {

            File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getApplicationName(context)+ File.separator + "OPGData");
            if(directory.exists()) {
                directory.mkdir();
            }
            File filesurveyIdtoDevice = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + com.opg.main.Utils.PATH + File.separator + "OPGData" + File.separator + "opgPanelListIdData.txt");
            filesurveyIdtoDevice.createNewFile();
            FileOutputStream fOut = new FileOutputStream(filesurveyIdtoDevice);
            OutputStreamWriter Osw = new OutputStreamWriter(fOut);
            Osw.append("" + panelListId);
            Osw.close();
            fOut.close();
        } catch (Exception e) {
            Log.i("APP", e.getLocalizedMessage());
        }
    }*/
/*public static  String readPanelListCachePathFromDevice() {
    try {

        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +com.opg.main.Utils.PATH+ File.separator + "OPGData" + File.separator + "opgPanelListIdData.txt");
        FileInputStream fIn = new FileInputStream(myFile);
        BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
        String aDataRow = "";
        String aBuffer = "";
        while ((aDataRow = myReader.readLine()) != null) {
            aBuffer += aDataRow;
        }
        String data = (aBuffer);
        myReader.close();

        return data;

    } catch (Exception e) {
        return null;
    }
}*/
    @DoNotRename
    public static String getApplicationName(Context context)
    {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        String appName = (stringId == 0) ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
        appName = appName.replaceAll(NEW_LINE,EMPTY_STRING);
        appName = appName.replaceAll(CARRIAGE_RETURN,EMPTY_STRING);
        appName = appName.replaceAll(OPEN_BRACE1_KEY,EMPTY_STRING);
        appName = appName.replaceAll(CLOSE_BRACE1_KEY,EMPTY_STRING);
        appName = appName.replaceAll(OPEN_BRACE2_KEY,EMPTY_STRING);
        appName = appName.replaceAll(CLOSE_BRACE2_KEY,EMPTY_STRING);
        appName = appName.replaceAll(OPEN_BRACE3_KEY,EMPTY_STRING);
        appName = appName.replaceAll(CLOSE_BRACE3_KEY,EMPTY_STRING);
        appName = appName.replaceAll(BLANK_SPACE,EMPTY_STRING);
        appName = appName.replaceAll(FORWARD_SLASH,EMPTY_STRING);
        appName = appName.replaceAll(BACKSLASH_KEY,EMPTY_STRING);
        appName = appName.replaceAll(COMA.trim(),EMPTY_STRING);
        appName = appName.replaceAll(COLON,EMPTY_STRING);
        appName = appName.replaceAll(HYPHEN,EMPTY_STRING);
        appName = appName.replaceAll(SEMI_COLON,EMPTY_STRING);
        appName = appName.replaceAll(SINGLE_QUOTE,EMPTY_STRING);
        appName = appName.replaceAll(AMP_KEY,EMPTY_STRING);
        appName = appName.replaceAll(CAP_KEY,EMPTY_STRING);
        appName = appName.replaceAll(QUE_KEY,EMPTY_STRING);
        appName = appName.replaceAll(PIPE_KEY,EMPTY_STRING);
        appName = appName.replaceAll(PLUS_KEY,EMPTY_STRING);
        appName = appName.replaceAll(ASTERIC_KEY,EMPTY_STRING);
        appName = appName.replaceAll(DOT_KEY_KEY,EMPTY_STRING);
        return appName;
    }

}
