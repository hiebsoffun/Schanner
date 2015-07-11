package de.haidozo.sudoku;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HieblMi on 09.07.2015.
 */
public class ImageProvider {

    private MainActivity                mainActivity;
    public static String                outputFile;

    public ImageProvider(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void getImageFromCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri outputFileUri = createOutputImageFileUri(); // create a file to save the image
        int lastSlashIndex = outputFileUri.toString().lastIndexOf('/');
        outputFile = outputFileUri.toString().substring(lastSlashIndex+1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        // start the image capture Intent
        mainActivity.startActivityForResult(intent, MainActivity.REQUEST_IMAGE_FROM_CAM);
    }

    public void getImageFromGallery() {

        // in onCreate or any event where your want the user to
        // select a file
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        mainActivity.startActivityForResult(Intent.createChooser(intent, "Select Sudoku Image"),
                                            MainActivity.REQUEST_IMAGE_FROM_GALLERY);
    }

    public String getPathOfCameraImage() {
        String sdcard = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        String imagePath = sdcard + File.separator + MainActivity.APP_NAME + File.separator + outputFile;
        return imagePath;
    }

    public String getPathOfGalleryImageFromUri(Context applicationContext, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = applicationContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        Log.d(MainActivity.TAG,filePath);
        return filePath;
    }

    //check if SD card is mounted. If so create unique file
    private Uri createOutputImageFileUri() {
        String state = Environment.getExternalStorageState();
        File mediaFile = null;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), MainActivity.APP_NAME);

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d(MainActivity.TAG, "failed to create directory");
                    return null;
                }
            }

            // create media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String file = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png";
            mediaFile = new File(file);
        } else {
            Log.d(MainActivity.TAG, "SD Card not mounted");
            return null;
        }

        return Uri.fromFile(mediaFile);
    }
}
