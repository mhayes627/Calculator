package edu.jsu.mcis.cs408.calculator;

import java.math.BigDecimal;

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
    private final String ERROR_MESSAGE = "ERROR";
    private final int MAX_DIGIT = 12;

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

            if (leftOperand.length() < MAX_DIGIT) {
                if (!newDigit.equals(".") || !leftOperand.toString().contains(newDigit)){
                    leftOperand.append(newDigit);
                }
            }

            if (leftOperand.charAt(0) == '0' && leftOperand.charAt(1) != '.') {
                leftOperand.deleteCharAt(0);
            }

            this.digit = leftOperand.toString();
        }
        else if (state.equals(CalculatorState.OP_SELECTED) || state.equals(CalculatorState.RHS) || state.equals(CalculatorState.RESULT)){
            state = CalculatorState.RHS;
            rightOperand = new StringBuilder(oldDigit);

            if (rightOperand.length() < MAX_DIGIT){
                if (!newDigit.equals(".") || !rightOperand.toString().contains(newDigit)){
                    rightOperand.append(newDigit);
                }
            }

            if (rightOperand.charAt(0) == '0' && rightOperand.charAt(1) != '.') {
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
            setResult(operator);
            operator = newOperator;
        }
    }

    public void setResult(String value){
        BigDecimal operand1 = new BigDecimal(leftOperand.toString());
        BigDecimal operand2 = new BigDecimal(rightOperand.toString());
        BigDecimal result = new BigDecimal(DEFAULT_DIGIT);

        switch (operator) {

            case "+":
                result = operand1.add(operand2);
                break;
            case "-":
                result = operand1.subtract(operand2);
                break;
            case "\u00D7":
                result = operand1.multiply(operand2);
                break;
            case "\u00F7":
                try {
                    result = operand1.divide(operand2, MAX_DIGIT, 0);
                }
                catch(ArithmeticException e){
                    state = CalculatorState.ERROR;
                }
                break;
        }

        if (state.equals(CalculatorState.ERROR)){
            firePropertyChange(CalculatorController.ELEMENT_RESULT, null, ERROR_MESSAGE);
        }
        else{
            double db = result.doubleValue();
            if (db % 1 == 0){
                firePropertyChange(CalculatorController.ELEMENT_RESULT, null, result.intValue());
            }
            else{
                firePropertyChange(CalculatorController.ELEMENT_RESULT, null, result);
            }

            state = CalculatorState.RESULT;
            this.digit = DEFAULT_DIGIT;
            leftOperand = new StringBuilder(result.toString());
        }

    }

    public void setPercent(String percent){
        if (state.equals(CalculatorState.LHS)){
            firePropertyChange(CalculatorController.ELEMENT_PERCENT, null, DEFAULT_DIGIT);
        }
        else if (state.equals(CalculatorState.RHS)){
            BigDecimal operand1 = new BigDecimal(leftOperand.toString());
            BigDecimal operand2 = new BigDecimal(rightOperand.toString());
            BigDecimal result;

            operand2 = operand2.divide(new BigDecimal(100));
            result = operand1.multiply(operand2);

            rightOperand = new StringBuilder(result.toString());
            firePropertyChange(CalculatorController.ELEMENT_PERCENT, null, rightOperand);
        }
    }

    public void setSquareRoot(String value){
        if (state.equals(CalculatorState.LHS)){
            leftOperand = sqrt(leftOperand.toString());
            firePropertyChange(CalculatorController.ELEMENT_SQUARE, null, leftOperand);
        }
        else if (state.equals(CalculatorState.RHS)){
            rightOperand = sqrt(rightOperand.toString());
            firePropertyChange(CalculatorController.ELEMENT_SQUARE, null, rightOperand);
        }
    }

    public void setSign(String value){
        if (state.equals(CalculatorState.LHS)){
            leftOperand = negate(leftOperand.toString());
            firePropertyChange(CalculatorController.ELEMENT_SIGN, null, leftOperand);
        }
        else if (state.equals(CalculatorState.RHS)){
            rightOperand = negate(rightOperand.toString());
            firePropertyChange(CalculatorController.ELEMENT_SIGN, null, rightOperand);
        }
    }

    public void setClear(String clear){
        state = CalculatorState.CLEAR;
        this.digit = DEFAULT_DIGIT;
        firePropertyChange(CalculatorController.ELEMENT_CLEAR, null, clear);
    }

    private StringBuilder sqrt(String operand){

        Double db = Math.sqrt(Double.parseDouble(operand));

        if (db % 1 == 0){
            operand = String.valueOf(db.intValue());
        }
        else{
            BigDecimal format = new BigDecimal(db);
            operand = format.setScale(MAX_DIGIT, 0).toString();
        }

        return new StringBuilder(operand);
    }

    private StringBuilder negate(String operand){
        BigDecimal result = new BigDecimal(operand);
        result = result.negate();

        return new StringBuilder(result.toString());
    }

}