package de.haidozo.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity {

    // Constants
    public static final String      TAG                                 = "Sudoku::MainActivity";
    public static String            APP_NAME                            = null;

    public static final int         REQUEST_IMAGE_FROM_CAM              = 100;
    public static final int         REQUEST_IMAGE_FROM_GALLERY          = 101;


    // Utilities
    private ImageProvider           imageProvider                       = null;


    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize utilities
        imageProvider = new ImageProvider(this);

        APP_NAME = getString(R.string.app_name);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.scan_button)
    public void fetchImageFromCamera(View view) {
        imageProvider.getImageFromCamera();
    }

    @OnClick(R.id.gallery_button)
    public void fetchImageFromGallery(View view) {
        imageProvider.getImageFromGallery();
    }

    private void importBoardFromImage(String imagePath) {
        if(! imagePath.isEmpty()) {
            new SudokuBoardRecognition().recognize(imagePath);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_FROM_CAM) {
                Log.d(TAG, imageProvider.getPathOfCameraImage());
                importBoardFromImage(imageProvider.getPathOfCameraImage());
            } else if (requestCode == REQUEST_IMAGE_FROM_GALLERY) {
                importBoardFromImage(imageProvider
                        .getPathOfGalleryImageFromUri(getApplicationContext(), data.getData()));
            }
        }
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
