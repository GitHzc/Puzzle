package com.example.puzzle.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.example.puzzle.R;

/**
 * Created by Administrator on 2018/7/3 0003.
 */

public class ChooseDifficultyFragment extends AppCompatDialogFragment {
    private ChooseDifficultyFragmentListener mChooseDifficultyFragmentListener;
    private int index;

    public interface ChooseDifficultyFragmentListener {
        void onChooseDifficulty(int index);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChooseDifficultyFragmentListener.onChooseDifficulty(index);
                    }
                })
                .setNegativeButton(R.string.negative, null)
                .setSingleChoiceItems(R.array.difficulty, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        index = which;
                    }
                })
                .create();
    }

    public void setChooseDifficultyFragmentListener(ChooseDifficultyFragmentListener chooseDifficultyFragmentListener) {
        mChooseDifficultyFragmentListener = chooseDifficultyFragmentListener;
    }
}
