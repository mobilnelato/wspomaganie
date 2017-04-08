package pl.edu.agh.mobilne_2017.activ;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import pl.edu.agh.mobilne_2017.R;

public class CategoryMenu extends Activity{
    private String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_category_layout);
        category =  this.getIntent().getExtras().getString("categoryId");
        TextView tv = (TextView) findViewById(R.id.categoryheader);
        tv.setText("Category: "+category);


        //teraz dodac listenery
    }

    private class NewQuestionListener implements View.OnClickListener{

        private final String category;
        NewQuestionListener(String category){
            this.category = category;
        }

        @Override
        public void onClick(View v) {
            Intent categoryActivity = new Intent(getBaseContext(), CategoryMenu.class);
            Bundle bundle = new Bundle();
            bundle.putString("categoryId", category);
            categoryActivity.putExtras(bundle);
            startActivity(categoryActivity);
        }
    }


    private class PreviewQuestionsListener implements View.OnClickListener{

        private final String category;
        PreviewQuestionsListener(String category){
            this.category = category;
        }

        @Override
        public void onClick(View v) {
            Intent categoryActivity = new Intent(getBaseContext(), CategoryMenu.class);
            Bundle bundle = new Bundle();
            bundle.putString("categoryId", category);
            categoryActivity.putExtras(bundle);
            startActivity(categoryActivity);
        }
    }


    private class TakeQuizListener implements View.OnClickListener{

        private final String category;
        TakeQuizListener(String category){
            this.category = category;
        }

        @Override
        public void onClick(View v) {
            Intent categoryActivity = new Intent(getBaseContext(), CategoryMenu.class);
            Bundle bundle = new Bundle();
            bundle.putString("categoryId", category);
            categoryActivity.putExtras(bundle);
            startActivity(categoryActivity);
        }
    }
}
