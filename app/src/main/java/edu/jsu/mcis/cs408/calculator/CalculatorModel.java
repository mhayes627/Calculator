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

    private String digit;

    /*
     * Initialize the model elements to known default values.  We use the setter
     * methods instead of changing the values directly so that these changes are
     * properly announced to the Controller, and so that the Views can be updated
     * accordingly.
     */

    public void initDefault() {
        digit = "0";

        leftOperand = new StringBuilder();
        rightOperand = new StringBuilder();

    }

    /*
     * Simple getter methods for text1 and text2
     */
//
//    public String getLeftOperand() {
//        return leftOperand;
//    }
//
//    public String getRightOperand() {
//        return rightOperand;
//    }

    /*
     * Setters for text1 and text2.  Notice that, in addition to changing the
     * values, these methods announce the change to the controller by firing a
     * PropertyChange event.  Any registered AbstractController subclasses will
     * receive this event, and will propagate it to all registered Views so that
     * they can update themselves accordingly.
     */


    public void setNewDigit(String newDigit){

        String oldDigit = this.digit;

        leftOperand = new StringBuilder(oldDigit);
        leftOperand.append(newDigit);

        this.digit = Integer.valueOf(leftOperand.toString()).toString();

        firePropertyChange(CalculatorController.ELEMENT_NEW_DIGIT, oldDigit, this.digit);
    }

}