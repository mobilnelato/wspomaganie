package pl.edu.agh.mobilne_2017.activ.categoryActiv;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import pl.edu.agh.mobilne_2017.activ.CategoryMenu;
import pl.edu.agh.mobilne_2017.model.ClosedQuestion;
import pl.edu.agh.mobilne_2017.db.DatabaseHelper;
import pl.edu.agh.mobilne_2017.model.OpenQuestion;
import pl.edu.agh.mobilne_2017.model.Question;
import pl.edu.agh.mobilne_2017.model.QuestionType;
import pl.edu.agh.mobilne_2017.R;

public class EditQuestionActivity extends Activity {
    private static int ANSWS = 4;
    private CheckBox[] checkBoxes = new CheckBox[ANSWS];
    private EditText stringAnswer;
    private EditText[] anws = new EditText[ANSWS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_editor);

        EditText tv = (EditText) findViewById(R.id.editor_content);

        int prev = tv.getId();
        long questionId = this.getIntent().getExtras().getLong("questionId");
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Question question = db.getQuestion(questionId);
        tv.setText(question.getContent());
        Log.w("EditQuestionActivity", "id pytania" + questionId);
        Log.w("EditQuestionActivity", "czy jest rowne null" + (null == question));
        if (question.getType() == QuestionType.CLOSED) {
            //dodajemy 4 pola z tymi pytaniami
            ClosedQuestion closedQuestion = (ClosedQuestion) question;

            for (int i = 0; i < ANSWS; i++) {
                CheckBox ch1 = new CheckBox(getBaseContext());
                ch1.setPadding(50, 50, 10, 0);
                ch1.setChecked(closedQuestion.getCheckboxes()[i]);
                Log.w("EditQuestionActivity", "czy jest true" + closedQuestion.getCheckboxes()[i]);
                prev = addToLayout(ch1, prev, RelativeLayout.BELOW);
                checkBoxes[i] = ch1;

                EditText ans1 = new EditText(getBaseContext());
                ans1.setEms(20);
                ans1.setHint("Answer " + (i + 1));
                ans1.setText(closedQuestion.getAnsws()[i]);
                Log.w("EditQuestionActivity", "odpowiedz" + closedQuestion.getAnsws()[i]);
                anws[i] = ans1;
                RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.question_editor_layout);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.RIGHT_OF, prev);
                params.addRule(RelativeLayout.ALIGN_BASELINE, prev);
                mainLayout.addView(ans1, params);


            }

        } else if (question.getType() == QuestionType.OPEN) {
            OpenQuestion openQuestion = (OpenQuestion) question;
            //dodjamey textarea z contentem
            stringAnswer = new EditText(getBaseContext());
            stringAnswer.setEms(20);
            stringAnswer.setText(openQuestion.getStringAnswer());
            addToLayout(stringAnswer, prev, RelativeLayout.BELOW);
        }
        //dodaj listener do save buttona
        String category = this.getIntent().getExtras().getString("category");
        findViewById(R.id.editor_save_question).setOnClickListener(new SaveEditQuestionListener(question.getType(), question.getId(), category));
        findViewById(R.id.editor_back).setOnClickListener(new BackEditQuestionListener(category));
    }

    private class BackEditQuestionListener implements View.OnClickListener {
        private final String category;

        public BackEditQuestionListener(String category) {
            this.category = category;
        }

        public void onClick(View arg0) {
            toCategoryMenu(category);
        }
    }


    private class SaveEditQuestionListener implements View.OnClickListener {
        private final QuestionType type;
        private final long questionId;
        private final String category;

        public SaveEditQuestionListener(QuestionType type, long questionId, String category) {
            this.type = type;
            this.questionId = questionId;
            this.category = category;
        }

        public void onClick(View arg0) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            Log.w("SaveEditQuestionListene", "Saving edit changes on quesiton ");
            Question question = null;
            String content = ((EditText) findViewById(R.id.editor_content)).getText().toString();
            if (type == QuestionType.CLOSED) {
                //dodajemy 4 pola z tymi pytaniami

                boolean[] checkboxes = new boolean[4];
                for (int i = 0; i < checkBoxes.length; i++) {
                    checkboxes[i] = checkBoxes[i].isChecked();
                }
                String[] sAnsws = new String[4];
                for (int i = 0; i < anws.length; i++) {
                    sAnsws[i] = anws[i].getText().toString();
                }
                question = new ClosedQuestion(content, checkboxes, sAnsws, questionId);
            } else if (type == QuestionType.OPEN) {
                //dodjamey textarea z contentem
                question = new OpenQuestion(content, stringAnswer.getText().toString(), questionId);

            }
            db.updateQuestion(question, category);
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

    private int addToLayout(View v, int prev, int rightOrBelow) {//dodac argument below czy po prawej od ostatniego elementu
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.question_editor_layout);
        int curr = View.generateViewId();
        v.setId(curr);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(rightOrBelow, prev);
        mainLayout.addView(v, params);
        return curr;
    }
}
