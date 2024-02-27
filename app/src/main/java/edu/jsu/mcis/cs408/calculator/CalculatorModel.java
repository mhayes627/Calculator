package edu.jsu.mcis.cs408.calculator;

import android.util.Log;

public class CalculatorModel extends AbstractModel {

    public static final String TAG = "CalculatorModel";

    /*
     * This is a simple implementation of an AbstractModel which encapsulates
     * two text fields, text1 and text2, which (in this example) are each
     * reflected in the View as an EditText field and a TextView label.
     */

    private StringBuilder leftOperand;
    private StringBuilder rightOperand;
    private String operator;
    private String digit;
    private CalculatorState state;
    private final String DEFAULT_DIGIT = "0";

    /*
     * Initialize the model elements to known default values.  We use the setter
     * methods instead of changing the values directly so that these changes are
     * properly announced to the Controller, and so that the Views can be updated
     * accordingly.
     */

    public void initDefault() {
        state = CalculatorState.CLEAR;
        digit = DEFAULT_DIGIT;

        leftOperand = new StringBuilder();
        rightOperand = new StringBuilder();

    }

    public void setNewDigit(String newDigit){

        String oldDigit = this.digit;

        if (state.equals(CalculatorState.CLEAR) || state.equals(CalculatorState.LHS)){
            state = CalculatorState.LHS;
            leftOperand = new StringBuilder(oldDigit);
            leftOperand.append(newDigit);

            this.digit = Integer.valueOf(leftOperand.toString()).toString();
        }
        else if (state.equals(CalculatorState.OP_SELECTED) || state.equals(CalculatorState.RHS)){
            state = CalculatorState.RHS;
            rightOperand = new StringBuilder(oldDigit);
            rightOperand.append(newDigit);

            this.digit = Integer.valueOf(rightOperand.toString()).toString();
        }

        firePropertyChange(CalculatorController.ELEMENT_NEW_DIGIT, oldDigit, this.digit);
    }

    public void setOperator(String newOperator){
        if (state.equals(CalculatorState.LHS) || state.equals(CalculatorState.OP_SELECTED) || state.equals(CalculatorState.RESULT)){
            operator = newOperator;
            state = CalculatorState.OP_SELECTED;
            this.digit = DEFAULT_DIGIT;

            firePropertyChange(CalculatorController.ELEMENT_OPERATOR, null, operator);
        }
        else if (state.equals(CalculatorState.RHS)){
            operator = newOperator;
            state = CalculatorState.RESULT;
            setResult(rightOperand.toString());
        }

    }

    public void setResult(String value){
        int operand1 = Integer.parseInt(leftOperand.toString());
        int operand2 = Integer.parseInt(rightOperand.toString());
        int result;


        switch (operator) {

            case "-":
                result = operand1 - operand2;
                break;
            default:
                result = operand1 + operand2;
                break;
        }
        firePropertyChange(CalculatorController.ELEMENT_RESULT, null, result);
        state = CalculatorState.RESULT;
        leftOperand = new StringBuilder(String.valueOf(result));
    }

    public void setClear(String clear){
        state = CalculatorState.CLEAR;
        this.digit = DEFAULT_DIGIT;
        firePropertyChange(CalculatorController.ELEMENT_CLEAR, null, clear);
    }

}