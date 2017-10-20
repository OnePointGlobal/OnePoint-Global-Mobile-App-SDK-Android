package OnePoint.CordovaPlugin;

import android.content.Context;
import android.util.Log;

import com.allatori.annotations.DoNotRename;
import com.google.zxing.FakeR;
import com.opg.sdk.BuildConfig;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.PluginResult;

import static com.opg.sdk.OPGSDKConstant.DEVICE_STATE_KEY;
import static com.opg.sdk.OPGSDKConstant.STRING;

/**
 * Created by Dinesh-opg on 9/22/2017.
 */
@DoNotRename

public class GetDeviceOrientation extends RootPlugin {
    private CallbackContext callback;
    Context context;
    FakeR fakeR;
    @Override@DoNotRename
    public boolean execute(String action, final CordovaArgs args,
                           final CallbackContext callbackContext) {
        this.context = this.cordova.getActivity();
        init(context, callbackContext);
        this.callback = callbackContext;
        fakeR = new FakeR(context);

        try {
            String status = context.getResources().getString(fakeR.getId(STRING,DEVICE_STATE_KEY));
            PluginResult pluginResult = new PluginResult(
                    PluginResult.Status.OK, status);
            pluginResult.setKeepCallback(true);
            callback.sendPluginResult(pluginResult);
        } catch (Exception e) {
            if(BuildConfig.DEBUG){
                e.printStackTrace();
                Log.e(getClass().getName(),e.getMessage());
            }
            callback.error(e.getLocalizedMessage());
            return false;
        }
        return true;
    }
}
