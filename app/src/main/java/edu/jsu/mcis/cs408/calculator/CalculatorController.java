package edu.jsu.mcis.cs408.calculator;

public class CalculatorController extends AbstractController{

    /*
     * These static property names are used as the identifiers for the elements
     * of the Models and Views which may need to be updated.  These updates can
     * be a result of changes to the Model which must be reflected in the View,
     * or a result of changes to the View (in response to user input) which must
     * be reflected in the Model.
     */

    public static final String ELEMENT_NEW_DIGIT = "NewDigit";
    public static final String ELEMENT_OPERATOR = "Operator";
    public static final String ELEMENT_VALUE = "Value";

    /*
     * This is the change method which corresponds to ELEMENT_TEXT1_PROPERTY.
     * It receives the new data for the Model, and invokes "setModelProperty()"
     * (inherited from AbstractController) so that the proper Model can be found
     * and updated properly.
     */

    public void appendNewDigit(String newDigit){
        setModelProperty(ELEMENT_NEW_DIGIT, newDigit);
    }

    public void useOperator(String newOperator) { setModelProperty(ELEMENT_OPERATOR, newOperator); }

    public void calculate(String value){ setModelProperty(ELEMENT_VALUE, value);}
}
