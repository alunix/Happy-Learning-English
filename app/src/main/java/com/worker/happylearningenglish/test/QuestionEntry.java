package com.worker.happylearningenglish.test;

import android.database.Cursor;
import android.view.View;
import android.widget.RadioGroup;

import com.worker.happylearningenglish.R;
import com.worker.happylearningenglish.activities.MainActivity;

/**
 * Created by hataketsu on 5/9/16.
 */
public class QuestionEntry {
    private static final int[] IDS = {R.id.answer_a, R.id.answer_b, R.id.answer_c, R.id.answer_d};
    private final String content;
    private final String[] answers = new String[4];
    private final int correctAnswer;
    private View view;

    public QuestionEntry(Cursor cursor) {
        this.content = cursor.getString(0);
        for (int i = 0; i < answers.length; i++) {
            this.answers[i] = cursor.getString(i + 1);
        }
        correctAnswer = Character.toLowerCase(cursor.getString(5).charAt(0)) - 'a';
    }

    public String getAnswer(int no) {
        return answers[no];
    }

    public String getContent() {
        return content;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public Result check() {
        RadioGroup group = (RadioGroup) view.findViewById(R.id.question_group);
        int id = group.getCheckedRadioButtonId();
        if (id == -1) {
            return Result.SKIP;
        }
        view.findViewById(IDS[getCorrectAnswer()]).setBackgroundColor(MainActivity.getContext().getResources().getColor(R.color.green));
        for (int i = 0; i < IDS.length; i++) {
            if (IDS[i] == id) {
                if (i == getCorrectAnswer())
                    return Result.CORRECT;
                else {
                    view.findViewById(IDS[getCorrectAnswer()]).setBackgroundColor(MainActivity.getContext().getResources().getColor(R.color.red));
                    return Result.MISTAKE;
                }
            }
        }
        return Result.SKIP;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public enum Result {
        CORRECT, MISTAKE, SKIP
    }
}
