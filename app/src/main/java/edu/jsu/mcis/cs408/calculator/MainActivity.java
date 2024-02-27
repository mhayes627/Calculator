package edu.jsu.mcis.cs408.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.beans.PropertyChangeEvent;

import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements AbstractView{

    ActivityMainBinding binding;

    public static final String TAG = "MainActivity";

    private CalculatorController controller;

    private static final int KEYS_HEIGHT = 4;
    private static final int KEYS_WIDTH = 5;

    int[][] horizontals = new int[KEYS_HEIGHT][KEYS_WIDTH];
    int[][] verticals = new int[KEYS_WIDTH][KEYS_HEIGHT];

    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initLayout();

        /* Create Controller and Model */

        controller = new CalculatorController();
        CalculatorModel model = new CalculatorModel();

        /* Register Activity View and Model with Controller */

        controller.addView(this);
        controller.addModel(model);

        /* Initialize Model to Default Values */

        model.initDefault();

        /* Associate Click Handler with Input Buttons */

        CalculatorClickHandler click = new CalculatorClickHandler();
        ConstraintLayout layout = binding.layout;

        for (int i = 0; i < layout.getChildCount(); ++i) {
            View child = layout.getChildAt(i);
            if(child instanceof Button) {
                child.setOnClickListener(click);
            }
        }
    }

    @Override
    public void modelPropertyChange(final PropertyChangeEvent evt) {

        /*
         * This method is called by the "propertyChange()" method of AbstractController
         * when a change is made to an element of a Model.  It identifies the element that
         * was changed and updates the View accordingly.
         */

        String propertyName = evt.getPropertyName();
        String propertyValue = evt.getNewValue().toString();

        Log.i(TAG, "New " + propertyName + " Value from Model: " + propertyValue);

        if ( propertyName.equals(CalculatorController.ELEMENT_NEW_DIGIT) ) {

            String oldPropertyValue = display.getText().toString();

            if (!oldPropertyValue.equals(propertyValue)){
                display.setText(propertyValue);
            }

        }
        else if ( propertyName.equals(CalculatorController.ELEMENT_OPERATOR) ) {
            String oldPropertyValue = display.getText().toString();

            if (!oldPropertyValue.equals(propertyValue)){
                display.setText(propertyValue);
            }

        }
        else if ( propertyName.equals(CalculatorController.ELEMENT_VALUE) ) {
            String oldPropertyValue = display.getText().toString();

            if (!oldPropertyValue.equals(propertyValue)){
                display.setText(propertyValue);
            }

        }
        else if ( propertyName.equals(CalculatorController.ELEMENT_CLEAR) ) {
            String oldPropertyValue = display.getText().toString();

            if (!oldPropertyValue.equals(propertyValue)){
                display.setText(propertyValue);
            }

        }

    }

    class CalculatorClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String tag = v.getTag().toString();
            Toast toast = Toast.makeText(binding.getRoot().getContext(), tag, Toast.LENGTH_SHORT);
            toast.show();

            /*
             * When the "Change" buttons are clicked, inform the controller of an input field
             * change, so that the Model(s) can be updated accordingly.
             */
            tag = ((Button) v).getTag().toString();

            switch(tag){
                case "1":
                    controller.appendNewDigit(tag);
                    break;
                case "2":
                    controller.appendNewDigit(tag);
                    break;
                case "3":
                    controller.appendNewDigit(tag);
                    break;
                case "4":
                    controller.appendNewDigit(tag);
                    break;
                case "5":
                    controller.appendNewDigit(tag);
                    break;
                case "6":
                    controller.appendNewDigit(tag);
                    break;
                case "7":
                    controller.appendNewDigit(tag);
                    break;
                case "8":
                    controller.appendNewDigit(tag);
                    break;
                case "9":
                    controller.appendNewDigit(tag);
                    break;
                case "0":
                    controller.appendNewDigit(tag);
                    break;
                case "+":
                    controller.useOperator(tag);
                    break;
                case "-":
                    controller.useOperator(tag);
                    break;
                case "\u00D7":
                    controller.useOperator(tag);
                    break;
                case "\u00F7":
                    controller.useOperator(tag);
                    break;
                case "\u221A":
                    controller.useOperator(tag);
                    break;
                case "%":
                    break;
                case ".":
                    controller.appendNewDigit(tag);
                    break;
                case "\u00B1":
                    break;
                case "C":
                    controller.clear(getResources().getString(R.string.display_text));
                    break;
                case "=":
                    controller.calculate(tag);
                    break;
            }

        }
    }

    private void initLayout(){
        ConstraintLayout layout = binding.layout;
        ConstraintSet set = new ConstraintSet();

        // adding TextView
        display = new TextView(this);
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