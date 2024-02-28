package edu.jsu.mcis.cs408.calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

        leftOperand = new StringBuilder(DEFAULT_DIGIT);
        rightOperand = new StringBuilder(DEFAULT_DIGIT);
    }

    public void setNewDigit(String newDigit){

        String oldDigit = this.digit;

        if (state.equals(CalculatorState.CLEAR) || state.equals(CalculatorState.LHS)){
            state = CalculatorState.LHS;
            leftOperand = new StringBuilder(oldDigit);
            leftOperand.append(newDigit);

            if (leftOperand.charAt(0) == '0' && leftOperand.charAt(1) != '.'){
                leftOperand.deleteCharAt(0);
            }

            this.digit = leftOperand.toString();
        }
        else if (state.equals(CalculatorState.OP_SELECTED) || state.equals(CalculatorState.RHS)){
            state = CalculatorState.RHS;
            rightOperand = new StringBuilder(oldDigit);
            rightOperand.append(newDigit);

            if (rightOperand.charAt(0) == '0' && rightOperand.charAt(1) != '.'){
                rightOperand.deleteCharAt(0);
            }

            this.digit = rightOperand.toString();
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

        if (operator.equals("\u221A")){
            setResult(leftOperand.toString());
        }
    }

    public void setResult(String value){
        BigDecimal operand1 = new BigDecimal(leftOperand.toString());
        BigDecimal operand2 = new BigDecimal(rightOperand.toString());
        BigDecimal result = new BigDecimal(DEFAULT_DIGIT);

        switch (operator) {

            case "-":
                result = operand1.subtract(operand2);
                break;
            case "\u00D7":
                result = operand1.multiply(operand2);
                break;
            case "\u00F7":
                result = operand1.divide(operand2, 5, 0);
                break;
            case "\u221A":
                if (state.equals(CalculatorState.OP_SELECTED) ){
                    result = BigDecimal.valueOf(Math.sqrt(operand1.doubleValue()));
                }
                else if (state.equals(CalculatorState.RESULT)){
                    result = BigDecimal.valueOf(Math.sqrt(operand2.doubleValue()));
                }
                break;
//            case "%":
//                break;
//            case "\u00B1":
//
//                break;
            default:
                result = operand1.add(operand2);
                break;
        }

        if (result.remainder(new BigDecimal(1)).equals(BigDecimal.valueOf(0.0))){
            firePropertyChange(CalculatorController.ELEMENT_RESULT, null, result.intValue());
        }
        else{
            firePropertyChange(CalculatorController.ELEMENT_RESULT, null, result);
        }

        state = CalculatorState.RESULT;
        leftOperand = new StringBuilder(result.toString());
    }

    public void setClear(String clear){
        state = CalculatorState.CLEAR;
        this.digit = DEFAULT_DIGIT;
        firePropertyChange(CalculatorController.ELEMENT_CLEAR, null, clear);
    }

}