package com.example.administrator.yidiankuang.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author heronghua
 * <p>
 * Email : heronghua1989@126.com
 * <p>
 * Create date 19-3-2  上午12:16
 * <p>
 * Description:
 */
public class PictureChoiceCropUtils {

    public static final String TAG = PictureChoiceCropUtils.class.getSimpleName();

    private static final int CHOICE_FROM_ALBUM_REQUEST_CODE = 0;
    private static final int CROP_PHOTO_REQUEST_CODE = 1;

    private PictureChoiceCropUtils() {
        throw new AssertionError();
    }

    private static List<String> getSDCardPaths(Activity activity) {
        StorageManager storageManager = (StorageManager) activity.getApplication()
                .getSystemService(Context.STORAGE_SERVICE);
        List<String> paths = new ArrayList<>();
        try {
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
            getVolumePathsMethod.setAccessible(true);
            Object invoke = getVolumePathsMethod.invoke(storageManager);
            paths = Arrays.asList((String[]) invoke);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }

    public static <T extends Activity> void sendIntentToAlbum(T context) {
        Intent choiceFromAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        choiceFromAlbumIntent.addCategory(Intent.CATEGORY_OPENABLE);
        choiceFromAlbumIntent.setType("image/*");
        context.startActivityForResult(choiceFromAlbumIntent, CHOICE_FROM_ALBUM_REQUEST_CODE);
    }

    private static void cropPhoto(Activity activity, Uri inputUri) {
        Log.d(TAG + ".cropPhoto=inputUri", inputUri + "$");
        // 调用系统裁剪图片的 Action
        Intent cropPhotoIntent = new Intent("com.android.camera.action.CROP");
        // 设置数据类型
        cropPhotoIntent.setDataAndType(inputUri, "image/*");
        cropPhotoIntent.putExtra("aspectX", 1);
        cropPhotoIntent.putExtra("aspectY", 1);
        cropPhotoIntent.putExtra("outputX", 150);
        cropPhotoIntent.putExtra("outputY", 150);
        // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //cropPhotoIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //设置图片的最终输出目录
        Uri imageUri = getImageUri();
        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Log.d(TAG + ".cropPhoto", imageUri.getPath() + "$");
        if (!getSDCardPaths(activity).isEmpty()) {
            activity.startActivityForResult(cropPhotoIntent, CROP_PHOTO_REQUEST_CODE);
        } else {
            Toast.makeText(activity,"请先挂载手机卡",Toast.LENGTH_SHORT).show();
        }
    }

    private static Uri getImageUri() {
        String finalFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pic.jpg";
        File finalFile = new File(finalFilePath);
        return Uri.fromFile(finalFile);
    }


    public static <T extends Activity> void handleOnActivityResult(T context, int requestCode, int resultCode, @Nullable Intent data,OnPictureCropResultListner listner) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureChoiceCropUtils.CHOICE_FROM_ALBUM_REQUEST_CODE:
                    cropPhoto(context, data.getData());
                    break;

                case CROP_PHOTO_REQUEST_CODE:
                    try {
                        File file = new File(getPath(context, getImageUri()));
                        if (file.exists()) {

                            listner.onCropSuccess(file);

                        } else {

                            Toast.makeText(context,"图片未找到！",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
        }

    }

    public interface OnPictureCropResultListner{
        void onCropSuccess(File file);
    }


    /**
     * =======================================GetImagePath.java=====================================================================================
     */


    // 4.4以上 content://com.android.providers.media.documents/document/image:3952
    // 4.4以下 content://media/external/images/media/3951
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)){
                return uri.getLastPathSegment();

            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    // Android 4.4以下版本自动使用该方法
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }

        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    /**=======================================GetImagePath.java=====================================================================================*/


}
