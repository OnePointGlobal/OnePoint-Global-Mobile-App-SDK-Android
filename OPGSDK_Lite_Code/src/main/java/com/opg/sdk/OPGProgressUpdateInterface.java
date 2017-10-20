package com.opg.sdk;

import com.allatori.annotations.DoNotRename;
import com.opg.sdk.models.OPGScript;

/**
 * Created by kiran on 15-12-2016.
 */
@DoNotRename
public interface OPGProgressUpdateInterface
{
 @DoNotRename
 void updateProgress(int progress);
 @DoNotRename
 void onCompleted(OPGScript opgScript);
 @DoNotRename
 void onError(OPGScript opgScript);
 @DoNotRename
 void showMessage(String msg);
}
