package OnePoint.CordovaPlugin.Utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.allatori.annotations.DoNotRename;
import com.opg.sdk.BuildConfig;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Locale;

import OnePoint.Common.Utils;

import static com.opg.sdk.OPGSDKConstant.APPLICATION_OCTET_STREAM;
import static com.opg.sdk.OPGSDKConstant.AUDIO_FILE_UTILS;
import static com.opg.sdk.OPGSDKConstant.BACK_SLASH;
import static com.opg.sdk.OPGSDKConstant.COLON;
import static com.opg.sdk.OPGSDKConstant.COM_ANDROID_EXTERNAL_STORAGE_DOCUMENTS;
import static com.opg.sdk.OPGSDKConstant.COM_ANDROID_PROVIDERS_DOWNLOADS_DOCUMENTS;
import static com.opg.sdk.OPGSDKConstant.COM_GOOGLE_ANDROID_APPS_PHOTOS_CONTENT;
import static com.opg.sdk.OPGSDKConstant.CONTENT;
import static com.opg.sdk.OPGSDKConstant.CONTENT_DOWNLOADS_PUBLIC_DOWNLOADS;
import static com.opg.sdk.OPGSDKConstant.DOT;
import static com.opg.sdk.OPGSDKConstant.ERROR_ATTEMPTING_THUMBNAIL;
import static com.opg.sdk.OPGSDKConstant.ERROR_ATTEMPTING_THUMBNAIL_VIDEAO;
import static com.opg.sdk.OPGSDKConstant.FILE;
import static com.opg.sdk.OPGSDKConstant.FORWARD_SLASH;
import static com.opg.sdk.OPGSDKConstant.GB;
import static com.opg.sdk.OPGSDKConstant.GET_THUMB_NAIL;
import static com.opg.sdk.OPGSDKConstant.GOT_THUMB_ID;
import static com.opg.sdk.OPGSDKConstant.HTTP;
import static com.opg.sdk.OPGSDKConstant.HTTPS;
import static com.opg.sdk.OPGSDKConstant.IMAGE_CAPTURE_;
import static com.opg.sdk.OPGSDKConstant.IMAGE_FILE_UTILS;
import static com.opg.sdk.OPGSDKConstant.IMAGE_REGEX;
import static com.opg.sdk.OPGSDKConstant.JPEG;
import static com.opg.sdk.OPGSDKConstant.KB;
import static com.opg.sdk.OPGSDKConstant.MB;
import static com.opg.sdk.OPGSDKConstant.MEDIA;
import static com.opg.sdk.OPGSDKConstant.MIME_TYPE_IMAGE;
import static com.opg.sdk.OPGSDKConstant.OPG_IMAGE;
import static com.opg.sdk.OPGSDKConstant.PATH;
import static com.opg.sdk.OPGSDKConstant.PRIMARY;
import static com.opg.sdk.OPGSDKConstant.REGEX;
import static com.opg.sdk.OPGSDKConstant.TYPE;
import static com.opg.sdk.OPGSDKConstant.VIDEO;
import static com.opg.sdk.OPGSDKConstant.VIDEO_FILE_UTILS;
import static com.opg.sdk.OPGSDKConstant._DATA;
import static com.opg.sdk.OPGSDKConstant._ID_EQUAL;
import static com.opg.sdk.OPGSDKConstant.com_android_providers_media_documents;

@DoNotRename
public class FileUtils {
    public static final String HIDDEN_PREFIX = ".";
    /**
     * TAG for log messages.
     */
    static final String TAG = "FileUtils";
    //private static final boolean DEBUG = false; // Set to true to enable logging
    private static final String MEDIAID = "MediaID";
    /**
     * File and folder comparator. TODO Expose sorting option method
     *
     * @author paulburke
     */
    @DoNotRename
    public static Comparator<File> sComparator = new Comparator<File>() {
        @Override
        public int compare(File f1, File f2) {
            // Sort alphabetically by lower case, which is much cleaner
            return f1.getName().toLowerCase(Locale.US)
                           .compareTo(f2.getName().toLowerCase(Locale.US));
        }
    };
    /**
     * File (not directories) filter.
     *
     * @author paulburke
     */
    @DoNotRename
    public static FileFilter sFileFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return files only (not directories) and skip hidden files
            return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX);
        }
    };
    /**
     * Folder (directories) filter.
     *
     * @author paulburke
     */
    @DoNotRename
    public static FileFilter sDirFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return directories only and skip hidden directories
            return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX);
        }
    };

    private FileUtils() {
    } // private constructor to enforce Singleton pattern

	/**
	 * Gets the extension of a file name, like ".png" or ".jpg".
	 *
	 * @param uri
	 * @return Extension including the dot("."); "" if there is no extension;
	 *         null if uri was null.
	 */
	@DoNotRename
	public static String getExtension(String uri) {
		if (uri == null) {
			return null;
		}

		int dot = uri.lastIndexOf(DOT);
		if (dot >= 0) {
			return uri.substring(dot);
		} else {
			// No extension.
			return "";
		}
	}

	@DoNotRename
	public static String getJSONPathObj(String path){
		JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PATH, path);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("Error",e.toString());
		}
		return jsonObject.toString();
	}

	@DoNotRename
	public static String getJSONMediaPathObj(String mediaID, String path){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put(MEDIAID, mediaID);
            jsonObject.put(PATH, path);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("Error",e.toString());
		}
		return jsonObject.toString();
	}

	/**
	 * @return Whether the URI is a local one.
	 */
	@DoNotRename
	public static boolean isLocal(String url) {
		if (url != null && !url.startsWith(HTTP)
				&& !url.startsWith(HTTPS)) {
			return true;
		}
		return false;
	}

	/**
	 * @return True if Uri is a MediaStore Uri.
	 * @author paulburke
	 */
	@DoNotRename
	public static boolean isMediaUri(Uri uri) {
		return MEDIA.equalsIgnoreCase(uri.getAuthority());
	}

	/**
	 * Convert File into Uri.
	 *
	 * @param file
	 * @return uri
	 */
	@DoNotRename
	public static Uri getUri(File file) {
		if (file != null) {
			return Uri.fromFile(file);
		}
		return null;
	}

	/**
	 * Returns the path only (without file name).
	 *
	 * @param file
	 * @return
	 */
	@DoNotRename
	public static File getPathWithoutFilename(File file) {
		if (file != null) {
			if (file.isDirectory()) {
				// no file to be split off. Return everything
				return file;
			} else {
				String filename = file.getName();
				String filepath = file.getAbsolutePath();

				// Construct path without file name.
				String pathwithoutname = filepath.substring(0,
						filepath.length() - filename.length());
				if (pathwithoutname.endsWith(BACK_SLASH)) {
					pathwithoutname = pathwithoutname.substring(0,
							pathwithoutname.length() - 1);
				}
				return new File(pathwithoutname);
			}
		}
		return null;
	}

	/**
	 * @return The MIME type for the given file.
	 */
	@DoNotRename
	public static String getMimeType(File file) {

		String extension = getExtension(file.getName());

		if (extension.length() > 0)
			return MimeTypeMap.getSingleton().getMimeTypeFromExtension(
					extension.substring(1));

		return APPLICATION_OCTET_STREAM;
	}

	/**
	 * @return The MIME type for the give Uri.
	 */
	@DoNotRename
	public static String getMimeType(Context context, Uri uri) {
		File file = new File(getPath(context, uri));
		return getMimeType(file);
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 * @author paulburke
	 */
	@DoNotRename
	public static boolean isExternalStorageDocument(Uri uri) {
		return COM_ANDROID_EXTERNAL_STORAGE_DOCUMENTS.equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 * @author paulburke
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return COM_ANDROID_PROVIDERS_DOWNLOADS_DOCUMENTS.equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 * @author paulburke
	 */
	@DoNotRename
	public static boolean isMediaDocument(Uri uri) {
		return com_android_providers_media_documents.equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	@DoNotRename
	public static boolean isGooglePhotosUri(Uri uri) {
		return COM_GOOGLE_ANDROID_APPS_PHOTOS_CONTENT.equals(uri
				.getAuthority());
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 * @author paulburke
	 */
	@DoNotRename
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = _DATA;
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
                if (BuildConfig.DEBUG)
                    DatabaseUtils.dumpCursor(cursor);

				final int column_index = cursor.getColumnIndexOrThrow(column);
				String path = cursor.getString(column_index);
				if(path == null)
				{
					final InputStream ist = context.getContentResolver().openInputStream(uri);
					File vitaccessMediaDir = new File(Environment.getExternalStorageDirectory() + File.separator + Utils.getApplicationName(context) + File.separator + MEDIA + FORWARD_SLASH);
					File vitaccessMediaImageDir = new File(vitaccessMediaDir + OPG_IMAGE);
					if (!vitaccessMediaImageDir.exists()) {
						vitaccessMediaImageDir.mkdir();
					}
					String format = String.format(Locale.ENGLISH, IMAGE_REGEX, 3);
					File saveFile;
					do {
						String filename = IMAGE_CAPTURE_ + String.format(Locale.ENGLISH, format, System.currentTimeMillis() / 1000L) + JPEG;
						saveFile = new File(vitaccessMediaImageDir, filename);
					} while (saveFile.exists());
					copyToFile(ist,saveFile);
					return  saveFile.getAbsolutePath();
				}
				else
				{
					return path;
				}
			}
		}
		catch (Exception ex)
		{

		}
		finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	private static boolean copyToFile(InputStream inputStream, File destFile) {
		if (inputStream == null || destFile == null) return false;
		try {
			OutputStream out = new FileOutputStream(destFile);
			try {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				out.close();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.<br>
	 * <br>
	 * Callers should check whether the path is local before assuming it
	 * represents a local file.
	 *
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @see #isLocal(String)
	 * @see #getFile(Context, Uri)
	 * @author paulburke
	 */
	@TargetApi(19)
	@DoNotRename
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(COLON);
				final String type = split[0];

				if (PRIMARY.equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + BACK_SLASH
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse(CONTENT_DOWNLOADS_PUBLIC_DOWNLOADS),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(COLON);
				final String type = split[0];

				Uri contentUri = null;
				if (IMAGE_FILE_UTILS.equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if (VIDEO_FILE_UTILS.equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if (AUDIO_FILE_UTILS.equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = _ID_EQUAL;
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if (CONTENT.equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if (FILE.equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Convert Uri into File, if possible.
	 *
	 * @return file A local file that the Uri was pointing to, or null if the
	 *         Uri is unsupported or pointed to a remote resource.
	 * @see #getPath(Context, Uri)
	 * @author paulburke
	 */
	@DoNotRename
	public static File getFile(Context context, Uri uri) {
		if (uri != null) {
			String path = getPath(context, uri);
			if (path != null && isLocal(path)) {
				return new File(path);
			}
		}
		return null;
	}

	/**
	 * Get the file size in a human-readable string.
	 *
	 * @param size
	 * @return
	 * @author paulburke
	 */
	@DoNotRename
	public static String getReadableFileSize(int size) {
		final int BYTES_IN_KILOBYTES = 1024;
		final DecimalFormat dec = new DecimalFormat(REGEX);
		final String KILOBYTES = KB;
		final String MEGABYTES = MB;
		final String GIGABYTES = GB;
		float fileSize = 0;
		String suffix = KILOBYTES;

		if (size > BYTES_IN_KILOBYTES) {
			fileSize = size / BYTES_IN_KILOBYTES;
			if (fileSize > BYTES_IN_KILOBYTES) {
				fileSize = fileSize / BYTES_IN_KILOBYTES;
				if (fileSize > BYTES_IN_KILOBYTES) {
					fileSize = fileSize / BYTES_IN_KILOBYTES;
					suffix = GIGABYTES;
				} else {
					suffix = MEGABYTES;
				}
			}
		}
		return String.valueOf(dec.format(fileSize) + suffix);
	}

	/**
	 * Attempt to retrieve the thumbnail of given File from the MediaStore. This
	 * should not be called on the UI thread.
	 *
	 * @param context
	 * @param file
	 * @return
	 * @author paulburke
	 */
	@DoNotRename
	public static Bitmap getThumbnail(Context context, File file) {
		return getThumbnail(context, getUri(file), getMimeType(file));
	}

	/**
	 * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
	 * should not be called on the UI thread.
	 *
	 * @param context
	 * @param uri
	 * @return
	 * @author paulburke
	 */
	@DoNotRename
	public static Bitmap getThumbnail(Context context, Uri uri) {
		return getThumbnail(context, uri, getMimeType(context, uri));
	}

	/**
	 * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
	 * should not be called on the UI thread.
	 *
	 * @param context
	 * @param uri
	 * @param mimeType
	 * @return
	 * @author paulburke
	 */
	@DoNotRename
	public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {
        if (BuildConfig.DEBUG)
            Log.d(TAG,ERROR_ATTEMPTING_THUMBNAIL);

        if (!isMediaUri(uri)) {
            if (BuildConfig.DEBUG)
                Log.e(TAG,ERROR_ATTEMPTING_THUMBNAIL_VIDEAO );
			return null;
		}

		Bitmap bm = null;
		if (uri != null) {
			final ContentResolver resolver = context.getContentResolver();
			Cursor cursor = null;
			try {
				cursor = resolver.query(uri, null, null, null, null);
				if (cursor.moveToFirst()) {
                    final int id = cursor.getInt(0);
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, GOT_THUMB_ID + id);

					if (mimeType.contains(VIDEO)) {
						bm = MediaStore.Video.Thumbnails
								.getThumbnail(resolver, id,
										MediaStore.Video.Thumbnails.MINI_KIND,
										null);
					} else if (mimeType.contains(MIME_TYPE_IMAGE)) {
						bm = MediaStore.Images.Thumbnails.getThumbnail(
								resolver, id,
								MediaStore.Images.Thumbnails.MINI_KIND, null);
                    }
                }
            } catch (Exception e) {
                if (BuildConfig.DEBUG)
                    Log.e(TAG, GET_THUMB_NAIL, e);
			} finally {
				if (cursor != null)
					cursor.close();
			}
		}
		return bm;
	}

	/**
	 * Get the Intent for selecting content to be used in an Intent Chooser.
	 *
	 * @return The intent for opening a file with Intent.createChooser()
	 * @author paulburke
	 */
	@DoNotRename
	public static Intent createGetContentIntent() {
		// Implicitly allow the user to select a particular kind of data
		final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// The MIME data type filter
		intent.setType(TYPE);
		// Only return URIs that can be opened with ContentResolver
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		return intent;
	}
	@DoNotRename
	public static byte[] convertFileToByteArray(File f)
	throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(f);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		try {
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum); // no doubt here is 0
				// Writes len bytes from the specified byte array starting at
				// offset off to this byte array output stream.
				//System.out.println("read " + readNum + " bytes,");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		byte[] byteArray = bos.toByteArray();
		//System.out.println("BYTE ARRY : "+byteArray.toString());
		return byteArray;
	}

    /**
     * This method is used to get the file size of a file present in specific filepath
     * @param filePath
     * @return
     */
    @DoNotRename
    public static long getFileSize(String filePath){
        File f = new File(filePath);
        if(f.exists()){
            return f.length();
        }
        return 0;
    }
}
