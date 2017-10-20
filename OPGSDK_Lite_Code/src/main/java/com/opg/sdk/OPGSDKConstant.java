package com.opg.sdk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.provider.Settings;

import java.io.File;

/**
 * Created by neeraj on 20-10-2016.
 */

public class OPGSDKConstant
{
    //API routes
    public static  final String authenticateAPIRoute = "Authentication";
    public static  final String googleAuthenticateAPIRoute   = "SocialLogin/GoogleLogin";
    public static  final String facebookAuthenticateAPIRoute = "SocialLogin/FacebookLogin";
    public static  final String surveyAPIRoute = "Survey/Surveys";
    public static  final String changePasswordAPIRoute = "ChangePassword";
    public static  final String forgotPasswordAPIRoute = "ForgotPassword";
    public static  final String panellistProfileAPIRoute = "PanellistProfile/Profiles";
    public static  final String panellistPanelAPIRoute = "PanellistPanel/Panels";
    public static  final String updatePanellistProfileAPIRoute = "UpdatePanelList/Update";
    public static  final String scriptAPIRoute = "Script";
    public static  final String notificationRegisterAPIRoute = "PushNotification/Post";
    public static  final String notificationUnRegisterAPIRoute = "PushNotification/Delete";
    public static  final String countriesAPIRoute = "UpdatePanelList/Country";
    public static  final String geofencingAPIRoute = "Geofencing/Geofencing";
    public static  final String resultAPIRoute = "Result/Post";


    public static final String  UPLOAD_ALL = "uploadAll";
    public static final String UNIQUE_ID_ERROR = "UniqueID does not exist.";
    public static final String SESSION_TIME_OUT_ERROR = "Session expired.Please authenticate again";

    public static final String  OPGSDK = "OPGSDK";


    /*Key Value pair for String used as argument*/
              /*OPGNetworkRequest*/
    public static final String POST = "POST";
    public static final String MEDIA_PROFILE_MEDIA = "Media/ProfileMedia";
    public static final String DATA = "Data";
    public static final String APPLICATION_JSON_CHARSET = "application/json;charset=utf-8";
    public static final String APPLICATION_JSON = "application/json";
    public static final String BASIC = "Basic ";
    public static final String USER_AGENT = "USER-AGENT";
    public static final String USER_AGENT_NAME = "Mozilla/5.0";
    public static final String ACCEPT_LANGUAGE = "ACCEPT-LANGUAGE";
    public static final String ACCEPT_LANGUAGE_NAME = "en-US,en;0.5";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String UNKNOWN_EXCEPTION = "Unknown Exception";
    public static final String UTF = "UTF-8";
    public static final String CONNECTION = "Connection";
    public static final String CONNECTION_TYPE = "Keep-Alive";
    public static final String ENCTYPE = "ENCTYPE";
    public static final String ENCTYPE_NAME = "multipart/form-data";
    public static final String CONTENT_TYPE_FOR_FILE_UPLOAD = "multipart/form-data;boundary=";
    public static final String FILE = "file";
    public static final String CONTENT_DISPOSITION = "Content-Disposition: form-data; name=\"file\";filename=\"" ;
    public static final String MEDIA_ID = "mediaid=";
    public static final String MEDIA_TYPE = "&mediatype=";
    public static final String IMAGE = "Image/";
    public static final String VIDEO = "video/";
    public static final String AUDIO = "audio/";
    public static final String BACK_SLASH = "\"";
    public static final String FORWARD_SLASH = "/";

    public static final String LINE_END ="\r\n";
    public static final String BOUNDARY = "*****";


    /*OPGParseResult*/
    public static final  String  UNIQUE_ID = "UniqueID";
    public static final  String URL = "Url";
    public static final  String INTERVIEW_URL = "InterviewUrl";
    public static final  String ERROR_MESSAGE = "ErrorMessage";
    public static final  String HTTP_STATUS_CODE = "HttpStatusCode";
    public static final  String MESSAGE = "Message";
    public static final String SUCCESS = "Success";
    public static final String ERROR_AUTH = "Authorization response is null or empty";
    public static final String COUNTRY = "Country";
    public static final String ERROR_PANELIST_PROFILE = "Response from server for PanelistProfile is null or empty";
    public static final String ERROR_UPDATE_PANELIST_PROFILE = "Response from server for UpdatePanelistProfile is null or empty";
    public static final String SCRIPT_CONTENT = "ScriptContent";
    public static final String BYTE_CODE = "ByteCode";
    public static final String ERROR_NO_SCRIPT = "No Script for this SurveyReference";
    public static final String ERROR_SCRIPT = "response from server for script is null or empty.";
    public static final String EXCEPTION_OCCURRED = "Exception occurred : ";
    public static final String ERROR_THEME = "response from server for getThemes is null or empty.";
    public static final String ERROR_NULL_RESPONSE = "Response from server in empty or null";
    public static final String THEMES = "Themes";
    public static final String PANELS = "Panels";
    public static final String PANEL_PANELLIST = "PanelPanellist";
    public static final String SURVEYPANEL = "SurveyPanel";
    public static final String CAUSED_BY = "Caused by: ";
    public static final String ERROR_UPLOAD_FILE = "Failed to upload the file";
    public static final String DATE_FORMAT_T = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String SCRIPTS = "Scripts";
    public static final String OPGS = ".opgs";
    public static final String OPEN_SQUARE_BRACKET = "[";
    public static final String CLOSE_SQUARE_BRACKET = "]";
    public static final String OPEN_CURLY_BRACKET = "{";
    public static final String CLOSE_CURLY_BRACKET = "}";

    /*OPGRequest*/
    public static final String USERNAME        = "UserName";
    public static final String PASSWORD        = "Password";
    public static final String APP_VERSION     = "AppVersion";
    public static final String SIGNIN_TIME_UTC = "SigninTimeUtc";
    public static final String EMAIL_ID        = "emailID";
    public static final String GoogleToken     = "GoogleToken";
    public static final String FacebookToken   = "FacebookToken";
    public static final String MD5             = "MD5";
    public static final String ZERO        = "0";
    public static final String SESSIONID     = "SessionID";
    public static final String CURRENT_PASSWORD = "CurrentPassword";
    public static final String NEW_PASSWORD        = "NewPassword";
    public static final String PANEL_ID   = "PanelID";
    public static final String LATITUDE   = "Latitude";
    public static final String LONGITUDE   = "Longitude";
    public static final String COUNTRY_CODE   = "CountryCode";
    public static final String SURVEY_REF   = "SurveyRef";
    public static final String DEVICE_TOKEN_ID   = "DeviceTokenID";
    public static final String PLATFORM   = "Platform";
    public static final String TWO   = "2";
    public static final String VERSION   = "Version";
    public static final String DEVICE_ID   = "DeviceID";

    /**************OPGRoot**********/
    public static final  String ERROR_NULL_ADMIN_NAME = "Null or empty values for admin username.";
    public static final  String ERROR_NULL_UNIQUE_ID = "Null or empty values for uniqueid.";
    public static final  String ERROR_NULL_SHARED_KEY = "Null or empty values for admin sharedkey.";
    public static final  String  ERROR_MESSAGE_OPGROOT = "Caused by: Null or empty credential values for either admin username or sharedkey";
    public static final  String FAILED_INITIALIZE = "OPGSDK failed to initialize due to null or empty credential values for either admin username or sharedkey";
    public static final  String SUCCESS_INITIALIZE = "OPGSDK initialised successfully";
    public static final  String ERROR_NULL_APP_VERSION = "Caused by: Null or empty  values for appversion";
    public static final  String ERROR_NULL_PANEL_ID = "Null or empty values for paneliid.";
    public static final  String ERROR_NULL_LATITUDE = "Null or empty values for  latitude.";
    public static final  String ERROR_NULL_LONGITUDE = "Null or empty values for  longitude.";
    public static final  String ERROR_NULL_LOGIN_USERNAME = "Null or empty values for login username.";
    public static final  String ERROR_NULL_LOGIN_PASSWORD = "Null or empty values for login password.";
    public static final  String ERROR_NULL_GOOGLE_TOKENID = "Null or empty values for google tokenID.";
    public static final  String ERROR_NULL_FACEBOOK_TOKENID = "Null or empty values for facebook tokenID.";
    public static final  String ERROR_NULL_CURRENT_PASSWORD = "Null or empty values for current password.";
    public static final  String ERROR_NULL_NEW_PASSWORD = "Null or empty values for new password.";
    public static final  String ERROR_PASSWORD_SHOULD_SAME = "Current password and new password should not be same";
    public static final  String ERROR_NULL_EMAILID = "Null or empty values for email id.";
    public static final  String ERROR_NULL_SURVEY_REFERENCE = "Null or empty values for SurveyReference.";
    public static final  String ERROR_INVALIDE_MAIL_ID = "Invalid email id";
    public static final  String MEDIA_POST ="Media/Post";
    public static final  String MEDIA_POST_DATA = "Media/Post?Data=";
    public static final  String ERROR_FILE_NOT_FOUND = "Caused by:File not found";
    public static final  String DOWNLOAD_SUCCESSFUL = "Downloaded the media successfully.";
    public static final  String ERROR_FAILED_DOWNLOAD = "Failed to download  the media.";
    public static final  String ERROR_NULL_MEDIA_PATH = "Null or empty values for media path.";
    public static final  String ERROR_NULL_MEDIA_TYPE = "Null or empty values for media type.";
    public static final  String ERROR_NULL_DEVICE_TOKEN = "Null or empty values for Device Token.";
    public static final  String DATE_REGEX = "%%0%dd";
    public static final  String DATE_FORMATE_YYMMDD = "yyyyMMdd";
    public static final  String OPG_MEDIA = "OpgMedia/";
    public static final  String OPG = "-OPG";
    public static final  String DOT = ".";
    public static final  String NEW_LINE = "\n";
    public static final String CARRIAGE_RETURN ="\r";
    public static final  String SEMI_COLON =";";
    public static final  String SINGLE_QUOTE ="\'";
    public static final  String DOUBLE_QUOTE ="";

    public static final  String APP_MEDIA = "\n";
    public static final  String GEOFENCE_MONITORING_ADDED = "Successfully added geofence for monitoring";
    public static final  String GEOFENCE_MONITORING_REMOVED = "Successfully removed geofence from monitoring....";
    public static final  String INVALIDE_LOCATION_PERMISSION = "Invalid location permission. You need to use ACCESS_FINE_LOCATION with geofences";
    public static final  String EXCEPTION_GPS_ENABLED = "Exception gps_enabled";
    public static final  String EXCEPTION_NETWORK_ENABLED = "Exception network_enabled";


    /**************OPGActivity*/
    public static final String TARGET_HTML = "TARGET_HTML.html";
    public static final String OPG_IMG_PATH_DATA = "opgImgPathData";
    public static final String OPG_SURVEY_ID_DATA = "opgsurveyIdData";
    public static final String SCRIPT_CLASS = "script.class";
    public static final String ZIP = ".zip";


    /*****************OPGGetSurveyURL**************/
    public static final String PANELLISTID = "panellistID";
    public static final String ANDROID = "android";
    public static final String SEV = "SEV";
    public static final String SEV_VALUE = "B1";
    public static final String DATA1 = "?data=";
    public static final String SURVEY_REFERENCE = "surveyReference";
    public static final String EQUAL = "=";
    public static final String PANELID = "panelID";
    public static final String AND = "&";


    public static final String TRIGGERED_GEOFENCES = "triggeredGeofences";
    public static final String HYPHEN = "-";
    public static final String COLON = ":";
    public static final String COMA = ", ";

    /************************Aes256******/
    public static final String CIPHER_KEY = "AES/CBC/PKCS5Padding";
    public static final String AES = "AES";
    public static final String UTF_16LE = "UTF-16LE";
    public static final String METHOD_GET_MEDIA_SESSION_ID =  "method=getmedia&sessionid=";
    public static final String MEDIA_ID_EQUAL = "&mediaId=";
    public static final String UTF_8= "UTF-8";
    public static final String MEDIA_TYPE_EQUAL = "&mediaType=";
    public static final String WIDTH_100_HEIGHT_100 = "&width=100&height=100";
    public static final String WIDTH_EQUAL = "&width=";
    public static final String HEIGHT_EQUAL =  "&height=";
    public static final String EMPTY_STRING = "";

    /********ImagePreviewPlugin*/
    public static final String PATH = "path";
    public static final String LAYOUT = "layout";
    public static final String IMAGE_PREVIEW = "image_preview";
    public static final String ID = "id";
    public static final String STRING = "string";
    public static final String IMAGE_PREVIEW_IOPG = "image_preview_iopg";
    public static final String OK_BOPG = "ok_bopg";
    public static final String GEOFENCE_TRANSITION_ENTERED_KEY ="geofence_transition_entered";
    public static final String GEOFENCE_TRANSITION_EXITED_KEY ="geofence_transition_exited";
    public static final String GEOFENCE_TRANSITION_DWELL_KEY ="geofence_transition_dwell";
    public static final String UNKNOWN_GEOFENCE_TRANSITION_KEY = "unknown_geofence_transition";
    public static final String GEOFENCE_TRANSITION_INVALID_TYPE_KEY = "geofence_transition_invalid_type";
    public static final String MSG_SDK_LITE_KEY = "msg_sdk_lite";
    public static final String SDK_LITE_KEY = "sdk_lite";
    public static final String GEOFENCE_NOT_AVAILABLE_KEY = "geofence_not_available";
    public static final String GEOFENCE_TOO_MANY_GEOFENCES_KEY = "geofence_too_many_geofences";
    public static final String GEOFENCE_TOO_MANY_PENDING_INTENTS_KEY = "geofence_too_many_pending_intents";
    public static final String UNKNOWN_GEOFENCE_ERROR_KEY = "unknown_geofence_error";
    public static final String ERROR_FORGOT_PASSWORD_KEY = "error_forgot_password";
    public static final String GPS_ENABLE_GPS_ALERT_KEY = "gps_enable_gps_alert";

    /*OPGWebView*/
    public static final String STATUS_3 = "status=3";

    /*Utils*/
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String UTC = "UTC";
    public static final String T = "T";
    public static final String BLANK_SPACE = " ";

    /*FileUtils*/
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String MEDIA = "media";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String MIME_TYPE_AUDIO = "audio/*";
    public static final String MIME_TYPE_TEXT = "text/*";
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_VIDEO = "video/*";
    public static final String MIME_TYPE_APP = "application/*";
    public static final String COM_ANDROID_EXTERNAL_STORAGE_DOCUMENTS = "com.android.externalstorage.documents";
    public static final String COM_ANDROID_PROVIDERS_DOWNLOADS_DOCUMENTS = "com.android.providers.downloads.documents";
    public static final String com_android_providers_media_documents = "com.android.providers.media.documents";
    public static final String COM_GOOGLE_ANDROID_APPS_PHOTOS_CONTENT = "com.google.android.apps.photos.content";
    public static final String _DATA = "_data";
    public static final String PRIMARY = "primary";
    public static final String CONTENT_DOWNLOADS_PUBLIC_DOWNLOADS = "content://downloads/public_downloads";
    public static final String IMAGE_FILE_UTILS = "image";
    public static final String VIDEO_FILE_UTILS = "video";
    public static final String AUDIO_FILE_UTILS = "audio";
    public static final String _ID_EQUAL = "_id=?";
    public static final String CONTENT = "content";
    public static final String REGEX        = "###.#";
    public static final String KB        = " KB";
    public static final String MB     = " MB";
    public static final String GB = " GB";
    public static final String ERROR_ATTEMPTING_THUMBNAIL        = "Attempting to get thumbnail";
    public static final String ERROR_ATTEMPTING_THUMBNAIL_VIDEAO        = "You can only retrieve thumbnails for images and videos.";
    public static final String GOT_THUMB_ID     = "Got thumb ID: ";
    public static final String GET_THUMB_NAIL   = "getThumbnail";
    public static final String TYPE   = "*/*";


    /**********************MediaPickerAndPreviewPlugin*/

    public static final String OPG_IMAGE        = "/Opg_Image/";
    public static final String IMAGE_REGEX = "%%0%dd";
    public static final String IMAGE_CAPTURE_     = "image_capture_";
    public static final String JPEG = ".jpeg";
    public static final String SELECT_IMAGE        = "Select Image ";
    public static final String FILE_COLON     = "file://";
    public static final String FAILED_ERROR_PREVIEW   = "Failed : Error in Preview";
    public static final String SELECT_AUDIO        = "Select Audio ";
    public static final String OPG_AUDIO        = "/Opg_Audio/";
    public static final String AUDIO_REGEX     = "%%0%dd";
    public static final String AUDIO_CAPTURE_ = "audio_capture_";
    public static final String WAV        = ".wav";
    public static final String COMPLETED     = "COMPLETED";
    public static final String OPG_VIDEO       = "/Opg_Video/";
    public static final String VIDEO_REGEX     = "%%0%dd";
    public static final String MP4        = ".mp4";
    public static final String VIDEO_     = "video_";
    public static final String IMAGE_GALLERY_       = "image_gallery_";
    public static final String CANNOT_CREATE_DIR      = "Cannot create dir ";
    public static String VIDEO_CAPTURE = MediaStore.ACTION_VIDEO_CAPTURE;
    public static String IMAGE_CAPTURE = MediaStore.ACTION_IMAGE_CAPTURE;
    public static String ACTION_PICK = Intent.ACTION_PICK;
    public static String EXTRA_LOCAL_ONLY = Intent.EXTRA_LOCAL_ONLY;
    public static String ACTION_GET_CONTENT = Intent.ACTION_GET_CONTENT;
    public static String EXTRA_OUTPUT = MediaStore.EXTRA_OUTPUT;
    public static String ACTION_VIEW = Intent.ACTION_VIEW;
    public static String MEDIA_PATH_KEY = "path";
    public static String FILE_SEPARATOR = File.separator;
    public static final String  PROGRESS_DIALOG = "progress_dialog";


    /*********************Notification***************/
    public static final String BEEP = "beep";
    public static final String ALERT = "alert";
    public static final String CONFIRM = "confirm";
    public static final String PROMPT = "prompt";
    public static final String ACTIVITY_START = "activityStart";
    public static final String ACTIVITY_STOP = "activityStop";
    public static final String PROGRESS_START = "progressStart";
    public static final String PROGRESS_VALUE = "progressValue";
    public static final String PROGRESS_STOP = "progressStop";
    public static final String BUTTON_INDEX = "buttonIndex";
    public static final String INPUT_1 = "input1";

    /***** KEYS USED FOR APP NAME ****************/
    public static String OPEN_BRACE1_KEY = "\\{";
    public static String CLOSE_BRACE1_KEY = "\\}";
    public static String OPEN_BRACE2_KEY = "\\[";
    public static String CLOSE_BRACE2_KEY = "\\]";
    public static String OPEN_BRACE3_KEY = "\\(";
    public static String CLOSE_BRACE3_KEY = "\\)";
    public static String AMP_KEY ="\\&";
    public static String CAP_KEY ="\\^";
    public static String QUE_KEY ="\\?";
    public static String PIPE_KEY ="\\|";
    public static String PLUS_KEY ="\\+";
    public static String ASTERIC_KEY ="\\*";
    public static String DOT_KEY_KEY ="\\.";
    public static String BACKSLASH_KEY ="\\\\";

    /***Runtime Permissions *****/

    public static String APP_DETAILS_SETTINGS = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
    public static String READ_EXTRENAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static String WRITE_EXTRENAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static String ACCESS_FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static String ACCESS_COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    public static String AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO;
    public static String SETTINGS = "app_settings";
    public static String DENY = "deny";
    public static String RUNTIME_PERMISSION= "runtime_permission";
    public static String ALLOW = "allow";
    public static String ACCESS_PERMISSION_MSG= "access_permission_msg";
    public static String ACTIVATE_STORAGE_MSG = "activate_storage_msg";
    public static String STORAGE_PERMISSION_MSG= "storage_permission_msg";
    public static String LOCATION_PERMISSION_MSG= "storage_permission_msg";
    public static String CAMERA_PERMISSION_MSG= "camera_permission_msg";
    public static String AUDIO_PERMISSION_MSG= "audio_permission_msg";
    public static String RECORD_PERMISSION_MSG= "record_permission_msg";
    public static String PACKAGE = "package";

    public static final String CONNECTIVITY_SERVICE_STR = Context.CONNECTIVITY_SERVICE;

    /**GETDEVICEORITNTATIONPLUGIN**/
    public static String DEVICE_STATE_KEY = "device_state";
}

