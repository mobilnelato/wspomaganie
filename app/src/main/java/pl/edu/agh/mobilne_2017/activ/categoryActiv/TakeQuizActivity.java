package pl.edu.agh.mobilne_2017.activ.categoryActiv;

import android.app.Activity;
import android.os.Bundle;

import pl.edu.agh.mobilne_2017.R;


public class TakeQuizActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_category_layout);

        String category = this.getIntent().getExtras().getString("categoryId");
        int size = this.getIntent().getExtras().getInt("quizSize");
    }
}
