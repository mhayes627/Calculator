package edu.jsu.mcis.cs408.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final int KEYS_HEIGHT = 4;
    private static final int KEYS_WIDTH = 5;

    int[][] horizontals = new int[KEYS_HEIGHT][KEYS_WIDTH];
    int[][] verticals = new int[KEYS_WIDTH][KEYS_HEIGHT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initLayout();
    }

    private void initLayout(){
        ConstraintLayout layout = binding.layout;
        ConstraintSet set = new ConstraintSet();

        // adding TextView
        TextView display = new TextView(this);
        display.setId(View.generateViewId());
        display.setTag(getResources().getString(R.string.display_tag));
        display.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        display.setText(getResources().getString(R.string.display_text));
        display.setTextSize(48);

        layout.addView(display);

        // Connecting TextView to ConstraintLayout
        set.clone(layout);
        set.connect(display.getId(), ConstraintSet.RIGHT, binding.guideEast.getId(),
                ConstraintSet.RIGHT, 0);
        set.connect(display.getId(), ConstraintSet.TOP, binding.guideNorth.getId(),
                ConstraintSet.TOP, 0);
        set.connect(display.getId(), ConstraintSet.LEFT, binding.guideWest.getId(),
                ConstraintSet.LEFT, 0);

        // Adding Buttons
        for (int row = 0; row < KEYS_HEIGHT; ++row) {
            for (int col = 0; col < KEYS_WIDTH; ++col) {

                int index = (row * KEYS_WIDTH) + col;
                int id = View.generateViewId();

                Button button = new Button(this);
                button.setId(id);
                button.setTag(getResources().getStringArray(R.array.button_text)[index]);
                button.setText(getResources().getStringArray(R.array.button_text)[index]);
                button.setTextSize(24);

                layout.addView(button);

                horizontals[row][col] = id;
                verticals[col][row] = id;
            }
        }

        // Chaining buttons together into a grid
        for (int row = 0; row < KEYS_HEIGHT; ++row) {
            set.createHorizontalChain(binding.guideWest.getId(), ConstraintSet.LEFT, binding.guideEast.getId(),
                    ConstraintSet.RIGHT, horizontals[row], null, ConstraintSet.CHAIN_PACKED);
        }
        for (int col = 0; col < KEYS_WIDTH; ++col) {
            set.createVerticalChain(display.getId(), ConstraintSet.BOTTOM, binding.guideSouth.getId(),
                    ConstraintSet.BOTTOM, verticals[col], null, ConstraintSet.CHAIN_PACKED);
        }

        // Applies ConstraintSet to layout
        set.applyTo(layout);

        // Setting layout parameters for display
        LayoutParams params = display.getLayoutParams();
        params.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        display.setLayoutParams(params);
    }
}