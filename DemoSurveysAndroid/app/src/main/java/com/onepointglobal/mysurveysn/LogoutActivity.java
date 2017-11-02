package com.onepointglobal.mysurveysn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


/**
 * The type Logout activity.
 */
public class LogoutActivity extends AppCompatActivity {

    /**
     * The Context.
     */
    Context context ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        context = this;
    }

    /**
     * On logout click.
     *
     * @param view the view
     */
    public void onLogoutClick(View view)
    {
        // for logout
        Util.getOPGSDKInstance().logout(context);
        Toast.makeText(this,"Successfully Logout.",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
