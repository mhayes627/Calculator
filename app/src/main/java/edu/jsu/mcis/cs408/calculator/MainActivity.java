package edu.jsu.mcis.cs408.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int KEYS_HEIGHT = 4;
    private static final int KEYS_WIDTH = 5;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initLayout();
    }

    public void initLayout(){
        ConstraintLayout layout = binding.layout;
        ConstraintSet set = new ConstraintSet();

        TextView tv = new TextView(this);
        tv.setId(View.generateViewId());
        tv.setTag("textView" + 1);
        tv.setText("0");
        tv.setTextSize(48);
        layout.addView(tv);

        set.clone(layout);
        set.connect(tv.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT, 70);

        ViewGroup.LayoutParams params = tv.getLayoutParams();
        params.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        tv.setLayoutParams(params);

//        int[][] horizontals = new int[KEYS_HEIGHT][KEYS_WIDTH];
//        int[][] verticals = new int[KEYS_WIDTH][KEYS_HEIGHT];
//
//        for (int row = 0; i < KEYS_WIDTH; ++row) {
//            for(int col = 0; i < KEYS_HEIGHT; ++col){
//                int id = View.generateViewId(); // generate new ID
//                TextView tv = new TextView(this); // create new TextView
//                tv.setId(id); // assign ID
//                tv.setTag("textView" + col); // assign tag (for acquiring references later)
//                tv.setText("TextView Chain Element " + col); // set text (using a string resource)
//                tv.setTextSize(24); // set size
//                layout.addView(tv); // add to layout
//                viewIds[col] = id; // store ID to collection
//            }
//
//        }

    }
}