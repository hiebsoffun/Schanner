package de.haidozo.sudoku;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.Bind;
import de.haidozo.sudoku.R;


public class MainActivity extends Activity {

    private static final String     TAG                 = "Sudoku::MainActivity";
    private String                  APP_NAME            = null;
    private static final int        REQUEST_CODE        = 100;
    public static String            outputFile;
    @Bind(R.id.scan_button) Button button;

    // Native methods
    private native String AnalyzePicture(long imgAddr);

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APP_NAME = getString(R.string.app_name);
        ButterKnife.bind(this);
    }

    public void selectImage(View view) {
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

    private void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCVLoader.initDebug() failed.");
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            // Load native library after(!) OpenCV initialization
            System.loadLibrary("sudoku");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
