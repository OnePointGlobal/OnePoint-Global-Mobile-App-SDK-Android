package com.opg.sdk;

import android.app.Activity;
import android.content.Context;

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
* 
* OPGR class.
*/
public class OPGR {
	private Context context;
	private String packageName;

	public OPGR(Activity activity) {
		this.context     = activity.getApplicationContext();
		this.packageName = this.context.getPackageName();
	}

	public OPGR(Context context) {
		this.context     = context;
		this.packageName = context.getPackageName();
	}

	/***
	 * Fetches the view/layout id from the R.java based on the below params
	 * @param group
	 * @param key
	 * @return
	 */
	public int getId(String group, String key) {
		return this.context.getResources().getIdentifier(key, group, this.packageName);
	}

	/**
	 * Fetches the view/layout id from the R.java based on the below params
	 * @param context
	 * @param group
	 * @param key
	 * @return
	 */
	public static int getId(Context context, String group, String key) {
		return context.getResources().getIdentifier(key, group, context.getPackageName());
	}

	/***
	 * Fetches the String  from the R.java based on the below params
	 * @param group
	 * @param key
	 * @return
	 */
	public String getString(String group, String key) {
		return context.getString(context.getResources().getIdentifier(key, group, this.packageName));
	}

	/**
	 * Fetches the String from the R.java based on the below params
	 * @param context
	 * @param group
	 * @param key
	 * @return
	 */
	public static String getString(Context context, String group, String key) {
		return context.getString(context.getResources().getIdentifier(key, group, context.getPackageName()));
	}
}
