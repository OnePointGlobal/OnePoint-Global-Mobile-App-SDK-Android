package com.onepointglobal.mysurveysn;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.opg.sdk.models.OPGDownloadMedia;

/**
 * The type Download media activity.
 */
public class DownloadMediaActivity extends AppCompatActivity
{
    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;
    /**
     * The M context.
     */
    Context mContext;
    /**
     * The Download btn.
     */
    Button download_btn;
    /**
     * The Path tv.
     */
    TextView path_tv;
    /**
     * The Uniqued id et.
     */
    EditText uniquedID_et, /**
 * The Media id et.
 */
mediaID_et, /**
 * The Media type et.
 */
mediaType_et;
    /**
     * The Media id.
     */
    String mediaID, /**
 * The Media type.
 */
mediaType;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_media);
        mContext          = this;
        uniquedID_et      = (EditText)findViewById(R.id.uniqueid_et);
        mediaID_et        = (EditText)findViewById(R.id.mediaid);
        mediaType_et      = (EditText)findViewById(R.id.mediatype);
        download_btn      = (Button)findViewById(R.id.download_btn);
        path_tv           = (TextView)findViewById(R.id.path_tv);
        progressDialog    = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaType = mediaType_et.getText().toString().trim();
                mediaID   = mediaID_et.getText().toString().trim();
                if(Util.isOnline(DownloadMediaActivity.this))
                {
                    new DownloadMedia(mediaID,mediaType).execute();
                }
                else
                {
                    Util.showAlert(DownloadMediaActivity.this);
                }

            }
        });
    }
    private class DownloadMedia extends AsyncTask<String ,String ,OPGDownloadMedia>{

        /**
         * The Media id.
         */
        String mediaID, /**
         * The Media type.
         */
        mediaType;
        /**
         * The Url.
         */
        String url ;

        /**
         * Instantiates a new Download media.
         *
         * @param mediaID   the media id
         * @param mediaType the media type
         */
        public DownloadMedia(String mediaID, String mediaType) {
            this.mediaID   = mediaID;
            this.mediaType = mediaType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }


        @Override
        protected OPGDownloadMedia doInBackground(String... strings) {
            //Download any media by passing mediaID and media  type
            return Util.getOPGSDKInstance().downloadMediaFile(DownloadMediaActivity.this,mediaID,mediaType);
        }

        @Override
        protected void onPostExecute(OPGDownloadMedia opgDownloadMedia) {
            super.onPostExecute(opgDownloadMedia);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(opgDownloadMedia.isSuccess()){
                path_tv.setText("Download media path:"+opgDownloadMedia.getMediaPath());
            }else{
                path_tv.setText("Status:"+opgDownloadMedia.getStatusMessage());
            }

        }
    }
}
