package pl.edu.agh.mobilne_2017.activ.categoryActiv;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        TextView tv = (TextView) findViewById(R.id.editorHeader);
        int prev = tv.getId();
        int questionId = this.getIntent().getExtras().getInt("questionId");
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Question question = db.getQuestion(questionId);
        if (question.getType() == QuestionType.CLOSED) {
            //dodajemy 4 pola z tymi pytaniami
            ClosedQuestion closedQuestion = (ClosedQuestion) question;

            for (int i = 0; i < ANSWS; i++) {
                CheckBox ch1 = new CheckBox(getBaseContext());
                ch1.setChecked(closedQuestion.getCheckboxes()[i]);
                prev = addToLayout(ch1, prev, RelativeLayout.BELOW);
                checkBoxes[i] = ch1;

                EditText ans1 = new EditText(getBaseContext());
                ans1.setText(closedQuestion.getAnsws()[i]);
                prev = addToLayout(ans1, prev, RelativeLayout.RIGHT_OF);
                anws[i] = ans1;
            }

        } else if (question.getType() == QuestionType.OPEN) {
            //dodjamey textarea z contentem
            stringAnswer = new EditText(getBaseContext());
            stringAnswer.setText(question.getContent());
            addToLayout(stringAnswer, prev, RelativeLayout.BELOW);
        }
        //dodaj listener do save buttona
        String category = this.getIntent().getExtras().getString("category");
        findViewById(R.id.editor_save_question).setOnClickListener(new SaveEditQuestionListener(question.getType(), question.getId(),category));

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
            String content = ((EditText) findViewById(R.id.new_question_content)).getText().toString();
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
            db.updateQuestion(question,category);
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
}
