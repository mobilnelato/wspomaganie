package pl.edu.agh.mobilne_2017.activ.categoryActiv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.mobilne_2017.DatabaseHelper;
import pl.edu.agh.mobilne_2017.model.Answer;
import pl.edu.agh.mobilne_2017.model.ClosedQuestion;
import pl.edu.agh.mobilne_2017.model.Question;
import pl.edu.agh.mobilne_2017.model.QuestionType;
import pl.edu.agh.mobilne_2017.R;


public class TakeQuizActivity extends Activity {

    private CheckBox[] checkBoxes = new CheckBox[4];
    private TextView[] answerTexts = new TextView[4];
    private EditText stringAnswer;
    Answer[] answers;
    int currentQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_category_layout);

        String category = this.getIntent().getExtras().getString("categoryId");
        int size = this.getIntent().getExtras().getInt("quizSize");
        answers = new Answer[size];

        //instancojnuj bhelpera, i wylosuj tyle pytaz z tej kategorii
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Question> questions = db.getRandomQuestions(category, size);
        //ustaw pierwsze pytanie
        setQuestionInLayout(questions.get(0));
        //prev rob disable jak counter jest na zero
        (findViewById(R.id.quiz_taking_prev_button)).setEnabled(false);


        //dodaj listener do prev i next


        //jak next dojdzie do konca to sprawdz wyniki, zmien lauout i tma podaj punkty  -- na osobnym layouce
        findViewById(R.id.quiz_taking_prev_button).setOnClickListener(new PreviousQuestionListener());
        findViewById(R.id.quiz_taking_next_button).setOnClickListener(new NextQuestionListener());
        //odpowiedziami bedzi list<Answers>
    }


    private void setQuestionInLayout(Question q) {
        cleanLayout();
        //ustaw text pytnia
        TextView tv = (TextView) findViewById(R.id.quiz_taking_content);
        tv.setText(q.getContent());
        int prev = findViewById(R.id.quiz_taking_content).getId();
        if (q.getType() == QuestionType.CLOSED) {
            ClosedQuestion closed = (ClosedQuestion) q;
            //dodac 4 razy checkbox i tekst
            for (int i = 0; i < 4; i++) {
                CheckBox checkBox = new CheckBox(getBaseContext());
                prev = addToLayout(checkBox, prev, RelativeLayout.BELOW);
                checkBoxes[i] = checkBox;

                answerTexts[i] = new TextView(getBaseContext());
                answerTexts[i].setText(closed.getAnsws()[i]);
                prev = addToLayout(answerTexts[i], prev, RelativeLayout.RIGHT_OF);
            }
        } else if (q.getType() == QuestionType.OPEN) {
            stringAnswer = new EditText(getBaseContext());
            addToLayout(stringAnswer, prev, RelativeLayout.BELOW);
        }
    }

    private class PreviousQuestionListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    private class NextQuestionListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }


    //te listenery
    //sprawdzanie wyniku i wyswitlanie w osobnym layoucie n
    //selecty w helperze

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


    private void cleanLayout() {
        if (stringAnswer != null) {
            removeFromLaoyt(stringAnswer);
            stringAnswer = null;
        }
        for (int i = 0; i < 4; i++) {
            removeFromLaoyt(checkBoxes[i]);
            removeFromLaoyt(answerTexts[i]);
            answerTexts[i] = null;
            checkBoxes[i] = null;
        }
    }

    private void removeFromLaoyt(View v) {
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.quiz_taking_layout);
        mainLayout.removeView(v);
    }
}
