package com.opg.sdk;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.allatori.annotations.DoNotRename;
import com.opg.sdk.exceptions.OPGException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.opg.sdk.OPGSDKConstant.CAUSED_BY;
import static com.opg.sdk.OPGSDKConstant.ERROR_FILE_NOT_FOUND;
import static com.opg.sdk.OPGSDKConstant.ID;
import static com.opg.sdk.OPGSDKConstant.IMAGE_PREVIEW;
import static com.opg.sdk.OPGSDKConstant.IMAGE_PREVIEW_IOPG;
import static com.opg.sdk.OPGSDKConstant.LAYOUT;
import static com.opg.sdk.OPGSDKConstant.OK_BOPG;
import static com.opg.sdk.OPGSDKConstant.PATH;
import static com.opg.sdk.OPGSDKConstant.SUCCESS;

///////////////////////////////////////////////////////////////////////////////
//
//Copyright (c) 2016 OnePoint Global Ltd. All rights reserved.
//
//This code is licensed under the OnePoint Global License.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.
//
///////////////////////////////////////////////////////////////////////////////

/**
 * This is a plugin used to display an image.
 */
@DoNotRename
public class ImagePreviewPlugin extends CordovaPlugin {

	Context context;
	CallbackContext callback;
	@DoNotRename@Override
	public boolean execute(String action, final CordovaArgs args,
			final CallbackContext callbackContext) {
		this.context = this.cordova.getActivity();
		this.callback = callbackContext;
		try {
			JSONObject obj = args.getJSONObject(0);
			if (obj.get(PATH) != null) {
				String path = obj.get(PATH).toString();
				/*OPGCustomDialog cDialog = new OPGCustomDialog(context);
				cDialog.showImage(path);*/
				showImage(path);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			callback.error(e.getLocalizedMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			callback.error(e.getLocalizedMessage());
		}catch (Exception e) {
			e.printStackTrace();
			callback.error(e.getLocalizedMessage());
		}
		callback.success(SUCCESS);
		return true;
	}

	/**
	 * To Display the image in a dialog
	 * @param path
	 * @throws FileNotFoundException
	 * @throws Exception
	 */

	@DoNotRename
	private void showImage(String path) throws FileNotFoundException, Exception {
		//OPGR opg_r = new OPGR(context);
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(OPGR.getId(context ,LAYOUT, IMAGE_PREVIEW));
		ImageView image = (ImageView) dialog.findViewById(OPGR.getId(context, ID, IMAGE_PREVIEW_IOPG));
		Button ok    = (Button) dialog.findViewById(OPGR.getId(context, ID, OK_BOPG));
		image.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(
				new File(path))));
		File imagePath = new File(path);
		if(imagePath.exists()){
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath(),bmOptions);
			if(image!=null){
				image.setImageBitmap(bitmap);
			}
			dialog.show();
		}else{
			throw (new OPGException(CAUSED_BY+ERROR_FILE_NOT_FOUND));
		}
		dialog.show();

		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

}
