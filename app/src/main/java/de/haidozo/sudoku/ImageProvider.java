package de.haidozo.sudoku;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HieblMi on 09.07.2015.
 */
public class ImageProvider {

    private String imageFromCamera;

    public String getImageFromCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri outputFileUri = createOutputImageFileUri(); // create a file to save the image
        int lastSlashIndex = outputFileUri.toString().lastIndexOf('/');
        outputFile = outputFileUri.toString().substring(lastSlashIndex+1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        // start the image capture Intent
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to outputFile

                // read the image
                String sdcard = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString();
                String filePath = sdcard + File.separator + APP_NAME + File.separator + outputFile;
                Mat image = Highgui.imread(filePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);

                Log.d(TAG, image.channels() + "");
                if(! image.empty()) {
                    showToast("Image loaded by OpenCV!");
                    showToast(AnalyzePicture(image.nativeObj));
                } else {
                    showToast("Highgui.imread() failed.");
                }
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                showToast("Image capture failed");
            }
        }
    }

    //check if SD card is mounted. If so create unique file
    private Uri createOutputImageFileUri() {
        String state = Environment.getExternalStorageState();
        File mediaFile = null;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), APP_NAME);

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }

            // create media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String file = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png";
            mediaFile = new File(file);
        } else {
            Log.d(TAG, "SD Card not mounted");
            showToast("SD Card not mounted");
            return null;
        }

        return Uri.fromFile(mediaFile);
    }
}
