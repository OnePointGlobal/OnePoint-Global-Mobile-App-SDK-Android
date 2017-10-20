package OnePoint.CordovaPlugin;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Window;

import com.allatori.annotations.DoNotRename;
import com.opg.sdk.OPGR;
import com.opg.sdk.OPGSDKConstant;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import OnePoint.Common.Utils;
import OnePoint.CordovaPlugin.Utils.FileUtils;

import static com.opg.sdk.OPGSDKConstant.LAYOUT;


//import OnePoint.Logging.LogManager;
@DoNotRename
public class MediaPickerAndPreviewPlugin extends RootPlugin {
    final private static int REQUEST_CODE_STORAGE_PERMISSION = 129;
    final private static int REQUEST_CODE_CAMERA_PERMISSIONS = 126;
    final private static int REQUEST_CODE_AUDIO_PERMISSIONS = 127;
    final private static int REQUEST_CODE_RECORD_PERMISSIONS = 128;
    public static String mediaFilename;
    private static boolean isGallery = false;
    private static int MAX_IMAGE_SIZE = 900 * 1024;//size in bytes
    // FOR IMAGE CAPTUER
    private final int PICK_IMAGE_FROM_CAMERA = 1;
    private final int PICK_IMAGE_FROM_GALLARY = 2;
    // FOR AUDIO CAPTURE
    private final int PICK_AUDIO_FROM_GALLARY = 4;
    // FOR AUDIO CAPTURE
    private final int PICK_VIDEO_FROM_CAMERA = 5;
    private final int PICK_VIDEO_FROM_GALLARY = 6;
    private final int CROP_IMAGE_FROM_CAMERA = 7;
    public String _CurrentMediaPath = "";
    Context context;
    PluginResult pluginResult;
    private CallbackContext callback;
    private ImageLoadingUtils utils;
    private String _CapturedAuioPath = null;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private Dialog progressDialog;

    @Override@DoNotRename
    public boolean execute(String action, final CordovaArgs args, final CallbackContext callbackContext) {

        this.context = this.cordova.getActivity();
        //OPGDBHelper.mContext = this.context;
        this.callback = callbackContext;
        utils = new ImageLoadingUtils(context);
        try {
            System.out.println(action);
            if (action.equalsIgnoreCase(ACTION_MEDIA_PICK_MAGE_FROM_CAMERA)) {
                pickImageFromCamera();
            } else if (action.equalsIgnoreCase(ACTION_MEDIA_PICK_IMAGE_FROM_GALLERY)) {
                pickImageFromGallery();
            } else if (action.equalsIgnoreCase(ACTION_MEDIA_PICK_AUDIO_FROM_GALLERY)) {
                pickAudioFromGallery();
            } else if (action.equalsIgnoreCase(ACTION_MEDIA_START_RECORDING_AUDIO)) {
                startRecordingAudio();
            } else if (action.equalsIgnoreCase(ACTION_MEDIA_STOP_RECORDING_AUDIO)) {
                stopRecordingAudio();
            } else if (action.equalsIgnoreCase(ACTION_MEDIA_START_PLAYING_RECORDED_AUDIO)) {
                JSONObject obj = args.getJSONObject(0);
                if (obj.opt(OPGSDKConstant.MEDIA_PATH_KEY) != null)
                    startPlayingAudio(obj.getString(OPGSDKConstant.MEDIA_PATH_KEY));
            } else if (action.equalsIgnoreCase(ACTION_MEDIA_STOP_PALYING_RECORDED_AUDIO)) {
                stopPlayingRecordedAudio();
            } else if (action.equalsIgnoreCase(ACTION_MEDIA_PICK_VIDEO_FROM_CAMERA)) {
                pickVideoFromCamera();
            } else if (action.equalsIgnoreCase(ACTION_MEDIA_PICK_VIDEO_FROM_GALLERY)) {
                pickVideoFromGallery();
            } else if (action.equalsIgnoreCase(ACTION_MEDIA_PLAY_VIDEO_FROM_SELECTEDPATH)) {
                JSONObject obj = args.getJSONObject(0);
                if (obj.opt(OPGSDKConstant.MEDIA_PATH_KEY) != null)
                    playVideoFromPath(obj.getString(OPGSDKConstant.MEDIA_PATH_KEY));
            } else if (action.equalsIgnoreCase(ACTION_MEDIA_SHOW_IMAGE_FROM_PATH)) {
                JSONObject obj = args.getJSONObject(0);
                if (obj.opt(OPGSDKConstant.MEDIA_PATH_KEY) != null)
                    showImageFromPath(obj.getString(OPGSDKConstant.MEDIA_PATH_KEY));
            } else {
                callback.error(getReplyJsonString(102));
            }
        } catch (Exception e) {
            e.printStackTrace();
            //LogManager.getLogger(getClass()).error(e.getMessage());
            callback.error(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    /** PICK IMAGE FROM CAMERA **/
    public void pickImageFromCamera() {
        if(checkPermission(REQUEST_CODE_CAMERA_PERMISSIONS)) {
            File vitaccessMediaDir = new File(Environment.getExternalStorageDirectory() +OPGSDKConstant.FILE_SEPARATOR + Utils.getApplicationName(context) +OPGSDKConstant.FILE_SEPARATOR + OPGSDKConstant.MEDIA + OPGSDKConstant.FORWARD_SLASH);
            File vitaccessMediaImageDir = new File(vitaccessMediaDir + OPGSDKConstant.OPG_IMAGE);
            if (!vitaccessMediaImageDir.exists()) {
                vitaccessMediaImageDir.mkdirs();
            }

            String format = String.format(Locale.ENGLISH, OPGSDKConstant.IMAGE_REGEX, 3);
            File saveFile;
            do {
                String filename = OPGSDKConstant.IMAGE_CAPTURE_ + String.format(Locale.ENGLISH, format, System.currentTimeMillis() / 1000L) + OPGSDKConstant.JPEG;
                saveFile = new File(vitaccessMediaImageDir, filename);
                mediaFilename = filename;
            } while (saveFile.exists());

            Intent intent = new Intent(OPGSDKConstant.IMAGE_CAPTURE);
            _CurrentMediaPath = saveFile.getAbsolutePath();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(this.context, this.context.getPackageName(), saveFile);
                intent.putExtra(OPGSDKConstant.EXTRA_OUTPUT, contentUri);
            }
            else
            {
                intent.putExtra(OPGSDKConstant.EXTRA_OUTPUT, Uri.fromFile(saveFile));
            }
            this.cordova.startActivityForResult(this, intent, PICK_IMAGE_FROM_CAMERA);
        }
    }

    /** PICK IMAGE FROM GALLERY **/
    public void pickImageFromGallery() {
        if(checkStoragePermission()) {
            Intent intent = new Intent(OPGSDKConstant.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(OPGSDKConstant.EXTRA_LOCAL_ONLY,true);
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            if(componentName != null)
            {
                this.cordova.startActivityForResult(this, intent, PICK_IMAGE_FROM_GALLARY);
            }
            else
            {
                Intent intent2 = new Intent(OPGSDKConstant.ACTION_GET_CONTENT);
                intent2.setType(OPGSDKConstant.MIME_TYPE_IMAGE);
                componentName = intent2.resolveActivity(context.getPackageManager());
                if(componentName != null)
                {
                    this.cordova.startActivityForResult(this, Intent.createChooser(intent2, OPGSDKConstant.SELECT_IMAGE), PICK_IMAGE_FROM_GALLARY);
                }
                else
                {
                    //show error
                }
            }
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(MIME_TYPE_IMAGE);
                this.cordova.startActivityForResult(this, Intent.createChooser(intent, SELECT_IMAGE), PICK_IMAGE_FROM_GALLARY);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MIME_TYPE_IMAGE);
                this.cordova.startActivityForResult(this, intent, PICK_IMAGE_FROM_GALLARY);
            }*/
        }
    }

    /** SHOW IMAGE FROM PATH **/
    public void showImageFromPath(String path) {
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            Uri uri = Uri.parse(OPGSDKConstant.FILE_COLON + path);
            Intent intent = new Intent(OPGSDKConstant.ACTION_VIEW);
            intent.setDataAndType(uri, OPGSDKConstant.MIME_TYPE_IMAGE);
            this.cordova.getActivity().startActivity(intent);
        } else {
            callback.error(OPGSDKConstant.FAILED_ERROR_PREVIEW);
        }
    }
    /** PICK AUDIO FROM GALLERY **/
    public void pickAudioFromGallery() {
        if(checkStoragePermission()) {
            Intent audioChooser = new Intent(OPGSDKConstant.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            ComponentName componentName = audioChooser.resolveActivity(context.getPackageManager());
            if(componentName != null)
            {
                this.cordova.startActivityForResult(this, audioChooser, PICK_AUDIO_FROM_GALLARY);
            }
            else
            {
                Intent intent = new Intent(OPGSDKConstant.ACTION_GET_CONTENT);
                intent.setType(OPGSDKConstant.MIME_TYPE_AUDIO);
                componentName = intent.resolveActivity(context.getPackageManager());
                if(componentName != null)
                {
                    this.cordova.startActivityForResult(this, Intent.createChooser(intent, OPGSDKConstant.SELECT_AUDIO), PICK_AUDIO_FROM_GALLARY);
                }
                else
                {
                    //show error
                }
            }
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(MIME_TYPE_AUDIO);
                this.cordova.startActivityForResult(this, Intent.createChooser(intent, SELECT_AUDIO), PICK_AUDIO_FROM_GALLARY);
            } else {
                Intent videoChooser = new Intent(Intent.ACTION_PICK,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                videoChooser.setType(MIME_TYPE_AUDIO);
                videoChooser.setAction(Intent.ACTION_GET_CONTENT);
                this.cordova.startActivityForResult(this, videoChooser, PICK_AUDIO_FROM_GALLARY);
            }*/
        }
    }

    private boolean showStoragePermissionDialog(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(), OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION)
                && ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(), OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION)){
            return  true;
        }
        return false;
    }

    private boolean showCameraPermissionDialog(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(), OPGSDKConstant.CAMERA_PERMISSION)){
            return  true;
        }
        return false;
    }

    private boolean showAudioPermissionDialog(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(), OPGSDKConstant.AUDIO_PERMISSION)){
            return  true;
        }
        return false;
    }

    private boolean isPermissionGranted(String permission){
        if(ActivityCompat.checkSelfPermission(this.context, permission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private boolean checkPermission(int requestCode) {
        if(Build.VERSION.SDK_INT >= 23) {
            if ((isPermissionGranted(OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION) && isPermissionGranted(OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION))
                    && ((requestCode == REQUEST_CODE_CAMERA_PERMISSIONS && isPermissionGranted(OPGSDKConstant.CAMERA_PERMISSION))
                    || (requestCode == REQUEST_CODE_AUDIO_PERMISSIONS && isPermissionGranted(OPGSDKConstant.AUDIO_PERMISSION))
                    || (requestCode == REQUEST_CODE_RECORD_PERMISSIONS && isPermissionGranted(OPGSDKConstant.CAMERA_PERMISSION) &&
                    isPermissionGranted(OPGSDKConstant.AUDIO_PERMISSION)))) {
                return true;
            } else {
                if (requestCode == REQUEST_CODE_CAMERA_PERMISSIONS &&
                        showCameraPermissionDialog() && showStoragePermissionDialog()) {
                    //Camera permission 1 camera dialog can display and storage also display
                    requestRuntimePermissions(
                            new String[]{OPGSDKConstant.CAMERA_PERMISSION, OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION,
                                    OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION },requestCode);

                }else if (requestCode == REQUEST_CODE_CAMERA_PERMISSIONS &&
                        showCameraPermissionDialog() && !showStoragePermissionDialog()) {
                    //Camera permission 2 camera dialog can display and storage shouldn't display
                    requestRuntimePermissions(new String[]{ OPGSDKConstant.CAMERA_PERMISSION },	requestCode);

                } else if (requestCode == REQUEST_CODE_AUDIO_PERMISSIONS &&
                        showAudioPermissionDialog()	&& showStoragePermissionDialog()) {
                    //Audio permission 1 audio dialog can display and storage also display
                    requestRuntimePermissions(
                            new String[]{OPGSDKConstant.AUDIO_PERMISSION,OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION,
                                    OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION}, requestCode);

                }else if (requestCode == REQUEST_CODE_AUDIO_PERMISSIONS &&
                        showAudioPermissionDialog()	&& !showStoragePermissionDialog()){
                    //Audio permission 2 audio dialog can display and storage shoudn't display
                    requestRuntimePermissions(new String[]{OPGSDKConstant.AUDIO_PERMISSION}, requestCode);

                } else if (requestCode == REQUEST_CODE_RECORD_PERMISSIONS &&
                        showCameraPermissionDialog() && showAudioPermissionDialog() && showStoragePermissionDialog()){
                    //video permission 1 show camera , audio nd storage dialog
                    requestRuntimePermissions(new String[]{OPGSDKConstant.CAMERA_PERMISSION, OPGSDKConstant.AUDIO_PERMISSION,
                            OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION, OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION}, requestCode);

                } else if (requestCode == REQUEST_CODE_RECORD_PERMISSIONS &&
                        showCameraPermissionDialog() && showAudioPermissionDialog() && !showStoragePermissionDialog()) {
                    //video permission 2 show camera , audio  display nd don't show storage dialog
                    requestRuntimePermissions(new String[]{OPGSDKConstant.CAMERA_PERMISSION,
                            OPGSDKConstant.AUDIO_PERMISSION}, requestCode);

                } else if (requestCode == REQUEST_CODE_RECORD_PERMISSIONS &&
                        showCameraPermissionDialog() && !showAudioPermissionDialog() && !showStoragePermissionDialog()) {
                    //video permission 3 show camera display nd    don't show audio storage dialog
                    requestRuntimePermissions(new String[]{OPGSDKConstant.CAMERA_PERMISSION},	requestCode);

                } else if (requestCode == REQUEST_CODE_RECORD_PERMISSIONS &&
                        !showCameraPermissionDialog() && showAudioPermissionDialog() && !showStoragePermissionDialog()) {
                    //video permission 4 show audio display nd    don't show camera storage dialog
                    ActivityCompat.requestPermissions(cordova.getActivity(),
                            new String[]{OPGSDKConstant.AUDIO_PERMISSION},	requestCode);

                } else if (requestCode == REQUEST_CODE_RECORD_PERMISSIONS &&
                        showCameraPermissionDialog() && !showAudioPermissionDialog() && showStoragePermissionDialog()) {
                    //video permission 5 don't show audio display nd  show camera storage dialog
                    requestRuntimePermissions(new String[]{OPGSDKConstant.CAMERA_PERMISSION, OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION,
                            OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION}, requestCode);

                } else if (requestCode == REQUEST_CODE_RECORD_PERMISSIONS &&
                        !showCameraPermissionDialog() && showAudioPermissionDialog() && showStoragePermissionDialog()) {
                    //video permission 6 don't show camera display nd  show audio storage dialog
                    requestRuntimePermissions(new String[]{OPGSDKConstant.AUDIO_PERMISSION, OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION,
                            OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION}, requestCode);

                } else if(showStoragePermissionDialog()){
                    //If there is no permission given
                    requestRuntimePermissions(new String[]{OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION,
                            OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION}, requestCode);

                } else {
                    showAlertDialog(requestCode);
                }
            }
            return false;
        }
        return  true;

    }
    @DoNotRename
    private boolean checkStoragePermission(){
        if(Build.VERSION.SDK_INT >= 23) {
            if (isPermissionGranted(OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION) && isPermissionGranted(OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION)) {
                return true;
            } else if (showStoragePermissionDialog()) {
                requestRuntimePermissions(new String[]{OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION,
                        OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION}, REQUEST_CODE_STORAGE_PERMISSION);
            } else {
                showAlertDialog(REQUEST_CODE_STORAGE_PERMISSION);
            }
            return false;
        }
        return true;
    }

    private void requestRuntimePermissions(String[] permissions,int requestCode){
        ActivityCompat.requestPermissions(cordova.getActivity(),
                permissions,
                requestCode);
    }

    private void showAlertDialog(int requestCode) {
        final android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.support.v7.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.support.v7.app.AlertDialog.Builder(context);
        }
        String message = "";
        if(requestCode== REQUEST_CODE_CAMERA_PERMISSIONS){
            if(!isPermissionGranted(OPGSDKConstant.CAMERA_PERMISSION))
                message = OPGR.getString(context,OPGSDKConstant.STRING,OPGSDKConstant.CAMERA_PERMISSION_MSG);
        }else if( requestCode == REQUEST_CODE_AUDIO_PERMISSIONS){
            if(!isPermissionGranted(OPGSDKConstant.AUDIO_PERMISSION))
                message = OPGR.getString(context,OPGSDKConstant.STRING,OPGSDKConstant.AUDIO_PERMISSION_MSG);
        }else if(requestCode == REQUEST_CODE_RECORD_PERMISSIONS){
            if(!isPermissionGranted(OPGSDKConstant.CAMERA_PERMISSION) || (!isPermissionGranted(OPGSDKConstant.AUDIO_PERMISSION)))
                message = OPGR.getString(context,OPGSDKConstant.STRING,OPGSDKConstant.RECORD_PERMISSION_MSG);
        }

        if(!isPermissionGranted(OPGSDKConstant.WRITE_EXTRENAL_STORAGE_PERMISSION) && !isPermissionGranted(OPGSDKConstant.READ_EXTRENAL_STORAGE_PERMISSION)){
            if(!message.isEmpty())
                message = message + OPGSDKConstant.NEW_LINE;
            message = message + OPGR.getString(context,OPGSDKConstant.STRING,OPGSDKConstant.ACCESS_PERMISSION_MSG);
        }
        builder.setTitle(OPGR.getString(context,OPGSDKConstant.STRING,OPGSDKConstant.RUNTIME_PERMISSION))
                .setMessage(message)
                .setPositiveButton(OPGR.getString(context,OPGSDKConstant.STRING,OPGSDKConstant.SETTINGS), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goToSettingPage();
                    }
                })
                .setNegativeButton(OPGR.getString(context,OPGSDKConstant.STRING,OPGSDKConstant.DENY), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void goToSettingPage(){
        Intent intent = new Intent();
        intent.setAction(OPGSDKConstant.APP_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(OPGSDKConstant.PACKAGE, cordova.getActivity().getPackageName(), null);
        intent.setData(uri);
        cordova.getActivity().startActivity(intent);
    }


    /** PICK AUDIO FROM RECORDER : 1 - START RECORDING **/
    public void startRecordingAudio() throws IOException {
        if(checkPermission(REQUEST_CODE_AUDIO_PERMISSIONS)) {
            File vitaccessMediaDir = new File(Environment.getExternalStorageDirectory() +OPGSDKConstant.FILE_SEPARATOR + Utils.getApplicationName(context) +OPGSDKConstant.FILE_SEPARATOR + OPGSDKConstant.MEDIA + OPGSDKConstant.FORWARD_SLASH);
            File vitaccessMediaAudioDir = new File(vitaccessMediaDir + OPGSDKConstant.OPG_AUDIO);
            if (!vitaccessMediaAudioDir.exists()) {
                vitaccessMediaAudioDir.mkdirs();
            }

            String format = String.format(Locale.ENGLISH, OPGSDKConstant.AUDIO_REGEX, 3);
            File saveFile;
            do {
                String filename = OPGSDKConstant.AUDIO_CAPTURE_ + String.format(Locale.ENGLISH, format, System.currentTimeMillis()) + OPGSDKConstant.WAV;
                saveFile = new File(vitaccessMediaAudioDir, filename);
            } while (saveFile.exists());

            this._CapturedAuioPath = saveFile.getAbsolutePath();
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFile(this._CapturedAuioPath);
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setAudioEncodingBitRate(96000);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.prepare();
            mRecorder.start();
        }
    }

    /** PICK AUDIO FROM RECORDER : 2 - STOP RECORDING **/
    public void stopRecordingAudio() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
        }
        mRecorder = null;
        sendResul(this._CapturedAuioPath);
    }

    /** PLAY AUDIO **/
    public void startPlayingAudio(String path) throws IOException {
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    sendResul(OPGSDKConstant.COMPLETED);
                }
            });
        } else {
            callback.error(OPGSDKConstant.FAILED_ERROR_PREVIEW);
        }
    }

    /** STOP PLAYING AUDIO **/
    public void stopPlayingRecordedAudio() {
        if (mPlayer != null)
            mPlayer.release();
        mPlayer = null;
    }

    /** PICK VIDEO FROM CAMERA **/
    public void pickVideoFromCamera() {
        if(checkPermission(REQUEST_CODE_RECORD_PERMISSIONS)) {
            Intent takeVideoIntent = new Intent(OPGSDKConstant.VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(this.cordova.getActivity().getPackageManager()) != null) {
                this.cordova.startActivityForResult(this, takeVideoIntent, PICK_VIDEO_FROM_CAMERA);
            }
        }
    }

    /** PICK VIDEO FROM GALLERY **/
    public void pickVideoFromGallery() {
        if(checkStoragePermission()) {
            Intent videoChooser = new Intent(OPGSDKConstant.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            videoChooser.putExtra(OPGSDKConstant.EXTRA_LOCAL_ONLY, true);
            ComponentName componentName = videoChooser.resolveActivity(context.getPackageManager());
            if(componentName != null )
            {
                videoChooser.setType(OPGSDKConstant.MIME_TYPE_VIDEO);
                this.cordova.startActivityForResult(this, videoChooser, PICK_VIDEO_FROM_GALLARY);
            }
            else
            {
                Intent videoChooser2 = new Intent(OPGSDKConstant.ACTION_GET_CONTENT);
                videoChooser2.setType(OPGSDKConstant.MIME_TYPE_VIDEO);
                componentName = videoChooser2.resolveActivity(context.getPackageManager());
                if(componentName != null)
                {
                    this.cordova.startActivityForResult(this, videoChooser2, PICK_VIDEO_FROM_GALLARY);
                }
                else
                {
                    //show error message
                }
            }

        }
    }

    /** PLAY VIDEO FROM PATH **/
    public void playVideoFromPath(String path) {

        File f = new File(path);
        if (f.exists() && !f.isDirectory())
        {
            // Display video player
            Intent intent = new Intent(OPGSDKConstant.ACTION_VIEW);
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            if(componentName != null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(this.context, this.context.getPackageName(), f);
                    intent.setDataAndType(contentUri, OPGSDKConstant.MIME_TYPE_VIDEO);
                }
                else
                {
                    Uri uri = Uri.parse(OPGSDKConstant.FILE_COLON+ path);
                    intent.setDataAndType(uri, OPGSDKConstant.MIME_TYPE_VIDEO);
                }
                this.cordova.getActivity().startActivity(intent);
            }
            else
            {
                callback.error(OPGSDKConstant.FAILED_ERROR_PREVIEW);
            }
        } else {
            callback.error(OPGSDKConstant.FAILED_ERROR_PREVIEW);
        }
    }

    @Override@DoNotRename
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case PICK_IMAGE_FROM_GALLARY:
                isGallery = true;
                if (resultCode == Activity.RESULT_OK) {
                    final Uri imageUri = intent.getData();
                    _CurrentMediaPath = imageUri.toString();
                    try {
                        _CurrentMediaPath = OnePoint.CordovaPlugin.Utils.FileUtils.getPath(context,
                                Uri.parse(_CurrentMediaPath));
                    } catch (Exception e) {
                        _CurrentMediaPath = _CurrentMediaPath
                                .replaceFirst(_CurrentMediaPath.substring(0, _CurrentMediaPath.indexOf(OPGSDKConstant.COLON)), "");
                        _CurrentMediaPath = _CurrentMediaPath.replaceAll(OPGSDKConstant.COLON, "");
                    }

                    //compressImage(_CurrentMediaPath);
                    new CompressImage(_CurrentMediaPath).execute();
                    /*sendResult();*/
                }
                break;
            case PICK_IMAGE_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    isGallery = false;
                    sendResult();

                }
                break;

            case PICK_AUDIO_FROM_GALLARY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri audioUri = intent.getData();
                    _CurrentMediaPath = audioUri.toString();
                    try {
                        _CurrentMediaPath = OnePoint.CordovaPlugin.Utils.FileUtils.getPath(context,
                                Uri.parse(_CurrentMediaPath));
                    } catch (Exception e) {
                        _CurrentMediaPath = _CurrentMediaPath
                                .replaceFirst(_CurrentMediaPath.substring(0, _CurrentMediaPath.indexOf(OPGSDKConstant.COLON)), "");
                        _CurrentMediaPath = _CurrentMediaPath.replaceAll(OPGSDKConstant.COLON, "");
                    }
                    _CapturedAuioPath = _CurrentMediaPath;
                    sendResult();
                }
                break;
            case PICK_VIDEO_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    Uri videoUri1 = intent.getData();
                    _CurrentMediaPath = videoUri1.toString();
                    try {
                        _CurrentMediaPath = OnePoint.CordovaPlugin.Utils.FileUtils.getPath(context,
                                Uri.parse(_CurrentMediaPath));
                    } catch (Exception e) {
                        _CurrentMediaPath = _CurrentMediaPath
                                .replaceFirst(_CurrentMediaPath.substring(0, _CurrentMediaPath.indexOf(OPGSDKConstant.COLON)), "");
                        _CurrentMediaPath = _CurrentMediaPath.replaceAll(OPGSDKConstant.COLON, "");
                    }
                    CopyVideo(_CurrentMediaPath);


                }
                break;
            case PICK_VIDEO_FROM_GALLARY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri videoUri2 = intent.getData();
                    _CurrentMediaPath = videoUri2.toString();
                    try {
                        _CurrentMediaPath = OnePoint.CordovaPlugin.Utils.FileUtils.getPath(context,
                                Uri.parse(_CurrentMediaPath));
                    } catch (Exception e) {
                        _CurrentMediaPath = _CurrentMediaPath
                                .replaceFirst(_CurrentMediaPath.substring(0, _CurrentMediaPath.indexOf(OPGSDKConstant.COLON)), "");
                        _CurrentMediaPath = _CurrentMediaPath.replaceAll(OPGSDKConstant.COLON, "");
                    }
                    // _CurrentMediaPath="/storage/emulated/0/DCIM/Camera/VID_20150302_103648.mp4";
                    //System.out.println("OnActivity Result path ----- : " + _CurrentMediaPath);
                    CopyVideo(_CurrentMediaPath);

                }
                break;
        }
    }

    private void CopyVideo(String _CurrentMediaPath2) {

        String filename = null;
        File vitaccessMediaDir = new File(Environment.getExternalStorageDirectory()+OPGSDKConstant.FILE_SEPARATOR + Utils.getApplicationName(context)+OPGSDKConstant.FILE_SEPARATOR+OPGSDKConstant.MEDIA+OPGSDKConstant.FORWARD_SLASH);
        File vitaccessMediaVideoDir = new File(vitaccessMediaDir + OPGSDKConstant.OPG_VIDEO);
        if (!vitaccessMediaVideoDir.exists()) {
            vitaccessMediaVideoDir.mkdirs();
        }
        String format = String.format(Locale.ENGLISH,OPGSDKConstant.VIDEO_REGEX , 3);
        File saveFile;
        do {
            String videofilename = OPGSDKConstant.VIDEO_ + String.format(Locale.ENGLISH, format, System.currentTimeMillis() / 1000L) +OPGSDKConstant.MP4 ;
            saveFile = new File(vitaccessMediaVideoDir, videofilename);
            mediaFilename = videofilename;
        } while (saveFile.exists());
        File sourceFile = new File(_CurrentMediaPath2);
        try {
            filename = copyDirectory(sourceFile, saveFile.getAbsoluteFile()).getAbsolutePath();
            _CurrentMediaPath2 = filename;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        _CurrentMediaPath = _CurrentMediaPath2;
        sendResult();



    }


    private void sendResult() {
        //System.out.println("Result Path : " + _CurrentMediaPath);
        if (_CurrentMediaPath.contains(OPGSDKConstant.IMAGE_CAPTURE_)) {
            /*compressImage(_CurrentMediaPath);*/
            new CompressImage(_CurrentMediaPath).execute();
            return;
        }
        //pluginResult = new PluginResult(PluginResult.Status.OK, OPEN_CURLY_BRACKET+BACK_SLASH + MEDIA_PATH_KEY +BACK_SLASH+COLON+BACK_SLASH + _CurrentMediaPath + BACK_SLASH+CLOSE_CURLY_BRACKET);
        pluginResult = new PluginResult(PluginResult.Status.OK, FileUtils.getJSONPathObj(_CurrentMediaPath));

        //System.out.println("pluginResult : " + pluginResult.getJSONString());
        ;
        pluginResult.setKeepCallback(true);
        if (callback != null)
            callback.sendPluginResult(pluginResult);

    }

    public boolean compressImage(String imageUri) {

        try
        {
            String filePath = imageUri;
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
           /* float maxHeight = 816.0f;
            float maxWidth = 612.0f;*/
            float maxHeight = actualHeight;
            float maxWidth = actualWidth;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2,
                    new Paint(Paint.FILTER_BITMAP_FLAG));

            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                //Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    //Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    //Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    //Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                        matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filename = null;
            if (isGallery) {
                File vitaccessMediaDir = new File(Environment.getExternalStorageDirectory()+OPGSDKConstant.FILE_SEPARATOR + Utils.getApplicationName(context)+OPGSDKConstant.FILE_SEPARATOR+OPGSDKConstant.MEDIA+OPGSDKConstant.FORWARD_SLASH);
                File vitaccessMediaImageDir = new File(vitaccessMediaDir + OPGSDKConstant.OPG_IMAGE);
                if (!vitaccessMediaImageDir.exists()) {
                    vitaccessMediaImageDir.mkdirs();
                }
                String format = String.format(Locale.ENGLISH, OPGSDKConstant.IMAGE_REGEX, 3);
                File saveFile;
                do {
                    String imagefilename = OPGSDKConstant.IMAGE_GALLERY_ + String.format(Locale.ENGLISH, format, System.currentTimeMillis() / 1000L) + OPGSDKConstant.JPEG;
                    saveFile = new File(vitaccessMediaImageDir, imagefilename);
                    mediaFilename = imagefilename;
                } while (saveFile.exists());
                File sourceFile = new File(_CurrentMediaPath);
                try {
                    filename = copyDirectory(sourceFile, saveFile.getAbsoluteFile()).getAbsolutePath();
                    _CurrentMediaPath = filename;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                filename = _CurrentMediaPath;
            }
            try {
                int compressionRate = 90;
                if(FileUtils.getFileSize(_CurrentMediaPath) > MAX_IMAGE_SIZE){
                    do{
                        out = new FileOutputStream(filename);
                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, compressionRate, out);
                        compressionRate = (int) (compressionRate * (.9));
                    }while (FileUtils.getFileSize(filename) > MAX_IMAGE_SIZE);
                }
                Log.i("FinalFileSize", FileUtils.getFileSize(_CurrentMediaPath) + "");
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;

    }

    public File copyDirectory(File sourceLocation, File targetLocation) throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                throw new IOException(OPGSDKConstant.CANNOT_CREATE_DIR + targetLocation.getAbsolutePath());
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else {

            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException(OPGSDKConstant.CANNOT_CREATE_DIR + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        return targetLocation;
    }

    private void sendResul(String path) {
        //System.out.println("Result Path JAVA 349: " + path);
        //pluginResult = new PluginResult(PluginResult.Status.OK, OPEN_CURLY_BRACKET+BACK_SLASH + MEDIA_PATH_KEY + BACK_SLASH+COLON+BACK_SLASH+ path +BACK_SLASH+CLOSE_CURLY_BRACKET);
        pluginResult = new PluginResult(PluginResult.Status.OK, FileUtils.getJSONPathObj(path));
        pluginResult.setKeepCallback(true);
        callback.sendPluginResult(pluginResult);
    }

    private class CompressImage extends AsyncTask<Void, Void, Boolean> {
        String imageURI;

        public CompressImage(String imageURI) {
            this.imageURI = imageURI;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new Dialog(cordova.getActivity(), android.R.style.Theme_Translucent);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(OPGR.getId(context, LAYOUT, OPGSDKConstant.PROGRESS_DIALOG));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return compressImage(this.imageURI);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                if (callback != null && _CurrentMediaPath != null) {
                    pluginResult = new PluginResult(PluginResult.Status.OK, FileUtils.getJSONPathObj(_CurrentMediaPath));
                    //System.out.println("pluginResult : " + pluginResult.getJSONString());
                    pluginResult.setKeepCallback(true);
                    callback.sendPluginResult(pluginResult);
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                Log.e(cordova.getActivity().getClass().getName(), e.toString());
            }
            progressDialog = null;
        }
    }

}
