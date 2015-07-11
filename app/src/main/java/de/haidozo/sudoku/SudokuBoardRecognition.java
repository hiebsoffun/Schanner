package de.haidozo.sudoku;

import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

/**
 * Created by HieblMi on 09.07.2015.
 */
public class SudokuBoardRecognition {

    // Native methods
    private native String extract(long imgAddr);

    public SudokuBoardRecognition() {

        if (!OpenCVLoader.initDebug()) {
            Log.e(MainActivity.TAG, "OpenCVLoader.initDebug() failed.");
        } else {
            Log.d(MainActivity.TAG, "OpenCV library found inside package. Using it!");
            // Load native library after(!) OpenCV initialization
            System.loadLibrary("sudoku_board_recognition");
        }
    }

    public void recognize(String imagePath) {

        Mat image = Highgui.imread(imagePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);

        Log.d(MainActivity.TAG, image.channels() + "");
        if(! image.empty()) {
            Log.d(MainActivity.TAG, "Image loaded by OpenCV!");
            Log.d(MainActivity.TAG, extract(image.nativeObj));
        } else {
            Log.d(MainActivity.TAG, "Highgui.imread() failed.");
        }
    }
}
