package com.onepointglobal.mysurveysn;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {
    private ListView listView;
   // private String ADMIN_USERNAME  = "**";////Replace the adminusername with your adminusername
    //private String ADMIN_SHAREDKEY = "**";//Replace the adminsharedkey with your adminsharedkey
   // private String APPVERSION  = "*****";//Replace the appversion with your appversion

    private Context   mContext;
    private List<String> listPermissionsNeeded;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };
    private String[] apiArray = new String[]{"Authenticate","Authenticate - SocialMedia","1.SurveyList","2.Script","3.Download Media",
            "4.Upload Media","5.PanellistPanel", "6.GetThemes", "7.GetPanels","8.GetSurveyPanels", "9.ForgotPassword", "10.ChangePassword",
            "11.PanellistProfile","12.UpdatePanellistProfile","13.GeoFencing","14.PushNotification","15.GetCountries","16.logout"};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mContext    = this;
        listPermissionsNeeded = new ArrayList<>();
        checkPermission(true);

        listView = (ListView) findViewById(R.id.api_list);
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, apiArray));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position)
                {
                    case 0 :   intent = new Intent(MainActivity.this,AuthenticateActivity.class);
                        startActivity(intent);
                        break;
                    case 1 :   intent = new Intent(MainActivity.this,AuthenticateWithSocialMedia.class);
                        startActivity(intent);
                        break;
                    case 2 : intent = new Intent(MainActivity.this,SurveyListActivity.class);
                        startActivity(intent);
                        break;
                    case 3 :
                        Toast.makeText(MainActivity.this,"Not Yet Integrated.",Toast.LENGTH_SHORT).show();
                        break;
                    case 4 : intent = new Intent(MainActivity.this,DownloadMediaActivity.class);
                        startActivity(intent);
                        break;
                    case 5 :intent = new Intent(MainActivity.this,UploadMediaActivity.class);
                        startActivity(intent);
                        break;
                    case 6 : intent = new Intent(MainActivity.this,GetPanellistPanel.class);
                        intent.putExtra("API_INDEX",position);
                        startActivity(intent);
                        break;
                    case 7 : intent = new Intent(MainActivity.this,GetThemeActivity.class);
                        startActivity(intent);
                        break;
                    case 8 : intent = new Intent(MainActivity.this,GetPanelActivity.class);
                        startActivity(intent);
                        break;
                    case 9 : intent = new Intent(MainActivity.this,GetSurveyPanelActivity.class);
                        startActivity(intent);
                        break;
                    case 10 : intent = new Intent(MainActivity.this,ForgotPasswordActivity.class);
                        startActivity(intent);
                        break;

                    case 11 :  intent = new Intent(MainActivity.this,ChangePasswordActivity.class);
                        startActivity(intent);
                        break;
                    case 12 :  intent = new Intent(MainActivity.this,GetPanellistProfileActivity.class);
                        startActivity(intent);
                        break;
                    case 13 :  intent = new Intent(MainActivity.this,UpdatePanellistProfileActivity.class);
                        startActivity(intent);
                        break;
                    case 14 : intent = new Intent(MainActivity.this,GetGeofenceSurvey.class);
                        startActivity(intent);
                        break;
                    case 15 :  intent = new Intent(MainActivity.this,NotificationActivity.class);
                        startActivity(intent);
                        break;
                    case 16 :   intent = new Intent(MainActivity.this,GetCountries.class);
                        startActivity(intent);
                           break;
                    case 17: intent = new Intent(MainActivity.this,LogoutActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        Toast.makeText(MainActivity.this,"API Not Integrated.",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    /*This method intializes the opgsdk.....  */
    /*private void initializeOPGSDK() {
        try
        {
            OPGSDK opgsdk = new OPGSDK();
            //Initialising the OPGSDK with adminUsername and adminSharedKey
            //OPGSDK.initialize(ADMIN_USERNAME , ADMIN_SHAREDKEY, getApplicationContext());

            //set the app version
           // opgsdk.setAppVersion(APPVERSION, MainActivity.this);
        }catch (Exception ex )
        {
            ex.printStackTrace();
        }
    }*/


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                for( int i = 0; i < permissions.length; i++ ) {
                    if( grantResults[i] == PackageManager.PERMISSION_GRANTED ) {
                        if(listPermissionsNeeded.contains(grantResults[i])){
                            listPermissionsNeeded.remove(grantResults);
                        }
                        Log.d( "Permissions", "Permission Granted: " + permissions[i] );
                    } else if( grantResults[i] == PackageManager.PERMISSION_DENIED ) {
                        Log.d( "Permissions", "Permission Denied: " + permissions[i] );
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    /**
     * Check permission boolean.
     *
     * @param requestPermission the request permission
     * @return the boolean
     */
    public boolean checkPermission(boolean requestPermission){
        if (Build.VERSION.SDK_INT >= 23) {
            int result;
            for (String p:permissions) {
                result = ContextCompat.checkSelfPermission(mContext,p);
                Log.d("Result",""+result);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty() ) {
                if(requestPermission)
                    ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return false;
            }
            return true;
        } else {
            return  true;
        }
    }
}


