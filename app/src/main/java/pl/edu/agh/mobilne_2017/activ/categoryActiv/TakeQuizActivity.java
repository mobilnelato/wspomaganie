package pl.edu.agh.mobilne_2017.activ.categoryActiv;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.edu.agh.mobilne_2017.MainActivity;
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

    private CheckBox[] checkBoxes = null;
    private TextView[] answerTexts = null;
    private EditText currOpenQuestionAns;
    Answer[] answers;
    int currentQuestion = 0;
    int quizSize;
    List<Question> questions;
    TextView textViewTimer;
    CounterClass timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_taking_layout);

        String category = this.getIntent().getExtras().getString("category");
        quizSize = this.getIntent().getExtras().getInt("quizSize");
        answers = new Answer[quizSize];

        //instancojnuj bhelpera, i wylosuj tyle pytaz z tej kategorii
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        questions = db.getRandomQuestions(category, quizSize);
        //ustaw pierwsze pytanie
        updateCounterLabel();
        setQuestionInLayout(questions.get(0));
        //prev rob disable jak counter jest na zero
        (findViewById(R.id.quiz_taking_prev_button)).setEnabled(false);


        //dodaj listener do prev i next


        //jak next dojdzie do konca to sprawdz wyniki, zmien lauout i tma podaj punkty  -- na osobnym layouce
        findViewById(R.id.quiz_taking_prev_button).setOnClickListener(new PreviousQuestionListener());
        findViewById(R.id.quiz_taking_next_button).setOnClickListener(new NextQuestionListener());
        //odpowiedziami bedzi list<Answers>

        //counter
        textViewTimer = (TextView) findViewById(R.id.textViewTimer);
        timer = new CounterClass(15000 * quizSize + 7000, 1000);
        timer.start();
    }

    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            System.out.println("QUIZ SIZE  "  + quizSize);
            System.out.println("millisInFuture " + millisInFuture + " countDownInterval " + countDownInterval);
        }

        @Override
        public void onTick(long l) {
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(l),
                    TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                    TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
            System.out.println(hms);
            textViewTimer.setText(hms);
        }

        @Override
        public void onFinish() {
            textViewTimer.setText("End of time!");
            saveAnswer();
            showFinalResults();
        }
    }

    private void setQuestionInLayout(Question q) {
        cleanLayout();
        //ustaw text pytnia
        TextView tv = (TextView) findViewById(R.id.quiz_taking_content);
        tv.setText(q.getContent());
        int prev = findViewById(R.id.quiz_taking_content).getId();
        if (q.getType() == QuestionType.CLOSED) {
            checkBoxes = new CheckBox[4];
            answerTexts =  new TextView[4];
            ClosedQuestion closed = (ClosedQuestion) q;
            //dodac 4 razy checkbox i tekst
            for (int i = 0; i < 4; i++) {

               // addToLayout(answerTexts[i], prev, RelativeLayout.RIGHT_OF);

                CheckBox ch1 = new CheckBox(getBaseContext());
                prev = addToLayout(ch1, prev, RelativeLayout.BELOW,R.id.quiz_taking_layout,true);
                checkBoxes[i] = ch1;
                answerTexts[i] = new TextView(getBaseContext());
                answerTexts[i].setTextColor(Color.parseColor("#000000"));
                answerTexts[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                answerTexts[i].setText(closed.getAnsws()[i]);

                RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.quiz_taking_layout);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.RIGHT_OF, prev);
                params.addRule(RelativeLayout.ALIGN_BASELINE, prev);
                mainLayout.addView(answerTexts[i], params);

            }
        } else if (q.getType() == QuestionType.OPEN) {
            currOpenQuestionAns = new EditText(getBaseContext());
            currOpenQuestionAns.setEms(30);
            addToLayout(currOpenQuestionAns, prev, RelativeLayout.BELOW,R.id.quiz_taking_layout,false);
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
            setQuestionInLayout(questions.get(currentQuestion));

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
                timer.cancel();
                showFinalResults();
            }else{
                setQuestionInLayout(questions.get(currentQuestion));
            }
        }

    }


    //te listenery
    //sprawdzanie wyniku i wyswitlanie w osobnym layoucie n
    //selecty w helperze

    private int addToLayout(View v, int prev, int rightOrBelow,int layoutId,boolean isCheckbox) {//dodac argument below czy po prawej od ostatniego elementu
        RelativeLayout mainLayout = (RelativeLayout) findViewById(layoutId);
        int curr = View.generateViewId();
        v.setId(curr);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(rightOrBelow, prev);
        if(isCheckbox){
            params.addRule(RelativeLayout.ALIGN_LEFT,R.id.quiz_taking_prev_button);
        }else{
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        }
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
            if (answers[currAns] != null) {
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
        textView.setText(currentQuestion+1 + "/" + quizSize);
    }

    private void displayResult(int points, boolean[] questionResult) {
        TextView textView = (TextView) (findViewById(R.id.final_result_points));
        textView.setText("You scored " + points + " on " + quizSize + ".");

        //dodaj calycontent do wodiku
        //jeszcze scrypty i testowanie
        int prev = textView.getId();
        prev = addResultToLayout(new TextView(getBaseContext()), prev);
        for (int i = 0; i < quizSize; i++) {
            TextView content = new TextView(getBaseContext());
            content.setText(questions.get(i).getContent());
            content.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            if (questionResult[i]) {
                content.setTextColor(Color.GREEN);
            } else {
                content.setTextColor(Color.RED);
            }
            prev = addResultToLayout(content, prev);
        }


        Button backButton = new Button(getBaseContext());
        backButton.setText("Back to Menu");
        backButton.setOnClickListener(new BackToMainMenuButtonListener());
        addResultToLayout(backButton,prev);
    }


    private int addResultToLayout(View v, int prev) {//dodac argument below czy po prawej od ostatniego elementu
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.results_layout_id);
        int curr = View.generateViewId();
        v.setId(curr);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, prev);
        mainLayout.addView(v, params);
        return curr;
    }

    class BackToMainMenuButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent mainActivity = new Intent(getBaseContext(), MainActivity.class);
            startActivity(mainActivity);
        }
    }
}
