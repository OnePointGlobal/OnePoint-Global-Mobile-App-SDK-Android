package com.onepointglobal.mysurveysn;

import android.content.CursorLoader;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.opg.sdk.OPGSDK;

/**
 * The type Upload media activity.
 */
public class UploadMediaActivity extends AppCompatActivity {

    /**
     * The Image btn.
     */
    Button image_btn, /**
     * The Voice btn.
     */
    voice_btn, /**
     * The Video btn.
     */
    video_btn, /**
     * The Upload btn.
     */
    upload_btn;
    /**
     * The Path tv.
     */
    TextView path_tv;
    /**
     * The Uniqued id.
     */
    EditText uniquedID;


    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_VIDEO_REQUEST = 2;
    private int PICK_AUDIO_REQUEST = 3;
    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;
    /**
     * The M context.
     */
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_media);
        image_btn         = (Button)findViewById(R.id.img_btn);
        voice_btn         = (Button)findViewById(R.id.voice_btn);
        video_btn         = (Button)findViewById(R.id.video_btn);
        path_tv           = (TextView)findViewById(R.id.path_tv);
        upload_btn        = (Button)findViewById(R.id.upload_btn);
        uniquedID         = (EditText)findViewById(R.id.uniqueid_et);
        mContext          = this;
        progressDialog    = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Uploading...");
        path_tv.setText("");
        uniquedID.setText(Util.getOPGSDKInstance().getUniqueID(mContext));

        image_btn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        voice_btn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Audio"), PICK_AUDIO_REQUEST);
            }
        });

        video_btn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Util.isOnline(UploadMediaActivity.this))
                {
                    new UploadMediaTask(path_tv.getText().toString().trim()).execute();
                }
                else
                {
                    Util.showAlert(UploadMediaActivity.this);
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String path = getImageRealPath(uri);
            path_tv.setText(path);
        }else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String path = getVideoRealPath(uri);
            path_tv.setText(path);
        }else if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String path = getAudioRealPath(uri);//getAudioRealPath(uri);
            path_tv.setText(path);
        }
    }

    /**
     * Gets image real path.
     *
     * @param uri the uri
     * @return the image real path
     */
    public String getImageRealPath(Uri uri)
    {
        String filePath = null;
        // SDK < API11
        if(Build.VERSION.SDK_INT < 11)
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
        }
        // SDK >= 11 && SDK < 19
        else if(Build.VERSION.SDK_INT < 19)
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if(cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(column_index);
            }
        }
        // SDK > 19 (Android 4.4)
        else
        {
            String wholeID = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if(cursor.moveToFirst())
            {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }

        return filePath;
    }

    /**
     * Gets video real path.
     *
     * @param uri the uri
     * @return the video real path
     */
    public String getVideoRealPath(Uri uri)
    {
        String filePath = null;
        // SDK < API11
        if(Build.VERSION.SDK_INT < 11)
        {
            String[] proj = {MediaStore.Video.Media.DATA};
            Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
        }
        // SDK >= 11 && SDK < 19
        else if(Build.VERSION.SDK_INT < 19)
        {
            String[] proj = {MediaStore.Video.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if(cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(column_index);
            }
        }
        // SDK > 19 (Android 4.4)
        else
        {
            String wholeID = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Video.Media.DATA};
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = this.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, column,sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if(cursor.moveToFirst())
            {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath;
    }

    /**
     * Get audio real path string.
     *
     * @param uri the uri
     * @return the string
     */
    public String getAudioRealPath(Uri uri){
        String filePath = null;
        // SDK < API11
        if(Build.VERSION.SDK_INT < 11)
        {
            String[] proj = {MediaStore.Audio.Media.DATA};
            Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
        }
        // SDK >= 11 && SDK < 19
        else if(Build.VERSION.SDK_INT < 19)
        {
            String[] proj = {MediaStore.Audio.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if(cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(column_index);
            }
        }
        // SDK > 19 (Android 4.4)
        else
        {
            try
            {
                String wholeID = DocumentsContract.getDocumentId(uri);
                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Audio.Media.DATA};
                // where id is equal to
                String sel = MediaStore.Audio.Media._ID + "=?";
                Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, column,sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if(cursor.moveToFirst())
                {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
        return filePath;

    }

    private class UploadMediaTask extends AsyncTask<String ,String ,String> {

        /**
         * The Exception obj.
         */
        Exception exceptionObj;

        /**
         * The Media path.
         */
        String mediaPath;

        /**
         * Instantiates a new Upload media task.
         *
         * @param mediaPath the media path
         */
        public UploadMediaTask(String mediaPath) {
            this.mediaPath = mediaPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            String mediaId = null;
            try
            {
                //uploading media
                mediaId = Util.getOPGSDKInstance().uploadMediaFile(mediaPath,mContext);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                exceptionObj = e;
            }
            return mediaId;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(s != null){
                path_tv.setText(s);
            }else{
                if(exceptionObj!=null){
                    path_tv.setText(exceptionObj.toString());
                }
            }
        }
    }
}
