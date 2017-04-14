package pl.edu.agh.mobilne_2017;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Map;

import pl.edu.agh.mobilne_2017.activ.CategoryMenu;
import pl.edu.agh.mobilne_2017.activ.NewCategory;
import pl.edu.agh.mobilne_2017.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        db = new DatabaseHelper(getApplicationContext());
        Map<String, Integer> categories = db.getCategoriesWithQuestionNumbers();
        int prev = R.id.begginning;
        for (String cat : categories.keySet()) {
            Button b = new Button(this);
            b.setText(cat + " (" + categories.get(cat) + ")");
            b.setOnClickListener(new GoToCategoryListener(cat, categories.get(cat)));
            prev = addToLayout(b, prev);
        }
        Button b = new Button(this);
        b.setText("Add new...");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newCategory = new Intent(getBaseContext(), NewCategory.class);
                startActivity(newCategory);
            }
        });
        addToLayout(b, prev);
    }

    private int addToLayout(View v, int prev) {
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.activity_main2);
        int curr = View.generateViewId();
        v.setId(curr);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, prev);
        mainLayout.addView(v, params);
        return curr;
    }

    private class GoToCategoryListener implements View.OnClickListener {
        private final int questionsNumber;
        private final String category;

        GoToCategoryListener(String category, int questionsNumber) {
            this.category = category;
            this.questionsNumber = questionsNumber;
        }

        @Override
        public void onClick(View v) {
            Intent categoryActivity = new Intent(getBaseContext(), CategoryMenu.class);
            Bundle bundle = new Bundle();
            bundle.putString("categoryId", category);
            bundle.putInt("questionsNumber", questionsNumber);
            categoryActivity.putExtras(bundle);
            startActivity(categoryActivity);
        }
    }

}
