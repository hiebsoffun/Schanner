package de.haidozo.sudoku;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

/**
 * Created by HieblMi on 09.07.2015.
 */
public class SudokuBoardRecognition {
    private final String imagePath;

    // Native methods
    private native String extract_board_from_image(long imgAddr);

    public SudokuBoardRecognition(String imagePath) {
        this.imagePath = imagePath;
    }

    public void recognize() {
        Mat image = Highgui.imread(imagePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);

        Log.d(MainActivity.TAG, image.channels() + "");
        if(! image.empty()) {
            Log.d(MainActivity.TAG, "Image loaded by OpenCV!");
            Log.d(MainActivity.TAG, extract_board_from_image(image.nativeObj));
        } else {
            Log.d(MainActivity.TAG, "Highgui.imread() failed.");
        }
    }
}
