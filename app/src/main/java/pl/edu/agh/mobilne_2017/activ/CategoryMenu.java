package pl.edu.agh.mobilne_2017.activ;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.agh.mobilne_2017.R;
import pl.edu.agh.mobilne_2017.activ.categoryActiv.AddQuestionActivity;
import pl.edu.agh.mobilne_2017.activ.categoryActiv.PreviewQuestionsActivity;
import pl.edu.agh.mobilne_2017.activ.categoryActiv.TakeQuizActivity;

public class CategoryMenu extends Activity {
    private String category;
    private int questionsNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_category_layout);


        //a moze to w on rezume ???
        category = this.getIntent().getExtras().getString("categoryId");
        questionsNumber = this.getIntent().getExtras().getInt("questionsNumber");

        TextView tv = (TextView) findViewById(R.id.categoryheader);
        tv.setText("Category: " + category);
        findViewById(R.id.generatequiz).setOnClickListener(new TakeQuizListener(category, questionsNumber));
        findViewById(R.id.seequestions).setOnClickListener(new PreviewQuestionsListener(category));
        findViewById(R.id.addquestion).setOnClickListener(new NewQuestionListener(category));

    }

    private class NewQuestionListener implements View.OnClickListener {

        private final String category;

        NewQuestionListener(String category) {
            this.category = category;
        }

        @Override
        public void onClick(View v) {
            Intent categoryActivity = new Intent(getBaseContext(), AddQuestionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("categoryId", category);
            categoryActivity.putExtras(bundle);
            startActivity(categoryActivity);
        }
    }

    private class PreviewQuestionsListener implements View.OnClickListener {

        private final String category;

        PreviewQuestionsListener(String category) {
            this.category = category;
        }

        @Override
        public void onClick(View v) {
            Intent categoryActivity = new Intent(getBaseContext(), PreviewQuestionsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("categoryId", category);
            categoryActivity.putExtras(bundle);
            startActivity(categoryActivity);
        }
    }

    private class TakeQuizListener implements View.OnClickListener {

        private final String category;
        private final int questionsNumber;

        TakeQuizListener(String category, int questions) {
            this.category = category;
            questionsNumber = questions;
        }

        @Override
        public void onClick(View v) {
            if (questionsNumber > 0) {
                //przeczytja z dialogu ile pytan w quizie
                showPickerDialog();
            } else {
                errorDialog();
            }
        }
        //no quesiotns so quiz cannot be taken
        public void errorDialog() {
            final Dialog dialog = new Dialog(getBaseContext()); // Context, this, etc.
            dialog.setContentView(R.layout.no_questions_layout);
            dialog.setTitle(R.string.noquestions);
            dialog.show();
        }
    }

    private class DialogPicker implements DialogInterface.OnClickListener {

        private final NumberPicker picker;

        public DialogPicker(NumberPicker picker) {
            this.picker = picker;
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            int size = picker.getValue();
            Intent categoryActivity = new Intent(getBaseContext(), TakeQuizActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("categoryId", category);
            bundle.putInt("quizSize", size);
            categoryActivity.putExtras(bundle);
            startActivity(categoryActivity);
        }
    }

    private void showPickerDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getBaseContext());
        alert.setTitle("Title");
        alert.setMessage("Message");
        // Set an EditText view to get user input
        NumberPicker picker = new NumberPicker(getBaseContext());
        picker.setMinValue(1);
        picker.setMaxValue(questionsNumber);
        alert.setView(picker);
        alert.setPositiveButton("Ok", new DialogPicker(picker));
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }
}
