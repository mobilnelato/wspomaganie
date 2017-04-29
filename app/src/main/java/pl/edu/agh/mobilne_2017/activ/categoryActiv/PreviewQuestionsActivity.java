package pl.edu.agh.mobilne_2017.activ.categoryActiv;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.mobilne_2017.activ.CategoryMenu;
import pl.edu.agh.mobilne_2017.db.DatabaseHelper;
import pl.edu.agh.mobilne_2017.model.Question;
import pl.edu.agh.mobilne_2017.R;
import pl.edu.agh.mobilne_2017.model.QuestionType;

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
        String category = this.getIntent().getExtras().getString("category");
        TextView categoryHeader = (TextView) findViewById(R.id.see_questions_header);
        categoryHeader.setText("Category: " + category);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Question> questions = db.getAllQuestions(category);
        int prev = categoryHeader.getId();
        if (questions == null) {
            TextView value = new TextView(this);
            value.setText("No questions yet!");
            addToLayout(value, prev, RelativeLayout.BELOW, RelativeLayout.CENTER_IN_PARENT);
        } else {

            for (int i = 0; i < questions.size(); i++) {
                //dodaj text z contentem + button edit a do tego buttona daj
                // onclick listener a temu onlcik listenerowi dasz id pytania w  kontuktorze. ten listener robi intent do edit question
                TextView value = new TextView(this);
                value.setText(i + ")" + questions.get(i).getContent());
                value.setPadding(50, 50, 50, 0);
                value.setTextColor(Color.parseColor("#000000"));
                value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                prev = addToLayout(value, prev, RelativeLayout.BELOW, RelativeLayout.ALIGN_PARENT_LEFT);
                Button editButton = new Button(this);
                editButton.setId(View.generateViewId());
                editButton.setOnClickListener(new EditQuestionListener(questions.get(i).getId(), category));
                editButton.setText("Edit");

                RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.see_questions);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, prev);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mainLayout.addView(editButton, params);



                Button deleteButton = new Button(this);
                deleteButton.setOnClickListener(new DeleteQuestionListener(questions.get(i).getId(), questions.get(i).getType(), category));
                deleteButton.setText("Delete");

                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.RIGHT_OF, editButton.getId());
                params.addRule(RelativeLayout.ALIGN_BASELINE, editButton.getId());
                mainLayout.addView(deleteButton, params);
                prev =editButton.getId();
                //addToLayout(editButton, prev, RelativeLayout.RIGHT_OF, RelativeLayout.ALIGN_BASELINE);
            }
        }


    }

    private int addToLayout(View v, int prev, int rightOrBelow, int alignLeftOrRight) {//dodac argument below czy po prawej od ostatniego elementu
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.see_questions);
        int curr = View.generateViewId();
        v.setId(curr);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(alignLeftOrRight, RelativeLayout.TRUE);
        params.addRule(rightOrBelow, prev);
        mainLayout.addView(v, params);
        return curr;
    }

    private class EditQuestionListener implements View.OnClickListener {
        private final long questionId;
        private final String category;

        public EditQuestionListener(long questionId, String category) {
            this.questionId = questionId;
            this.category = category;
        }

        public void onClick(View arg0) {
            Log.w("EditQuestionListener", "QuestionId " + questionId);
            Intent categoryActivity = new Intent(getBaseContext(), EditQuestionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("category", category);

            bundle.putLong("questionId", questionId);
            categoryActivity.putExtras(bundle);
            startActivity(categoryActivity);

        }
    }


    private class DeleteQuestionListener implements View.OnClickListener{
        private final long id;
        private final QuestionType questionType;
        private final String category;
        public DeleteQuestionListener(long id, QuestionType questionType, String category) {
            this.id = id;
            this.questionType = questionType;
            this.category = category;
        }

        @Override
        public void onClick(View v) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            Log.w("SaveEditQuestionListene", "Saving edit changes on quesiton ");

            db.deleteQuestion(id,questionType);
            Intent mainActivity = new Intent(getBaseContext(), PreviewQuestionsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("category", category);
            mainActivity.putExtras(bundle);
            startActivity(mainActivity);
        }
    }

}
