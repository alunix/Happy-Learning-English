package com.worker.happylearningenglish.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.worker.happylearningenglish.R;
import com.worker.happylearningenglish.test.QuestionEntry;
import com.worker.happylearningenglish.test.SubLevelEntry;
import com.worker.happylearningenglish.test.TestDatabase;

/**
 * Created by hataketsu on 5/6/16.
 */
public class TestFragment extends Fragment implements OnBackPressedListener, View.OnClickListener {
    private View rootView;
    private View levelScreen;
    private View sublevelScreen;
    private SCREEN_MODE current;
    private View testQuestionsScreen;
    private View resultScreen;
    private boolean addedFooter = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.test_fragment, container, false);
            levelScreen = rootView.findViewById(R.id.lv_screen);
            sublevelScreen = rootView.findViewById(R.id.sublv_screen);
            testQuestionsScreen = rootView.findViewById(R.id.test_main_screen);
            resultScreen = rootView.findViewById(R.id.test_result_screen);
            switchScreen(SCREEN_MODE.LEVEL);
        }
        return rootView;
    }

    @Override
    public boolean onBackPressed() {
        if (current == SCREEN_MODE.LEVEL)
            return false;
        else if (current == SCREEN_MODE.SUBLEVEL) {
            switchScreen(SCREEN_MODE.LEVEL);
            return true;
        } else if (current == SCREEN_MODE.TEST) {
            switchScreen(SCREEN_MODE.SUBLEVEL);
            return true;
        } else if (current == SCREEN_MODE.RESULT) {
            switchScreen(SCREEN_MODE.TEST);
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

    }

    private void switchToResult() {
        ListView testListview = (ListView) testQuestionsScreen.findViewById(R.id.test_questions_lv);
        ListAdapter adapter = testListview.getAdapter();
        int total = 0, correct = 0, skip = 0, mistake = 0;
        for (int i = 0; i < adapter.getCount() - 1; i++) {
            total++;
            QuestionEntry entry = (QuestionEntry) adapter.getItem(i);
            QuestionEntry.Result result = entry.check();
            if (result == QuestionEntry.Result.SKIP) skip++;
            else if (result == QuestionEntry.Result.CORRECT) correct++;
        }
        mistake = total - correct;
        switchScreen(SCREEN_MODE.RESULT);
        int percent = correct * 100 / total;
        ((TextView) resultScreen.findViewById(R.id.result_percent_tv)).setText(String.valueOf(percent) + "%");
        ((TextView) resultScreen.findViewById(R.id.result_comment_tv)).setText(getComment(percent));
        ((TextView) resultScreen.findViewById(R.id.result_total_tv)).setText(String.valueOf(total));
        ((TextView) resultScreen.findViewById(R.id.result_correct_tv)).setText(String.valueOf(correct));
        ((TextView) resultScreen.findViewById(R.id.result_mistake)).setText(String.valueOf(mistake));
        ((TextView) resultScreen.findViewById(R.id.result_skip_tv)).setText(String.valueOf(skip));
    }

    private String getComment(int percent) {
        if (percent >= 80)
            return "Rất tốt";
        else if (percent >= 60)
            return "Không tệ";
        else
            return "Quá tệ";
    }

    private void switchToLevel(int i) {
        switchScreen(SCREEN_MODE.SUBLEVEL);
        ListView subLevelListview = (ListView) sublevelScreen.findViewById(R.id.test_sublv_lv);
        subLevelListview.setAdapter(TestDatabase.getSubLevelAdapter(i));
        subLevelListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SubLevelEntry entry = (SubLevelEntry) adapterView.getItemAtPosition(position);
                Toast.makeText(getContext(), entry.getDescription(), Toast.LENGTH_SHORT).show();
                switchToTest(entry);
            }
        });
    }

    private void switchToTest(SubLevelEntry entry) {
        switchScreen(SCREEN_MODE.TEST);
        ListView testListview = (ListView) testQuestionsScreen.findViewById(R.id.test_questions_lv);
        testListview.setAdapter(TestDatabase.getQuestions(entry));
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (addedFooter) return;
        testListview.addFooterView(layoutInflater.inflate(R.layout.check_button, null));
        addedFooter = true;
    }

    private void switchScreen(SCREEN_MODE mode) {
        current = mode;
        switch (mode) {
            case LEVEL:
                levelScreen.setVisibility(View.VISIBLE);
                sublevelScreen.setVisibility(View.GONE);
                break;

        }
    }

    enum SCREEN_MODE {
        LEVEL, SUBLEVEL, TEST, RESULT;
    }
}
