package com.opg.sdk;

import com.allatori.annotations.DoNotRename;
import com.opg.sdk.models.OPGUploadResult;

/**
 * Created by kiran on 11-01-2017.
 */
@DoNotRename
public interface OPGUploadProgress {
    @DoNotRename
    void updateProgress(int progress);

    @DoNotRename
    void onCompleted(OPGUploadResult opgUploadResult);

    @DoNotRename
    void onError(OPGUploadResult opgUploadResult);

    @DoNotRename
    void showMessage(String msg);
}
