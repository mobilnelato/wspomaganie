package pl.edu.agh.mobilne_2017.activ;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import pl.edu.agh.mobilne_2017.db.DatabaseHelper;
import pl.edu.agh.mobilne_2017.R;


public class NewCategory extends Activity {
    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_category_layout);
        findViewById(R.id.savecat).setOnClickListener(new NewCategoryListener());
    }

    private class NewCategoryListener implements View.OnClickListener {
        public void onClick(View arg0) {
            db = new DatabaseHelper(getApplicationContext());
            EditText tv = (EditText) findViewById(R.id.newcattitle);
            Log.w("NewCategoryListener", tv.getText().toString());
            db.createCategory(tv.getText().toString());
        }
    }
}
