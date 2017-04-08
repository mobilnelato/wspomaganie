package pl.edu.agh.mobilne_2017.activ;

import android.app.Activity;
import android.os.Bundle;
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
}
