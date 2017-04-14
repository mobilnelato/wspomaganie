package pl.edu.agh.mobilne_2017.activ.categoryActiv;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.mobilne_2017.db.DatabaseHelper;
import pl.edu.agh.mobilne_2017.model.Answer;
import pl.edu.agh.mobilne_2017.model.ClosedAnswer;
import pl.edu.agh.mobilne_2017.model.ClosedQuestion;
import pl.edu.agh.mobilne_2017.model.OpenAnswer;
import pl.edu.agh.mobilne_2017.model.OpenQuestion;
import pl.edu.agh.mobilne_2017.model.Question;
import pl.edu.agh.mobilne_2017.model.QuestionType;
import pl.edu.agh.mobilne_2017.R;


public class TakeQuizActivity extends Activity {

    private CheckBox[] checkBoxes = new CheckBox[4];
    private TextView[] answerTexts = new TextView[4];
    private EditText currOpenQuestionAns;
    Answer[] answers;
    int currentQuestion = 0;
    int quizSize;
    List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_category_layout);

        String category = this.getIntent().getExtras().getString("categoryId");
        quizSize = this.getIntent().getExtras().getInt("quizSize");
        answers = new Answer[quizSize];

        //instancojnuj bhelpera, i wylosuj tyle pytaz z tej kategorii
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        questions = db.getRandomQuestions(category, quizSize);
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
            currOpenQuestionAns = new EditText(getBaseContext());
            addToLayout(currOpenQuestionAns, prev, RelativeLayout.BELOW);
        }
    }

    private class PreviousQuestionListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //zapisz pytanie
            saveAnswer();
            currentQuestion--;
            updateCounterLabel();
            if (currentQuestion == 0) {
                v.setEnabled(false);
            }

        }
    }

    private void saveAnswer() {
        Question q = questions.get(currentQuestion);
        if (q.getType() == QuestionType.CLOSED) {
            boolean[] checkAnsw = new boolean[checkBoxes.length];
            for (int i = 0; i < checkBoxes.length; i++) {
                checkAnsw[i] = checkBoxes[i].isChecked();
            }
            answers[currentQuestion] = new ClosedAnswer(checkAnsw);
        } else if (q.getType() == QuestionType.OPEN) {
            answers[currentQuestion] = new OpenAnswer(currOpenQuestionAns.getText().toString());
        }
    }

    private class NextQuestionListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            saveAnswer();
            currentQuestion++;
            updateCounterLabel();
            if (currentQuestion == 1) {
                findViewById(R.id.quiz_taking_prev_button).setEnabled(true);
            }
            if (currentQuestion == quizSize) {
                showFinalResults();
            }
        }

    }


    //te listenery
    //sprawdzanie wyniku i wyswitlanie w osobnym layoucie n
    //selecty w helperze

    private int addToLayout(View v, int prev, int rightOrBelow) {//dodac argument below czy po prawej od ostatniego elementu
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.quiz_taking_layout);
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
        if (currOpenQuestionAns != null) {
            removeFromLaoyt(currOpenQuestionAns);
            currOpenQuestionAns = null;
        }
        if (answerTexts != null) {
            for (int i = 0; i < 4; i++) {
                removeFromLaoyt(checkBoxes[i]);
                removeFromLaoyt(answerTexts[i]);
            }
            answerTexts = null;
        }
    }

    private void removeFromLaoyt(View v) {
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.quiz_taking_layout);
        mainLayout.removeView(v);
    }


    private void showFinalResults() {
        int points = 0;
        boolean[] questionResult = new boolean[questions.size()];
        //sprawdz odpowiedzi, wywswietl liste

        for (int currAns = 0; currAns < quizSize; currAns++) {
            Question q = questions.get(currAns);
            Answer a = answers[currAns];
            if (q.getType() == QuestionType.OPEN) {
                OpenQuestion openQuestion = (OpenQuestion) q;
                OpenAnswer openAnswer = (OpenAnswer) a;
                if (openQuestion.getStringAnswer().equalsIgnoreCase(openAnswer.getVal())) {
                    points++;
                    questionResult[currAns] = true;
                } else {
                    questionResult[currAns] = false;
                }
            } else if (q.getType() == QuestionType.CLOSED) {
                ClosedQuestion closedQuestion = (ClosedQuestion) q;
                ClosedAnswer closedAnswer = (ClosedAnswer) a;
                questionResult[currAns] = areEqual(closedQuestion.getCheckboxes(), closedAnswer.getCheckboxes());
                if (questionResult[currAns]) {
                    points++;
                }
            }
        }
        //tekst na czerwonym lub zielonym
        setContentView(R.layout.results_layout);
        //ustaw licznik na ile zdobytych
        displayResult(points, questionResult);

    }


    private boolean areEqual(boolean[] arr1, boolean[] arr2) {
        if (arr1.length != arr2.length) return false;

        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) return false;
        }
        return true;
    }


    private void updateCounterLabel() {
        TextView textView = (TextView) (findViewById(R.id.quiz_taking_counter));
        textView.setText(currentQuestion + "/" + quizSize);
    }

    private void displayResult(int points, boolean[] questionResult) {
        TextView textView = (TextView) (findViewById(R.id.final_result_points));
        textView.setText("You scored " + points + " on " + quizSize + ".");

        //dodaj calycontent do wodiku
        //jeszcze scrypty i testowanie
        int prev = textView.getId();
        for (int i = 0; i < quizSize; i++) {
            TextView content = new TextView(getBaseContext());
            content.setText(questions.get(i).getContent());
            if (questionResult[i]) {
                content.setTextColor(Color.GREEN);

            } else {
                content.setTextColor(Color.RED);

            }

            prev = addToLayout(content, prev, RelativeLayout.BELOW);

        }
    }
}
