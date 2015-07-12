package de.haidozo.sudoku.game;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.haidozo.sudoku.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment {

    private BoardView           boardView                   = null;
    private Game                game                        = null;
    protected static final int  DIFFICULTY_CONTINUE         = -1;

    public static final String  KEY_DIFFICULTY              = "com.apps.sudoku.difficulty";

    private static final String PREF_PUZZLE                 = "puzzle";

    public static final int     DIFFICULTY_EASY             = 0;
    public static final int     DIFFICULTY_MEDIUM           = 1;
    public static final int     DIFFICULTY_HARD             = 2;

    private final String        puzzle                      =   "360000000004230800000004200" +
                                                                "070460003820000014500013020" +
                                                                "001900000007048300000000045";

    public BoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(((MainActivity)getActivity()).TAG, "onCreate BoardFragment");

        int diff = savedInstanceState.getInt(KEY_DIFFICULTY, DIFFICULTY_EASY);

        game = new Game(puzzle);
        boardView.setGame(game);

        // if the activity restarts, do a continue next time
        savedInstanceState.putInt(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        boardView = new BoardView(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity activity = ((MainActivity)getActivity());
        Log.d(activity.TAG, "onPause BoardFragment");

        activity.getPreferences(activity.MODE_PRIVATE)
                .edit()
                .putString(PREF_PUZZLE, puzzle)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(de.haidozo.sudoku.game.BoardView, container, false);
        return boardView;
    }


}
