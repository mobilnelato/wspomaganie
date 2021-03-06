package pl.edu.agh.mobilne_2017.activ.categoryActiv;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import pl.edu.agh.mobilne_2017.MainActivity;
import pl.edu.agh.mobilne_2017.activ.CategoryMenu;
import pl.edu.agh.mobilne_2017.model.ClosedQuestion;
import pl.edu.agh.mobilne_2017.db.DatabaseHelper;
import pl.edu.agh.mobilne_2017.model.OpenQuestion;
import pl.edu.agh.mobilne_2017.model.Question;
import pl.edu.agh.mobilne_2017.R;

public class AddQuestionActivity extends Activity {

    private EditText stringAnswer;

    private static int ANSWS = 4;
    private CheckBox[] checkBoxes = new CheckBox[ANSWS];

    private EditText[] anws = new EditText[ANSWS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_question);


        ToggleButton toggle = (ToggleButton) findViewById(R.id.closedopen);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToggleButton toggle = (ToggleButton) findViewById(R.id.closedopen);
                cleanLayout();
                if (isChecked) {
                    stringAnswer = new EditText(getBaseContext());
                    stringAnswer.setHint("Expected answer");
                    stringAnswer.setEms(20);
                    addToLayout(stringAnswer, toggle.getId(), RelativeLayout.BELOW);
                } else {
                    // The toggle is disabled closed question
                    int prev = toggle.getId();
                    for (int i = 0; i < ANSWS; i++) {
                        CheckBox ch1 = new CheckBox(getBaseContext());
                        checkBoxes[i] = ch1;
                        prev = addToLayout(ch1, prev, RelativeLayout.BELOW);
                        EditText ans1 = new EditText(getBaseContext());
                        ans1.setEms(20);
                        ans1.setHint("Answer "+(i+1));
                        prev = addToLayout(ans1, prev, RelativeLayout.BELOW);
                        anws[i]= ans1;
                    }
                }
            }
        });

        toggle.setChecked(true);
        Button saveButton = (Button) findViewById(R.id.savequestion);
        String category = this.getIntent().getExtras().getString("category");
        saveButton.setOnClickListener(new NewQuestionListener(category));

        Button backButton = (Button) findViewById(R.id.new_question_back);
        backButton.setOnClickListener(new BackInNewQuestionListener(category));

    }

    private void cleanLayout() {
        if (stringAnswer != null) {
            removeFromLaoyt(stringAnswer);
            stringAnswer = null;
        }
        for (int i = 0; i < ANSWS; i++) {
            removeFromLaoyt(checkBoxes[i]);
            removeFromLaoyt(anws[i]);
            anws[i] = null;
            checkBoxes[i] = null;
        }
    }

    private int addToLayout(View v, int prev, int rightOrBelow) {//dodac argument below czy po prawej od ostatniego elementu
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.new_question_id);
        int curr = View.generateViewId();
        v.setId(curr);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(rightOrBelow, prev);
        mainLayout.addView(v, params);
        return curr;
    }


    private void removeFromLaoyt(View v) {
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.new_question_id);
        mainLayout.removeView(v);
    }


    private class NewQuestionListener implements View.OnClickListener {

        private final String category;

        public NewQuestionListener(String category) {
            this.category = category;
        }

        public void onClick(View arg0) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            EditText tv = (EditText) findViewById(R.id.new_question_content);
            Log.w("NewQuestionListener", tv.getText().toString());
            Question question;
            ToggleButton toggle = (ToggleButton) findViewById(R.id.closedopen);
            String content = ((EditText) findViewById(R.id.new_question_content)).getText().toString();

            if (toggle.isChecked()) {
                //open question
                Log.w("NewQuestionListener","odpowiedz w otwartym"+stringAnswer.getText().toString());
                question = new OpenQuestion(content, stringAnswer.getText().toString(), -1);
            } else {
                boolean[] checkboxes = new boolean[4];
                for (int i = 0; i < checkBoxes.length; i++) {
                    checkboxes[i] = checkBoxes[i].isChecked();
                }
                String[] sAnsws = new String[4];
                for (int i = 0; i < anws.length; i++) {
                    sAnsws[i] = anws[i].getText().toString();
                }
                question = new ClosedQuestion(content,checkboxes,sAnsws,-1);
            }
            db.createQuestion(question,category);
            toCategoryMenu(category);
        }
    }


    private class BackInNewQuestionListener implements View.OnClickListener {
        private final String category;

        public BackInNewQuestionListener(String category) {
            this.category = category;
        }

        public void onClick(View arg0) {
            toCategoryMenu(category);
        }
    }

    private void toCategoryMenu(String category) {
        Intent mainActivity = new Intent(getBaseContext(), CategoryMenu.class);
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        mainActivity.putExtras(bundle);
        startActivity(mainActivity);
    }
}




