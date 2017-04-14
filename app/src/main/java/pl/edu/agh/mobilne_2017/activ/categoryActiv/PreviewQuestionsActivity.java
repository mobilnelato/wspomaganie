package pl.edu.agh.mobilne_2017.activ.categoryActiv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.mobilne_2017.DatabaseHelper;
import pl.edu.agh.mobilne_2017.model.Question;
import pl.edu.agh.mobilne_2017.R;

public class PreviewQuestionsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_questions_layout);
    }

    @Override
    //ma pobierac pytania z bazy danych i wysiwetlac je
    //wraz z guzikiem edit
    protected void onResume() {
        super.onResume();
        String category = this.getIntent().getExtras().getString("categoryId");
        TextView categoryHeader = (TextView) findViewById(R.id.see_questions_header);
        categoryHeader.setText("Category: " + category);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Question> questions = db.getAllQuestions(category);
        int prev = categoryHeader.getId();
        for (int i = 0; i < questions.size(); i++) {
            //dodaj text z contentem + button edit a do tego buttona daj
            // onclick listener a temu onlcik listenerowi dasz id pytania w  kontuktorze. ten listener robi intent do edit question
            TextView value = new TextView(this);
            prev = addToLayout(value, prev, RelativeLayout.BELOW);
            Button editButton = new Button(this);
            editButton.setOnClickListener(new EditQuestionListener(questions.get(i).getId()));
            editButton.setText("Edit");
            prev = addToLayout(editButton, prev, RelativeLayout.RIGHT_OF);
        }

    }

    private int addToLayout(View v, int prev, int rightOrBelow) {//dodac argument below czy po prawej od ostatniego elementu
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.see_questions);
        int curr = View.generateViewId();
        v.setId(curr);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(rightOrBelow, prev);
        mainLayout.addView(v, params);
        return curr;
    }

    private class EditQuestionListener implements View.OnClickListener {
        private final int questionId;

        public EditQuestionListener(int questionId) {
            this.questionId = questionId;
        }

        public void onClick(View arg0) {
            Log.w("EditQuestionListener", "QuestionId "+questionId);
            Intent categoryActivity = new Intent(getBaseContext(), EditQuestionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("questionId", questionId);
            categoryActivity.putExtras(bundle);
            startActivity(categoryActivity);

        }
    }

}
